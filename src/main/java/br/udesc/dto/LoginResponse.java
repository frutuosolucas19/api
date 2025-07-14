package br.udesc.dto;

public class LoginResponse {
    public String nome;
    public String tipoUsuario;

    public LoginResponse(String nome, String tipoUsuario) {
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
    }
}    