package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.model.AvaliacaoProduto;
import br.com.lojavirtual.repository.AvaliacaoProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AvaliacaoProdutoController {

    @Autowired
    private AvaliacaoProdutoRepository avaliacaoProdutoRepository;


    @ResponseBody
    @PostMapping(value = "**/salvarAvaliacaoProduto")
    public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionJava {

        if(avaliacaoProduto.getEmpresa() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)){
            throw new ExceptionJava("Informe a empresa dona do registro");
        }

        if(avaliacaoProduto.getProduto() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)){
            throw new ExceptionJava("A avaliação deve conter o produto associado");
        }

        if(avaliacaoProduto.getPessoa() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)){
            throw new ExceptionJava("A avaliação deve conter a pessoa ou cliente associado");
        }

        avaliacaoProduto = avaliacaoProdutoRepository.saveAndFlush(avaliacaoProduto);
        return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);
    }


    @ResponseBody
    @DeleteMapping(value = "**/deleteAvaliacaoPessoa/{idAvalicao}")
    public ResponseEntity<?> deleteAvaliacaoPessoa(@PathVariable("idAvaliacao") Long idAvaliacao){

        avaliacaoProdutoRepository.deleteById(idAvaliacao);

        return new ResponseEntity<>("Avaliação Removida", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/avaliacaoPessoa/{idProduto}")
    public ResponseEntity<List<AvaliacaoProduto>> avaliacaoPessoa(@PathVariable("idPessoa") Long idPessoa){

        List<AvaliacaoProduto> avaliacaoPessoa = avaliacaoProdutoRepository.avaliacaoPessoa(idPessoa);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoPessoa, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/avaliacaoProduto/{idProduto}")
    public ResponseEntity<List<AvaliacaoProduto>> avaliacaoProduto(@PathVariable("idProduto") Long idProduto){

        List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProduto(idProduto);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/avaliacaoProdutoPessoa/{idProduto}/{idPessoa}")
    public ResponseEntity<List<AvaliacaoProduto>> avaliacaoProdutoPessoa(@PathVariable("idProduto") Long idProduto,
                                                                         @PathVariable("idPessoa") Long idPessoa){

        List<AvaliacaoProduto> avaliacaoProdutoPessoa = avaliacaoProdutoRepository.avaliacaoProdutoPessoa(idProduto, idPessoa);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutoPessoa, HttpStatus.OK);
    }
}
