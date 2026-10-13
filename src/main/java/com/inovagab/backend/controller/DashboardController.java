package com.inovagab.backend.controller;

import com.inovagab.backend.dto.DashboardResumo;
import com.inovagab.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasAuthority('ROLE_LIDER')")
    public ResponseEntity<DashboardResumo> resumo() {
        return ResponseEntity.ok(service.gerarResumo());
    }

    @GetMapping("/estrategia/{estrategiaId}")
    @PreAuthorize("hasAuthority('ROLE_LIDER')")
    public ResponseEntity<DashboardResumo> resumoPorEstrategia(
            @PathVariable String estrategiaId
    ) {
        return ResponseEntity.ok(
                service.gerarResumoPorEstrategia(estrategiaId)
        );
    }
}