package com.inovagab.backend.controller;

import com.inovagab.backend.model.Projeto;
import com.inovagab.backend.service.ProjetoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService service;

    public ProjetoController(ProjetoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GESTOR', 'LIDER')")
    public ResponseEntity<List<Projeto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GESTOR', 'LIDER')")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/estrategia/{estrategiaId}")
    @PreAuthorize("hasAnyRole('GESTOR', 'LIDER')")
    public ResponseEntity<List<Projeto>> listarPorEstrategia(
            @PathVariable String estrategiaId) {

        return ResponseEntity.ok(
                service.listarPorEstrategia(estrategiaId)
        );
    }

    @GetMapping("/ideia/{ideiaId}")
    @PreAuthorize("hasAnyRole('GESTOR', 'LIDER')")
    public ResponseEntity<List<Projeto>> listarPorIdeia(
            @PathVariable String ideiaId) {

        return ResponseEntity.ok(
                service.listarPorIdeia(ideiaId)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    public ResponseEntity<Projeto> criar(
            @RequestBody Projeto projeto) {

        return ResponseEntity.ok(
                service.criar(projeto)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    public ResponseEntity<Projeto> atualizar(
            @PathVariable String id,
            @RequestBody Projeto projeto) {

        return ResponseEntity.ok(
                service.atualizar(id, projeto)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    public ResponseEntity<Void> excluir(
            @PathVariable String id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}