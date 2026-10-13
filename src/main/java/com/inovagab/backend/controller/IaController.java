package com.inovagab.backend.controller;

import com.inovagab.backend.dto.AnaliseIaResponse;
import com.inovagab.backend.service.IdeiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class IaController {

    private final IdeiaService service;

    public IaController(IdeiaService service) {
        this.service = service;
    }

    // Gestor/Líder pedem análise de prioridade por IA (Gemini)
    @PostMapping("/analisar/{ideiaId}")
    @PreAuthorize("hasAnyRole('GESTOR', 'LIDER')")
    public ResponseEntity<AnaliseIaResponse> analisar(
            @PathVariable String ideiaId
    ) {
        return ResponseEntity.ok(service.analisarComIa(ideiaId));
    }
}
