package com.inovagab.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ideias")
public class Ideia {

    @Id
    private String id;

    private String titulo;

    private String descricao;

    private StatusIdeia status;
    
    private PrioridadeIdeia prioridade;

    private String operadorId;

    private String estrategiaId;

    private LocalDateTime dataCriacao;

    private Integer xp;

    public Ideia() {
    }

    public Ideia(
            String titulo,
            String descricao,
            StatusIdeia status,
            PrioridadeIdeia prioridade,
            String operadorId,
            String estrategiaId,
            LocalDateTime dataCriacao,
            Integer xp
    ) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.operadorId = operadorId;
        this.estrategiaId = estrategiaId;
        this.dataCriacao = dataCriacao;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusIdeia getStatus() {
        return status;
    }

    public void setStatus(StatusIdeia status) {
        this.status = status;
    }

    public PrioridadeIdeia getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeIdeia prioridade) {
        this.prioridade = prioridade;
    }

    public String getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(String operadorId) {
        this.operadorId = operadorId;
    }

    public String getEstrategiaId() {
        return estrategiaId;
    }

    public void setEstrategiaId(String estrategiaId) {
        this.estrategiaId = estrategiaId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }
}