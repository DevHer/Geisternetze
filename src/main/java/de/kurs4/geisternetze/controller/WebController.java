package de.kurs4.geisternetze.controller;

import de.kurs4.geisternetze.entity.GhostNet;
import de.kurs4.geisternetze.entity.Person;
import de.kurs4.geisternetze.service.GhostNetService;
import de.kurs4.geisternetze.service.MonthlyStatService;
import de.kurs4.geisternetze.repository.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    private final GhostNetService service;
    private final PersonRepository personRepo;
    private final MonthlyStatService statService;

    public WebController(GhostNetService service, PersonRepository personRepo, MonthlyStatService statService) {
        this.service = service;
        this.personRepo = personRepo;
        this.statService = statService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<GhostNet> reported = service.listByStatus("Gemeldet");
        List<GhostNet> toBeRescued = service.listByStatus("Bergung bevorstehend");
        List<GhostNet> archive = service.listRecoveredOrMissingSince(LocalDateTime.now().minusDays(30));

        model.addAttribute("reported", reported);
        model.addAttribute("toBeRescued", toBeRescued);
        model.addAttribute("archive", archive);

        LocalDate now = LocalDate.now();
        LocalDateTime monthStart = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = monthStart.plusMonths(1);

        long reportedThisMonth = service.countReportedInPeriod(monthStart, monthEnd);
        long recoveredThisMonth = service.countByStatusInMonth("Geborgen", monthStart, monthEnd);

        model.addAttribute("recoveredThisMonth", recoveredThisMonth);
        model.addAttribute("reportedThisMonth", reportedThisMonth);

        return "index";
    }

    @GetMapping("/map")
    public String mapPage() {
        return "map";
    }

    @GetMapping("/report")
    public String reportForm(Model model) {
        model.addAttribute("ghostNet", new GhostNet());
        return "report";
    }


    @PostMapping("/report")
    public String submitReport(@ModelAttribute GhostNet ghostNet,
                               @RequestParam(required = false) String reporterName,
                               @RequestParam(required = false) String reporterPhone) {
        if (reporterName != null && !reporterName.isBlank()) {
            Person reporter = new Person();
            reporter.setName(reporterName);
            reporter.setPhone(reporterPhone);
            ghostNet.setReporter(reporter);
        } else {
            ghostNet.setReporter(null);
        }
        try {
            service.reportGhostNet(ghostNet, false);

            statService.incrementReported();
        } catch (IllegalArgumentException e) {
            return "redirect:/report?error=" + e.getMessage();
        }
        return "redirect:/";
    }

    @PostMapping("/claim/{id}")
    public String claim(@PathVariable Long id, @RequestParam String rescuerName, @RequestParam String rescuerPhone) {
        Person rescuer = new Person();
        rescuer.setName(rescuerName);
        rescuer.setPhone(rescuerPhone);
        try {
            service.claimNet(id, rescuer);
        } catch (Exception e) {
        }
        return "redirect:/";
    }

    @PostMapping("/recover/{id}")
public String recover(@PathVariable Long id,
                      @RequestParam(required = false) String rescuerPhone,
                      RedirectAttributes redirectAttrs) {
    try {

        GhostNet g = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Geisternetz nicht gefunden: " + id));
        Person rescuer = g.getRescuer();


        if (rescuer == null || rescuer.getPhone() == null || rescuer.getPhone().isBlank()) {
            redirectAttrs.addFlashAttribute("error", "Keine bergende Person mit Telefonnummer hinterlegt.");
            return "redirect:/";
        }


        String stored = normalizePhone(rescuer.getPhone());
        String provided = normalizePhone(rescuerPhone);

        if (provided.isBlank() || !stored.equals(provided)) {
            redirectAttrs.addFlashAttribute("error", "Telefonnummer stimmt nicht überein. Geborgene Netze können nur von der bergenden Person bestätigt werden.");
            return "redirect:/";
        }

 
        service.markRecovered(id);
        statService.incrementRecovered();
        redirectAttrs.addFlashAttribute("info", "Netz erfolgreich als geborgen markiert.");
        return "redirect:/";
    } catch (Exception e) {
        redirectAttrs.addFlashAttribute("error", "Fehler beim Markieren als geborgen.");
        return "redirect:/";
    }
}


private String normalizePhone(String p) {
    if (p == null) return "";
    return p.replaceAll("[^0-9+]", "").trim();
}


    @PostMapping("/missing/{id}")
    public String markMissing(@PathVariable Long id,
                              @RequestParam(required = false) String reporterName,
                              @RequestParam(required = false) String reporterPhone) {
        try {
            service.markMissing(id, reporterName, reporterPhone);
        } catch (IllegalArgumentException e) {
            return "redirect:/?error=" + e.getMessage();
        } catch (Exception e) {
            return "redirect:/?error=Fehler beim Verschollenmelden";
    }
    return "redirect:/";
}

    @GetMapping("/stats")
    public String stats(Model model) {
        model.addAttribute("months", statService.listAll());
        return "stats";
    }
}
