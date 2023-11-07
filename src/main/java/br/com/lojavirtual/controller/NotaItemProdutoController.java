package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.model.NotaItemProduto;
import br.com.lojavirtual.repository.NotaItemProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public class NotaItemProdutoController {

    @Autowired
    private NotaItemProdutoRepository notaItemProdutoRepository;

    @ResponseBody
    @PostMapping(value = "**salvarNotaItemProduto")
    public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionJava {

        if (notaItemProduto.getId() == null) {

            if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {
                throw new ExceptionJava("O produto deve ser informado");
            }

            if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {
                throw new ExceptionJava("A Nota deve ser informado");
            }

            if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {
                throw new ExceptionJava("A Empresa deve ser informado");
            }



            List<NotaItemProduto> notaExistente = notaItemProdutoRepository.
                    buscaNotaItemPorProdutoNota(notaItemProduto.getProduto().getId(), notaItemProduto.getNotaFiscalCompra().getId());

            if (!notaExistente.isEmpty()) {
                throw new ExceptionJava("Já existe este produto acdastrado para esta nota");
            }
        }

        if(notaItemProduto.getQuantidade() <= 0){
            throw new ExceptionJava("A quantidade do produto deve ser informada");
        }

        NotaItemProduto notaItemSalva = notaItemProdutoRepository.save(notaItemProduto);

        return new ResponseEntity<NotaItemProduto>(notaItemProduto, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteNotaItemPorId/{id}")
    public ResponseEntity<?> deleteNotaItemPorId(@PathVariable("id") Long id) {

        notaItemProdutoRepository.deleteByIdNotaitem(id);/*deleta o pai*/

        return new ResponseEntity("Nota Item Produto Removido",HttpStatus.OK);
    }
}
