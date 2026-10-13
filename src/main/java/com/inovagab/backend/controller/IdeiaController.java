package com.inovagab.backend.controller;

import com.inovagab.backend.dto.AtualizarPrioridadeRequest;
import com.inovagab.backend.model.Ideia;
import com.inovagab.backend.service.IdeiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideias")
public class IdeiaController {

    private final IdeiaService service;

    public IdeiaController(IdeiaService service) {
        this.service = service;
    }

    // Gestor e Líder podem consultar todas as ideias
    @GetMapping
    @PreAuthorize("hasAnyRole('GESTOR', 'LIDER')")
    public ResponseEntity<List<Ideia>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Operador consulta apenas as próprias ideias
    @GetMapping("/operador/{operadorId}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GESTOR', 'LIDER')")
    public ResponseEntity<List<Ideia>> listarPorOperador(
            @PathVariable String operadorId
    ) {
        return ResponseEntity.ok(service.listarPorOperador(operadorId));
    }

    // Qualquer perfil autenticado pode consultar uma ideia específica
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GESTOR', 'LIDER')")
    public ResponseEntity<Ideia> buscarPorId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Operador cria ideia
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_OPERADOR')")
    public ResponseEntity<Ideia> criar(
            @RequestBody Ideia ideia
    ) {
        return ResponseEntity.ok(service.criar(ideia));
    }

    // Gestor altera prioridade (recebe { "prioridade": "ALTA" } no corpo)
    @PatchMapping("/{id}/prioridade")
    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    public ResponseEntity<Ideia> atualizarPrioridade(
            @PathVariable String id,
            @RequestBody AtualizarPrioridadeRequest request
    ) {
        return ResponseEntity.ok(
                service.atualizarPrioridade(id, request.prioridade())
        );
    }

    // Gestor aprova ideia
    @PatchMapping("/{id}/aprovar")
    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    public ResponseEntity<Ideia> aprovar(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.aprovar(id));
    }

    // Gestor arquiva ideia
    @PatchMapping("/{id}/arquivar")
    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    public ResponseEntity<Ideia> arquivar(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.arquivar(id));
    }

    // Apenas o Líder pode excluir
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_LIDER')")
    public ResponseEntity<Void> excluir(
            @PathVariable String id
    ) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}