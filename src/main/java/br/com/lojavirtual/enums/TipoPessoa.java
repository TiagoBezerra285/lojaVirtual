package br.com.lojavirtual.enums;

public enum TipoPessoa {

    JURIDICA("juridica"),
    JURIDICA_FORNECEDOR("Jurídica e Fornecedor"),
    FISICA("Física");

    private String descricao;

    private TipoPessoa(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return "TipoPessoa{" +
                "descricao='" + descricao + '\'' +
                '}';
    }
}
