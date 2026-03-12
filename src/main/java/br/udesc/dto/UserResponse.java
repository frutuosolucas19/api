package br.udesc.dto;

import br.udesc.model.Person;

public class UserResponse {
    public Long id;
    public Person pessoa;
    public String email;
    public String tipoUsuario;

    public UserResponse(Long id, Person pessoa, String email, String tipoUsuario) {
        this.id = id;
        this.pessoa = pessoa;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }
}

