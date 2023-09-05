package br.com.lojavirtual;

import br.com.lojavirtual.controller.PessoaController;
import br.com.lojavirtual.enums.TipoEndereço;
import br.com.lojavirtual.model.Endereco;
import br.com.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

    @Autowired
    private PessoaController pessoaController;


    @Test
    public void testCadPessoaFisica() throws ExceptionJava {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj("65825978000138");
        pessoaJuridica.setNome("Tiago Nascimento");
        pessoaJuridica.setEmail("tiagoNascimento@gmail.com");
        pessoaJuridica.setTelefone("45999795800");
        pessoaJuridica.setInscEstadual("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setInscMunicipal("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNomeFantasia("54556565665");
        pessoaJuridica.setRazaoSocial("4656656566");
        pessoaJuridica.setTipoPessoa("juridica");

        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Barroso");
        endereco1.setCep("2342242");
        endereco1.setComplemento("casa");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setNumero("2345");
        endereco1.setPessoa(pessoaJuridica);
        endereco1.setRuaLogra("Av Joao Pessoa");
        endereco1.setCidade("Fortaleza");
        endereco1.setTipoEndereço(TipoEndereço.COBRANCA);
        endereco1.setUf("Ce");

        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Cambeba");
        endereco2.setCep("2342242");
        endereco2.setCidade("Fortaleza");
        endereco2.setComplemento("ap");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setNumero("25");
        endereco2.setPessoa(pessoaJuridica);
        endereco2.setRuaLogra("Pedro Alencar");
        endereco2.setTipoEndereço(TipoEndereço.ENTREGA);
        endereco2.setUf("Ce");

        pessoaJuridica.getEnderecos().add(endereco1);
        pessoaJuridica.getEnderecos().add(endereco2);


        pessoaJuridica =  pessoaController.salvarPj(pessoaJuridica).getBody();

        assertEquals(true, pessoaJuridica.getId() > 0);

        for(Endereco endereco : pessoaJuridica.getEnderecos()){
            assertEquals(true, endereco.getId() > 0);
        }

        assertEquals(2, pessoaJuridica.getEnderecos().size());
    }

}
