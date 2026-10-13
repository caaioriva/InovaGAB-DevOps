package com.inovagab.backend.repository;

import com.inovagab.backend.model.Projeto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjetoRepository extends MongoRepository<Projeto, String> {

    List<Projeto> findByEstrategiaId(String estrategiaId);

    List<Projeto> findByIdeiaId(String ideiaId);
}