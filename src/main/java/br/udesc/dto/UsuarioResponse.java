package br.udesc.dto;

import br.udesc.model.Pessoa;

public class UsuarioResponse {
    public Long id;
    public Pessoa pessoa;
    public String email;
    public String tipoUsuario;

    public UsuarioResponse(Long id, Pessoa pessoa, String email, String tipoUsuario) {
        this.id = id;
        this.pessoa = pessoa;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }
}
