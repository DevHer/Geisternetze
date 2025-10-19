package de.kurs4.geisternetze.controller;

import de.kurs4.geisternetze.entity.GhostNet;
import de.kurs4.geisternetze.service.GhostNetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    private final GhostNetService service;

    public ApiController(GhostNetService service) {
        this.service = service;
    }

    @GetMapping("/api/ghostnets")
    public List<GhostNet> getByStatus(@RequestParam(defaultValue = "Gemeldet") String status) {
        return service.listByStatus(status);
    }
}
