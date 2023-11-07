package br.com.lojavirtual.controller;

import br.com.lojavirtual.dto.ImagemProdutoDTO;
import br.com.lojavirtual.model.ImagemProduto;
import br.com.lojavirtual.repository.ImagemProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagemProdutoController {

    @Autowired
    private ImagemProdutoRepository imagemProdutoRepository;


    @ResponseBody
    @PostMapping(value = "**/salvarImagemProduto")
    public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto){

        imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);

        ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
        imagemProdutoDTO.setId(imagemProduto.getId());
        imagemProdutoDTO.setEmpresa(imagemProdutoDTO.getEmpresa());
        imagemProdutoDTO.setProduto(imagemProdutoDTO.getProduto());
        imagemProdutoDTO.setImagemMiniatura(imagemProdutoDTO.getImagemMiniatura());
        imagemProdutoDTO.setImagemOriginal(imagemProdutoDTO.getImagemOriginal());

        return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
    }


    @ResponseBody
    @DeleteMapping(value = "**/deleteImagemProdutoPorId/{id}")
    public ResponseEntity<?> deleteImagemProdutoPorId(@PathVariable("id") Long id){

        if(!imagemProdutoRepository.existsById(id)){
            return new ResponseEntity("Imagens já foi removida ou não existe com esse id: " + id, HttpStatus.OK);
        }

        imagemProdutoRepository.deleteById(id);
        return new ResponseEntity("Imagem removida", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteTodoImagemProduto/{idProduto}")
    public ResponseEntity<?> deleteTodoImagemProduto(@PathVariable("idProduto") Long idProduto){



        imagemProdutoRepository.deleteImagem(idProduto);
        return new ResponseEntity("Imagens do produto removida", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteTodoImagemObjeto")
    public ResponseEntity<?> deleteTodoImagemObjeto(@RequestBody ImagemProduto imagemProduto){

        if(!imagemProdutoRepository.existsById(imagemProduto.getId())){
            return new ResponseEntity("Imagens já foi removida ou não existe com esse id: " + imagemProduto.getId(), HttpStatus.OK);
        }

        imagemProdutoRepository.deleteById(imagemProduto.getId());
        return new ResponseEntity("Imagens removida", HttpStatus.OK);
    }


    @GetMapping(value = "**/obterImagemPorProduto/{idProduto}")
    public ResponseEntity<List<ImagemProdutoDTO>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto){

        List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();

        List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);

        for(ImagemProduto imagemProduto : imagemProdutos){
            ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
            imagemProdutoDTO.setId(imagemProduto.getId());
            imagemProdutoDTO.setEmpresa(imagemProdutoDTO.getEmpresa());
            imagemProdutoDTO.setProduto(imagemProdutoDTO.getProduto());
            imagemProdutoDTO.setImagemMiniatura(imagemProdutoDTO.getImagemMiniatura());
            imagemProdutoDTO.setImagemOriginal(imagemProdutoDTO.getImagemOriginal());

            dtos.add(imagemProdutoDTO);
        }

        return new ResponseEntity<List<ImagemProdutoDTO>>(dtos, HttpStatus.OK);
    }
}
