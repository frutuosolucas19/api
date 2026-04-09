package br.udesc.dto;

import jakarta.validation.constraints.NotBlank;

public class AddressRequest {
    @NotBlank(message = "logradouro obrigatorio.")
    public String logradouro;
    public Integer numero;
    public String bairro;

    @NotBlank(message = "cidade obrigatoria.")
    public String cidade;

    @NotBlank(message = "uf obrigatorio.")
    public String uf;

    public String cep;
    public String complemento;
}
