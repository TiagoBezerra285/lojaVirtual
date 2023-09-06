package br.com.lojavirtual.service;

import br.com.lojavirtual.dto.CepDTO;
import br.com.lojavirtual.dto.ConsultaCnpjDto;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.PesssoaFisicaRepository;
import br.com.lojavirtual.repository.PesssoaRepository;
import br.com.lojavirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class PessoaUserService {


    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PesssoaRepository pessoaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    private PesssoaFisicaRepository pesssoaFisicaRepository;


    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica){

        //juridica = pessoaRepository.save(juridica);

        for(int i = 0;i < juridica.getEnderecos().size(); i++){
            juridica.getEnderecos().get(i).setPessoa(juridica);
            juridica.getEnderecos().get(i).setEmpresa(juridica);
        }

        juridica = pessoaRepository.save(juridica);


        Usuario usuariopj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());

        if(usuariopj == null){

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if(constraint != null){
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
            }

            usuariopj = new Usuario();
            usuariopj.setDataAtualSenha(Calendar.getInstance().getTime());
            usuariopj.setEmpresa(juridica);
            usuariopj.setPessoa(juridica);
            usuariopj.setLogin(juridica.getEmail());


            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuariopj.setSenha(senhaCript);
            usuariopj = usuarioRepository.save(usuariopj);

            usuarioRepository.insereAcessoUser(usuariopj.getId());
            usuarioRepository.insereAcessoUserPj(usuariopj.getId(), "ROLE_ADMIN");

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
            menssagemHtml.append("<b>Login: </b>"+juridica.getEmail()+"</b><br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try{
                serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString(), juridica.getEmail());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return juridica;
    }


    public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

        for(int i = 0;i < pessoaFisica.getEnderecos().size(); i++){
            pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
            pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
        }

        pessoaFisica = pesssoaFisicaRepository.save(pessoaFisica);


        Usuario usuariopf = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        if(usuariopf == null){

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if(constraint != null){
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
            }

            usuariopf = new Usuario();
            usuariopf.setDataAtualSenha(Calendar.getInstance().getTime());
            usuariopf.setEmpresa(pessoaFisica.getEmpresa());
            usuariopf.setPessoa(pessoaFisica);
            usuariopf.setLogin(pessoaFisica.getEmail());


            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuariopf.setSenha(senhaCript);
            usuariopf = usuarioRepository.save(usuariopf);

            usuarioRepository.insereAcessoUser(usuariopf.getId());
            //usuarioRepository.insereAcessoUserPj(usuariopf.getId(), "ROLE_ADMIN");

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
            menssagemHtml.append("<b>Login: </b>"+pessoaFisica.getEmail()+"</b><br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try{
                serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString(), pessoaFisica.getEmail());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return pessoaFisica;
    }

    public CepDTO consultaCep(String cep){
        return new RestTemplate().getForEntity("https://viacep.com.br/ws/"+ cep +"/json/", CepDTO.class).getBody();
    }

    public ConsultaCnpjDto consultaCnpjReceitaWS(String cnpj){
        return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/"+ cnpj, ConsultaCnpjDto.class).getBody();
    }
}
