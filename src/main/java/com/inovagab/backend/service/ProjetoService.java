package com.inovagab.backend.service;

import com.inovagab.backend.exception.RecursoNaoEncontradoException;
import com.inovagab.backend.model.Projeto;
import com.inovagab.backend.model.StatusProjeto;
import com.inovagab.backend.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository repository;

    public ProjetoService(ProjetoRepository repository) {
        this.repository = repository;
    }

    public List<Projeto> listarTodos() {
        return repository.findAll();
    }

    public Projeto buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Projeto não encontrado"));
    }

    public List<Projeto> listarPorEstrategia(String estrategiaId) {
        return repository.findByEstrategiaId(estrategiaId);
    }

    public List<Projeto> listarPorIdeia(String ideiaId) {
        return repository.findByIdeiaId(ideiaId);
    }

    public Projeto criar(Projeto projeto) {

        projeto.setStatus(StatusProjeto.PLANEJAMENTO);
        projeto.setProgresso(0);
        projeto.setDataCriacao(LocalDateTime.now());

        return repository.save(projeto);
    }

    public Projeto atualizar(String id, Projeto novoProjeto) {

        Projeto projeto = buscarPorId(id);

        projeto.setNome(novoProjeto.getNome());
        projeto.setDescricao(novoProjeto.getDescricao());
        projeto.setStatus(novoProjeto.getStatus());
        projeto.setProgresso(novoProjeto.getProgresso());
        projeto.setInvestimento(novoProjeto.getInvestimento());
        projeto.setRetornoFinanceiro(novoProjeto.getRetornoFinanceiro());
        projeto.setPrazo(novoProjeto.getPrazo());
        projeto.setGanhoProdutividade(novoProjeto.getGanhoProdutividade());
        projeto.setReducaoCustos(novoProjeto.getReducaoCustos());
        projeto.setEstrategiaId(novoProjeto.getEstrategiaId());
        projeto.setIdeiaId(novoProjeto.getIdeiaId());

        return repository.save(projeto);
    }

    public void excluir(String id) {

        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Projeto não encontrado");
        }

        repository.deleteById(id);
    }
}