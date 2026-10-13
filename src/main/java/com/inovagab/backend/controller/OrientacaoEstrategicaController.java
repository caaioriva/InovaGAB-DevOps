package com.inovagab.backend.controller;

import com.inovagab.backend.model.OrientacaoEstrategica;
import com.inovagab.backend.service.OrientacaoEstrategicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estrategias")
public class OrientacaoEstrategicaController {

    private final OrientacaoEstrategicaService service;

    public OrientacaoEstrategicaController(
            OrientacaoEstrategicaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LIDER', 'GESTOR', 'OPERADOR')")
    public ResponseEntity<List<OrientacaoEstrategica>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIDER', 'GESTOR', 'OPERADOR')")
    public ResponseEntity<OrientacaoEstrategica> buscarPorId(
            @PathVariable String id) {

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_LIDER')")
    public ResponseEntity<OrientacaoEstrategica> criar(
            @RequestBody OrientacaoEstrategica estrategia) {

        return ResponseEntity.ok(service.criar(estrategia));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIDER')")
    public ResponseEntity<OrientacaoEstrategica> atualizar(
            @PathVariable String id,
            @RequestBody OrientacaoEstrategica estrategia) {

        return ResponseEntity.ok(
                service.atualizar(id, estrategia)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIDER')")
    public ResponseEntity<Void> excluir(
            @PathVariable String id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}