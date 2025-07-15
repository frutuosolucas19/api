package br.udesc.dto;

public class LoginResponse {
    public String nome;
    public String email;
    public String tipoUsuario;

    public LoginResponse(String nome, String email, String tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }
}    