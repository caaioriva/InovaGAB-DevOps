package com.inovagab.backend.repository;

import com.inovagab.backend.model.OrientacaoEstrategica;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrientacaoEstrategicaRepository
        extends MongoRepository<OrientacaoEstrategica, String> {
}