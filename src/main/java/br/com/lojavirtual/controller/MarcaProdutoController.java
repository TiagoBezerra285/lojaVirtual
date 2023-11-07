package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.model.Acesso;
import br.com.lojavirtual.model.MarcaProduto;
import br.com.lojavirtual.repository.AcessoRepository;
import br.com.lojavirtual.repository.MarcaRepository;
import br.com.lojavirtual.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MarcaProdutoController {

    @Autowired
    private MarcaRepository marcaRepository;

    @ResponseBody
    @PostMapping(value = "**/salvarmarcaProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<MarcaProduto> salvarmarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionJava {

        if(marcaProduto.getId() == null){
            List<MarcaProduto> marcas = marcaRepository.buscarMarcaProdutoDesc((marcaProduto.getNomeDesc().toUpperCase()));
          if(!marcas.isEmpty()){
              throw new ExceptionJava("Já existe Marca com a descrição: " + marcaProduto.getNomeDesc());
          }
        }

        MarcaProduto marcaProdutoSalvo = marcaRepository.save(marcaProduto);

        return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "**/deleteMarcaProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteMarcaProduto(@RequestBody MarcaProduto marcaProduto) { /*Recebe o JSON e converte pra Objeto*/

        marcaRepository.deleteById(marcaProduto.getId());

        return new ResponseEntity("Marca Produto Removido",HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteMarcaProdutoPorId/{id}")
    public ResponseEntity<?> deleteMarcaProdutoPorId(@PathVariable("id") Long id) {

        marcaRepository.deleteById(id);

        return new ResponseEntity("Marca Produto Removido",HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/obterMarcaProduto/{id}")
    public ResponseEntity<MarcaProduto> obterMarcaProduto(@PathVariable("id") Long id) throws ExceptionJava {

        MarcaProduto marcaProduto = marcaRepository.findById(id).orElse(null);

        if(marcaProduto == null) {
            throw  new ExceptionJava("Não Encontrou a Marca com código: " + id);
        }

        return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/buscarMarcaProdutoPorDesc/{desc}")
    public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDesc(@PathVariable("desc") String desc) {

        List<MarcaProduto> marcaProdutos = marcaRepository.buscarMarcaProdutoDesc(desc.toUpperCase().trim());

        return new ResponseEntity<List<MarcaProduto>>(marcaProdutos,HttpStatus.OK);
    }
}
