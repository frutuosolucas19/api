package br.udesc.model;

public class Local {
    
    private int id;
    private String nome;
    private Localizacao localizacao;
    private Endereco endereco;

    public Local() {
    }

    public Local(int id, String nome, Localizacao localizacao, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Local{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", localizacao=" + localizacao +
                ", endereco=" + endereco +
                '}';
    }
}
