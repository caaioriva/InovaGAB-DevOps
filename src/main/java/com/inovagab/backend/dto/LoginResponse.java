package com.inovagab.backend.dto;

public class LoginResponse {

    private String token;
    private String id;
    private String nome;
    private String email;
    private String perfil;

    public LoginResponse(
            String token,
            String id,
            String nome,
            String email,
            String perfil
    ) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPerfil() {
        return perfil;
    }
}