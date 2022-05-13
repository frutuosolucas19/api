package br.udesc.model;

public class DenunciaEncaminhada {

    private int id;
    private Denuncia denuncia;
    private String destinatario;
    private String assunto;
    private String mensagem;

    public DenunciaEncaminhada() {
    }

    public DenunciaEncaminhada(int id, Denuncia denuncia, String destinatario, String assunto, String mensagem) {
        this.id = id;
        this.denuncia = denuncia;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "DenunciaEncaminhada{" +
                "id=" + id +
                ", denuncia=" + denuncia +
                ", destinatario='" + destinatario + '\'' +
                ", assunto='" + assunto + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }
}
