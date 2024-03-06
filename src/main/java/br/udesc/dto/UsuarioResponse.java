package br.udesc.dto;

import br.udesc.model.Usuario;

public class UsuarioResponse {

    private String login;
    private String tipoUser;

     public UsuarioResponse() {
    }

    public UsuarioResponse(String login, String tipoUser) {
        this.login = login;
        this.tipoUser = tipoUser;
    }

    public UsuarioResponse fromEntity(Usuario usuario){
        var response = new UsuarioResponse();
        response.setLogin(usuario.getLogin());
        response.setTipoUser(usuario.getTipoUsuario());

        return response;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    
    @Override
    public String toString() {
        return "Usuario{" +
                "login='" + login + '\'' +
                ", tipoUsuario='" + tipoUser + '\'' +
                '}';
    }

}