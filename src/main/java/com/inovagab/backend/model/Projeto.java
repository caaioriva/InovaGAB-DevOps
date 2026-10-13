package com.inovagab.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "projetos")
public class Projeto {

    @Id
    private String id;

    private String nome;
    private String descricao;
    private StatusProjeto status;
    private Integer progresso;
    private Double investimento;
    private Double retornoFinanceiro;
    private LocalDate prazo;
    private Double ganhoProdutividade;
    private Double reducaoCustos;
    private String estrategiaId;
    private String ideiaId;
    private LocalDateTime dataCriacao;

    public Projeto() {
    }

    public Projeto(
            String nome,
            String descricao,
            StatusProjeto status,
            Integer progresso,
            Double investimento,
            Double retornoFinanceiro,
            LocalDate prazo,
            Double ganhoProdutividade,
            Double reducaoCustos,
            String estrategiaId,
            String ideiaId,
            LocalDateTime dataCriacao
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.progresso = progresso;
        this.investimento = investimento;
        this.retornoFinanceiro = retornoFinanceiro;
        this.prazo = prazo;
        this.ganhoProdutividade = ganhoProdutividade;
        this.reducaoCustos = reducaoCustos;
        this.estrategiaId = estrategiaId;
        this.ideiaId = ideiaId;
        this.dataCriacao = dataCriacao;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public Integer getProgresso() {
        return progresso;
    }

    public void setProgresso(Integer progresso) {
        this.progresso = progresso;
    }

    public Double getInvestimento() {
        return investimento;
    }

    public void setInvestimento(Double investimento) {
        this.investimento = investimento;
    }

    public Double getRetornoFinanceiro() {
        return retornoFinanceiro;
    }

    public void setRetornoFinanceiro(Double retornoFinanceiro) {
        this.retornoFinanceiro = retornoFinanceiro;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public Double getGanhoProdutividade() {
        return ganhoProdutividade;
    }

    public void setGanhoProdutividade(Double ganhoProdutividade) {
        this.ganhoProdutividade = ganhoProdutividade;
    }

    public Double getReducaoCustos() {
        return reducaoCustos;
    }

    public void setReducaoCustos(Double reducaoCustos) {
        this.reducaoCustos = reducaoCustos;
    }

    public String getEstrategiaId() {
        return estrategiaId;
    }

    public void setEstrategiaId(String estrategiaId) {
        this.estrategiaId = estrategiaId;
    }

    public String getIdeiaId() {
        return ideiaId;
    }

    public void setIdeiaId(String ideiaId) {
        this.ideiaId = ideiaId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}