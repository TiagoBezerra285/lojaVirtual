package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.dto.CategoriaProdutoDto;
import br.com.lojavirtual.model.CategoriaProduto;
import br.com.lojavirtual.repository.CategoriaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaController {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;


    @ResponseBody
    @GetMapping(value = "**/buscarPorDescCategoria/{desc}")
    public ResponseEntity<List<CategoriaProduto>> buscarPorDescCastegoria(@PathVariable("desc") String desc) {

        List<CategoriaProduto> categorias = categoriaProdutoRepository.buscarCategoriaDesc(desc);

        return new ResponseEntity<List<CategoriaProduto>>(categorias,HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping(value = "**/salvarCategoria")
    public ResponseEntity<CategoriaProdutoDto> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionJava{

        if(categoriaProduto.getEmpresa() == null || categoriaProduto.getEmpresa().getId() == null){
            throw new ExceptionJava("A Empresa deve ser informada");
        }

        if(categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().toUpperCase()))
        {
            throw new ExceptionJava("Não pode cadastrar categoria com mesmo nome.");
        }


        CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);

        CategoriaProdutoDto categoriaProdutoDto = new CategoriaProdutoDto();
        categoriaProdutoDto.setId(categoriaSalva.getId());
        categoriaProdutoDto.setNomeDesc(categoriaSalva.getNomeDesc());
        categoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());

        return new ResponseEntity<CategoriaProdutoDto>(categoriaProdutoDto, HttpStatus.OK);
    }


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteCategoria") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteCategoria(@RequestBody CategoriaProduto categoriaProduto) { /*Recebe o JSON e converte pra Objeto*/

        categoriaProdutoRepository.deleteById(categoriaProduto.getId());

        return new ResponseEntity("Categoria Removida",HttpStatus.OK);
    }
}
