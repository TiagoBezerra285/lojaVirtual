package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.model.NotaFiscalCompra;
import br.com.lojavirtual.repository.NotaFiscalCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaFiscalCompraController {

    @Autowired
    private NotaFiscalCompraRepository notaFiscalCompraRepository;


    @ResponseBody
    @PostMapping(value = "**/salvarNotaFiscalCompra") /*Mapeando a url para receber JSON*/
    public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionJava {

        if(notaFiscalCompra.getId() == null){
           if(notaFiscalCompra.getDescricaObs() != null){
               boolean existe = notaFiscalCompraRepository.existeNotaComDescricao(notaFiscalCompra.getDescricaObs().toUpperCase().trim());

               if(existe){
                   throw new ExceptionJava("Já existe Nota de Compra com essa mesma descrição : " + notaFiscalCompra.getDescricaObs());
               }
           }
        }

        if(notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0){
            throw new ExceptionJava("A Pessoa juridica da nota fiscal deve ser informada");
        }

        if(notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0){
            throw new ExceptionJava("A empresa responsavel deve ser informada");
        }

        if(notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0){
            throw new ExceptionJava("A conta a pagar da nota deve ser informada.");
        }

        NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);

        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.OK);
    }


    @ResponseBody
    @DeleteMapping(value = "**/deleteNotaFiscalPorId/{id}")
    public ResponseEntity<?> deleteNotaFiscalPorId(@PathVariable("id") Long id) {

        notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);/*deleta os filhos*/
        notaFiscalCompraRepository.deleteById(id);/*deleta o pai*/

        return new ResponseEntity("NotaFiscal Compra Removida",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterNotaFiscalCompra/{id}")
    public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionJava {

        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

        if(notaFiscalCompra == null) {
            throw  new ExceptionJava("Não Encontrou a NotaFiscal de Compra: " + id);
        }

        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/buscarNotaFiscalPorDesc/{desc}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalPorDesc(@PathVariable("desc") String desc) {

        List<NotaFiscalCompra> notaFiscal = notaFiscalCompraRepository.buscaNotaDesc(desc.toUpperCase().trim());

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscal, HttpStatus.OK);
    }
}
