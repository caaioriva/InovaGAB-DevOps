package com.inovagab.backend.repository;

import com.inovagab.backend.model.Ideia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IdeiaRepository extends MongoRepository<Ideia, String> {

    List<Ideia> findByOperadorId(String operadorId);

}