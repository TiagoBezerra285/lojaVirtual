package br.com.lojavirtual.enums;

public enum TipoEndereço {

    COBRANCA("cobrança"),
    ENTREGA("Entrega");

    private String descricao;

    TipoEndereço(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
