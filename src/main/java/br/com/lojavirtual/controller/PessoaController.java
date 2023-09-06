package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.dto.CepDTO;
import br.com.lojavirtual.dto.ConsultaCnpjDto;
import br.com.lojavirtual.enums.TipoPessoa;
import br.com.lojavirtual.model.Endereco;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.repository.EnderecoRepository;
import br.com.lojavirtual.repository.PesssoaFisicaRepository;
import br.com.lojavirtual.repository.PesssoaRepository;
import br.com.lojavirtual.service.PessoaUserService;
import br.com.lojavirtual.util.ValidaCNPJ;
import br.com.lojavirtual.util.ValidaCPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PessoaController {
    @Autowired
    private PesssoaRepository pesssoaRepository;

    @Autowired
    private PessoaUserService pessoaUserService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PesssoaFisicaRepository pessoaFisicaRepository;


    @ResponseBody
    @GetMapping(value = "**/consultaPfNome/{nome}")
    public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome){

        List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
        return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/consultaPfCpf/{cpf}")
    public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf") String cpf){

        List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorCPFPF(cpf);
        return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
    }



    @ResponseBody
    @GetMapping(value = "**/consultaNomePj/{pj}")
    public ResponseEntity<List<PessoaJuridica>> consultaNomePj(@PathVariable("nome") String nome){

        List<PessoaJuridica> juridicas = pesssoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());
        return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/consultaCnpjPj/{cnpj}")
    public ResponseEntity<List<PessoaJuridica>> consultaCnpjPj(@PathVariable("cnpj") String cnpj){

        List<PessoaJuridica> juridicas = pesssoaRepository.existeCnpjCadastradoList(cnpj);
        return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/consultaCep/{cep}")
    public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
        return new ResponseEntity<CepDTO>(pessoaUserService.consultaCep(cep), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/consultaCnpjReceitaWs/{cnpj}")
    public ResponseEntity<ConsultaCnpjDto> consultaCnpjReceitaWs(@PathVariable("cnpj") String cnpj){
        return new ResponseEntity<ConsultaCnpjDto>(pessoaUserService.consultaCnpjReceitaWS(cnpj), HttpStatus.OK);
    }


    /*end-point é microsservicos é um API*/
    @ResponseBody
    @PostMapping(value = "**/salvarPj")
    public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionJava {

        if (pessoaJuridica == null) {
            throw new ExceptionJava("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaJuridica.getTipoPessoa() == null) {
            throw new ExceptionJava("Informe o tipo Jurídico ou Fornecedor da Loja");
        }

        if (pessoaJuridica.getId() == null && pesssoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionJava("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
        }


        if (pessoaJuridica.getId() == null && pesssoaRepository.existeInsEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
            throw new ExceptionJava("Já existe Inscrição estadual cadastrado com o número: " + pessoaJuridica.getInscEstadual());
        }


        if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
            throw new ExceptionJava("Cnpj : " + pessoaJuridica.getCnpj() + " está inválido.");
        }


        if(pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
                CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

                pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
                pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
                pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

            }
         }else{
            for(int p = 0; p < pessoaJuridica.getEnderecos().size(); p++){
                Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

                if(!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep()))
                {
                    CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

                    pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                    pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                    pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
                    pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
                    pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
                }
            }

        }


        pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
    }

    /*end-point é microsservicos é um API*/
    @ResponseBody
    @PostMapping(value = "**/salvarPf")
    public ResponseEntity<PessoaFisica> salvarPf(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionJava{

        if (pessoaFisica == null) {
            throw new ExceptionJava("Pessoa fisica não pode ser NULL");
        }

        if(pessoaFisica.getTipoPessoa() == null){
            pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
        }

        if (pessoaFisica.getId() == null && pesssoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
            throw new ExceptionJava("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
        }


        if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
            throw new ExceptionJava("CPF : " + pessoaFisica.getCpf() + " está inválido.");
        }

        pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
    }


}