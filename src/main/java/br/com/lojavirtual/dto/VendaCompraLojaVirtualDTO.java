package br.com.lojavirtual.dto;

import br.com.lojavirtual.model.Endereco;
import br.com.lojavirtual.model.Pessoa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendaCompraLojaVirtualDTO {

    private Long id;
    private BigDecimal ValorTotal;
    private Pessoa Pessoa;
    private Endereco cobranca;
    private Endereco entrega;

    private BigDecimal valorFrete;

    private List<ItemVendaDTO> itemVendaLoja = new ArrayList<ItemVendaDTO>();

    public void setItemVendaLoja(List<ItemVendaDTO> itemVendaLoja) {
        this.itemVendaLoja = itemVendaLoja;
    }

    public List<ItemVendaDTO> getItemVendaLoja() {
        return itemVendaLoja;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Endereco getCobranca() {
        return cobranca;
    }

    public void setCobranca(Endereco cobranca) {
        this.cobranca = cobranca;
    }

    public Endereco getEntrega() {
        return entrega;
    }

    public void setEntrega(Endereco entrega) {
        this.entrega = entrega;
    }

    public br.com.lojavirtual.model.Pessoa getPessoa() {
        return Pessoa;
    }

    public void setPessoa(br.com.lojavirtual.model.Pessoa pessoa) {
        Pessoa = pessoa;
    }

    public BigDecimal getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        ValorTotal = valorTotal;
    }
}
