package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.model.ContaPagar;
import br.com.lojavirtual.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ContaPagarController {

    @Autowired
    private ContaPagarRepository contaPagarRepository;


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/salvarContaPagar") /*Mapeando a url para receber JSON*/
    public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar) throws ExceptionJava {

        if(contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0){
            throw new ExceptionJava("Empresa responsável deve ser informada");
        }

        if(contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0){
            throw new ExceptionJava("Pessoa responsável deve ser informada");
        }

        if(contaPagar.getPessoa_fornecedor() == null || contaPagar.getPessoa_fornecedor().getId() <= 0){
            throw new ExceptionJava("Fornecedor responsável deve ser informada");
        }

        if(contaPagar.getId() == null) {
            List<ContaPagar> contaPagars = contaPagarRepository.buscaContaDesc(contaPagar.getDescricao().toUpperCase().trim());
            if(!contaPagars.isEmpty()){
                throw new ExceptionJava("Já exiuste conta a pagar com a mesma descrição");
            }
        }



        ContaPagar contaPagarSalvo = contaPagarRepository.save(contaPagar);

        return new ResponseEntity<ContaPagar>(contaPagarSalvo, HttpStatus.OK);
    }


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteContaPagar") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteContaPagar(@RequestBody ContaPagar contaPagar) { /*Recebe o JSON e converte pra Objeto*/

        contaPagarRepository.deleteById(contaPagar.getId());

        return new ResponseEntity("Contas a Pagar Removida",HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteContaPagarPorId/{id}")
    public ResponseEntity<?> deleteContaPagarPorId(@PathVariable("id") Long id) {

        contaPagarRepository.deleteById(id);

        return new ResponseEntity("Conta a Pagar Removida",HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/obterContaPagar/{id}")
    public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExceptionJava {

        ContaPagar ContaPagar = contaPagarRepository.findById(id).orElse(null);

        if(ContaPagar == null) {
            throw  new ExceptionJava("Não Encontrou Conta a Pagar com código: " + id);
        }

        return new ResponseEntity<ContaPagar>(ContaPagar, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/buscarContaPagarDesc/{desc}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarDesc(@PathVariable("desc") String desc) {

        List<ContaPagar> contaPagar = contaPagarRepository.buscaContaDesc(desc.toUpperCase());

        return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
    }
}
