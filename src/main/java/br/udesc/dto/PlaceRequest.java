package br.udesc.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class PlaceRequest {
    @NotBlank(message = "nome obrigatorio.")
    public String nome;

    @Valid
    public LocationRequest localizacao;

    @Valid
    public AddressRequest endereco;
}
