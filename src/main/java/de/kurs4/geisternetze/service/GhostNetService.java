package de.kurs4.geisternetze.service;

import de.kurs4.geisternetze.entity.GhostNet;
import de.kurs4.geisternetze.entity.Person;
import de.kurs4.geisternetze.repository.GhostNetRepository;
import de.kurs4.geisternetze.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GhostNetService {

    private final GhostNetRepository repo;
    private final PersonRepository personRepo;

    public GhostNetService(GhostNetRepository repo, PersonRepository personRepo) {
        this.repo = repo;
        this.personRepo = personRepo;
    }

    public GhostNet reportGhostNet(GhostNet g, boolean asMissing) {
        if (asMissing && (g.getReporter() == null || g.getReporter().getName() == null || g.getReporter().getPhone() == null || g.getReporter().getPhone().isBlank())) {
            throw new IllegalArgumentException("Ein anonymes Verschollenmelden ist nicht erlaubt. Bitte Name und Telefonnummer angeben.");
        }
        g.setStatus(asMissing ? "Verschollen" : "Gemeldet");
        g.setCreatedAt(LocalDateTime.now());
        if (g.getReporter() != null && g.getReporter().getId() == null) {
            personRepo.save(g.getReporter());
        }
        return repo.save(g);
    }

    public List<GhostNet> listByStatus(String status) {
        return repo.findByStatus(status);
    }

    public List<GhostNet> listRecoveredOrMissingSince(LocalDateTime since) {
        return repo.findRecoveredOrMissingSince(since);
    }

    public long countByStatusInMonth(String status, LocalDateTime monthStart, LocalDateTime monthEnd) {
        return repo.countByStatusAndCreatedAtBetween(status, monthStart, monthEnd);
    }

    public long countReportedInPeriod(java.time.LocalDateTime start, java.time.LocalDateTime end) {
    return repo.countByCreatedAtBetween(start, end);
    }

    public Optional<GhostNet> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public GhostNet claimNet(Long id, Person rescuer) {
        GhostNet g = repo.findById(id).orElseThrow();
        if (g.getRescuer() != null) {
            throw new IllegalStateException("Netz bereits von einer bergenden Person übernommen.");
        }
        if (rescuer.getId() == null) {
            personRepo.save(rescuer);
        }
        g.setRescuer(rescuer);
        g.setStatus("Bergung bevorstehend");
        return repo.save(g);
    }

    @Transactional
    public GhostNet markRecovered(Long id) {
        GhostNet g = repo.findById(id).orElseThrow();
        g.setStatus("Geborgen");
        return repo.save(g);
    }

    @Transactional
    public GhostNet markMissing(Long id, String reporterName, String reporterPhone) {
        if (reporterName == null || reporterName.isBlank() || reporterPhone == null || reporterPhone.isBlank()) {
            throw new IllegalArgumentException("Zum Verschollenmelden sind Name und Telefonnummer erforderlich.");
        }

        GhostNet g = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Geisternetz nicht gefunden: " + id));

        Person reporter = new Person();
        reporter.setName(reporterName);
        reporter.setPhone(reporterPhone);
        personRepo.save(reporter);

        g.setReporter(reporter);       // Reporter wird gesetzt (Person, die Verschollenmeldung gemeldet hat)
        g.setStatus("Verschollen");
        g.setRescuer(null);

        return repo.save(g);
    }
}
