package br.udesc.dto;

import br.udesc.model.Person;

public class UserResponse {
    public Long id;
    public String nomePessoa;
    public String imagemPessoa;
    public String email;
    public String tipoUsuario;

    public UserResponse(Long id, Person pessoa, String email, String tipoUsuario) {
        this.id = id;
        this.nomePessoa = pessoa != null ? pessoa.getNome() : null;
        this.imagemPessoa = pessoa != null ? pessoa.getImagem() : null;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }
}
