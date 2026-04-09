package br.udesc.dto;

public class ForgotPasswordResponse {
    public String token;

    public ForgotPasswordResponse(String token) {
        this.token = token;
    }
}
