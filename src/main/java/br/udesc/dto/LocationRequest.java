package br.udesc.dto;

import jakarta.validation.constraints.NotBlank;

public class LocationRequest {
    @NotBlank(message = "latitude obrigatoria.")
    public String latitude;

    @NotBlank(message = "longitude obrigatoria.")
    public String longitude;
}
