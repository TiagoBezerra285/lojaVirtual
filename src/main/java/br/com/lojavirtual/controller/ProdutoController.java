package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.repository.ProdutoRepository;
import br.com.lojavirtual.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/salvarProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionJava {

        if(produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0){
            throw new ExceptionJava("Empresa responsável deve ser informada");
        }

        if(produto.getId() == null){
            List<Produto> produtos = produtoRepository.buscarProdutoNome((produto.getNome().toUpperCase()), produto.getEmpresa().getId());
          if(!produtos.isEmpty()){
              throw new ExceptionJava("Já existe Produto com a descrição: " + produto.getDescricao());
          }
        }

        if(produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0){
            throw new ExceptionJava("Categoria deve ser informada");
        }

        if(produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0){
            throw new ExceptionJava("Marca deve ser informada");
        }


        Produto produtoSalvo = produtoRepository.save(produto);

        return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
    }


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) { /*Recebe o JSON e converte pra Objeto*/

        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity("Produto Removido",HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteProdutoPorId/{id}")
    public ResponseEntity<?> deleteProdutoPorId(@PathVariable("id") Long id) {

        produtoRepository.deleteById(id);

        return new ResponseEntity("Produto Removido",HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/obterProduto/{id}")
    public ResponseEntity<Produto> obterAcesso(@PathVariable("id") Long id) throws ExceptionJava {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if(produto == null) {
            throw  new ExceptionJava("Não Encontrou Produto com código: " + id);
        }

        return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/buscarProdutoNome/{desc}")
    public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("desc") String desc) {

        List<Produto> produto = produtoRepository.buscarProdutoNome(desc);

        return new ResponseEntity<List<Produto>>(produto,HttpStatus.OK);
    }
}
