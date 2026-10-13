package com.inovagab.backend.service;

import com.inovagab.backend.exception.RecursoNaoEncontradoException;
import com.inovagab.backend.model.OrientacaoEstrategica;
import com.inovagab.backend.repository.OrientacaoEstrategicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrientacaoEstrategicaService {

    private final OrientacaoEstrategicaRepository repository;

    public OrientacaoEstrategicaService(OrientacaoEstrategicaRepository repository) {
        this.repository = repository;
    }

    public List<OrientacaoEstrategica> listarTodas() {
        return repository.findAll();
    }

    public OrientacaoEstrategica buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Estratégia não encontrada"));
    }

    public OrientacaoEstrategica criar(OrientacaoEstrategica orientacao) {
        return repository.save(orientacao);
    }

    public OrientacaoEstrategica atualizar(
            String id,
            OrientacaoEstrategica novaOrientacao
    ) {
        OrientacaoEstrategica orientacao = buscarPorId(id);

        orientacao.setData(novaOrientacao.getData());
        orientacao.setCategoria(novaOrientacao.getCategoria());
        orientacao.setCampanha(novaOrientacao.getCampanha());

        return repository.save(orientacao);
    }

    public void excluir(String id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Estratégia não encontrada");
        }

        repository.deleteById(id);
    }
}