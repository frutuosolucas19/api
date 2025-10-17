package br.udesc.dto;

public class LoginResponse {
    public String nome;
    public String email;
    public String tipoUsuario;  
    public String accessToken;

    public LoginResponse() {}

    public LoginResponse(String nome, String email, String tipoUsuario, String accessToken) {
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.accessToken = accessToken;
    }
}
