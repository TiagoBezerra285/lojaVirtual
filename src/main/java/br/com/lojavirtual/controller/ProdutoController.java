package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionJava;
import br.com.lojavirtual.repository.ProdutoRepository;
import br.com.lojavirtual.model.Produto;
import br.com.lojavirtual.service.ServiceSendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/salvarProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionJava, MessagingException, IOException {

        if(produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()){
            throw new ExceptionJava("Tipo da unidade deve ser informada");
        }

        if(produto.getNome().length() < 10){
            throw new ExceptionJava("Nome do produto deve ter mais de 10 letras");
        }

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

        if(produto.getQtdEstoque() < 0){
            throw new ExceptionJava("O produto dever ter no minímo 1 no estoque");
        }


        if(produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0){
            throw new ExceptionJava("Deve ser informado imagens para o produto");
        }

        if(produto.getImagens().size() < 3){
            throw new ExceptionJava("Deve ser informado pelo menos 3 imagens para o produto");
        }

        if(produto.getImagens().size() < 6){
            throw new ExceptionJava("Deve ser informado no maximo 6 imagens");
        }

        if(produto.getId() == null){
            for(int x = 0; x < produto.getImagens().size(); x++){
                produto.getImagens().get(x).setProduto(produto);
                produto.getImagens().get(x).setEmpresa(produto.getEmpresa());

                String base64Image = "";

                if(produto.getImagens().get(x).getImagemOriginal().contains("data:image")){
                    base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];
                }else{
                    base64Image = produto.getImagens().get(x).getImagemOriginal();
                }

                byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

                if(bufferedImage != null){

                    int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
                    int largura = Integer.parseInt("800");
                    int altura = Integer.parseInt("600");

                    BufferedImage resizedImage = new BufferedImage(largura, altura, type);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, largura, altura, null);
                    g.dispose();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, "png", baos);

                    String miniImgbase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());

                    produto.getImagens().get(x).setImagemMiniatura(miniImgbase64);

                    bufferedImage.flush();
                    resizedImage.flush();
                    baos.flush();
                    baos.close();
                }
            }
        }


        Produto produtoSalvo = produtoRepository.save(produto);


        if(produto.getAletaQtdeEstoque() && produto.getQtdEstoque() <= 1){

            StringBuilder html = new StringBuilder();
            html.append("<h2>").append("Produto: " + produto.getNome()).append(" com estoque baixo " + produto.getQtdEstoque());
            html.append("<p> Id Prod.:").append(produto.getId()).append("</p>");

            if(produto.getEmpresa().getEmail() != null){
                serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
            }

        }


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
