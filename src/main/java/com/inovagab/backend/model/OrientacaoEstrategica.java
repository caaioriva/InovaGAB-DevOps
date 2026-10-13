package com.inovagab.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "orientacoes_estrategicas")
public class OrientacaoEstrategica {

    @Id
    private String id;

    private LocalDate data;
    private String categoria;
    private String campanha;

    public OrientacaoEstrategica() {
    }

    public OrientacaoEstrategica(
            LocalDate data,
            String categoria,
            String campanha) {
        this.data = data;
        this.categoria = categoria;
        this.campanha = campanha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCampanha() {
        return campanha;
    }

    public void setCampanha(String campanha) {
        this.campanha = campanha;
    }
}