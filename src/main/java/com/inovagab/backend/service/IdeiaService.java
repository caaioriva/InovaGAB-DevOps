package com.inovagab.backend.service;

import com.inovagab.backend.dto.AnaliseIaResponse;
import com.inovagab.backend.model.Ideia;
import com.inovagab.backend.model.PrioridadeIdeia;
import com.inovagab.backend.model.StatusIdeia;
import com.inovagab.backend.repository.IdeiaRepository;
import org.springframework.stereotype.Service;
import com.inovagab.backend.exception.RecursoNaoEncontradoException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IdeiaService {

    private final IdeiaRepository repository;
    private final GeminiService geminiService;

    public IdeiaService(IdeiaRepository repository, GeminiService geminiService) {
        this.repository = repository;
        this.geminiService = geminiService;
    }

    public List<Ideia> listarTodas() {
        return repository.findAll();
    }

    public List<Ideia> listarPorOperador(String operadorId) {
        return repository.findByOperadorId(operadorId);
    }

    public Ideia buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ideia não encontrada"));
    }

    public Ideia criar(Ideia ideia) {

        ideia.setStatus(StatusIdeia.PENDENTE);
        ideia.setPrioridade(PrioridadeIdeia.MEDIA);
        ideia.setDataCriacao(LocalDateTime.now());
        ideia.setXp(50);

        return repository.save(ideia);
    }

    public Ideia atualizarPrioridade(
            String id,
            PrioridadeIdeia prioridade
    ) {

        Ideia ideia = buscarPorId(id);

        ideia.setPrioridade(prioridade);

        return repository.save(ideia);
    }

    public Ideia aprovar(String id) {

        Ideia ideia = buscarPorId(id);

        ideia.setStatus(StatusIdeia.APROVADA);
        ideia.setXp(100);

        return repository.save(ideia);
    }

    public Ideia arquivar(String id) {

        Ideia ideia = buscarPorId(id);

        ideia.setStatus(StatusIdeia.ARQUIVADA);

        return repository.save(ideia);
    }

    public void excluir(String id) {

        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Ideia não encontrada");
        }

        repository.deleteById(id);
    }

    public AnaliseIaResponse analisarComIa(String id) {
        Ideia ideia = buscarPorId(id);
        return geminiService.analisarIdeia(ideia);
    }
}