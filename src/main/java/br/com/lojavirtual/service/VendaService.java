package br.com.lojavirtual.service;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaService {


    private JdbcTemplate jdbcTemplate;


    public void ativaRegistroVendaBanco(Long idVenda){
        String sql = "begin; update vd_cp_loja_virt set excluido = true where id = "+ idVenda +"; commit;";
        jdbcTemplate.execute(sql);
    }

    public void exclusaoTotalVendaBanco(Long idVenda){

        String value =
                "begin;"
                        + " UPDATE nota_fiscal_venda set venda_compra_loja_virt_id = null where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from nota_fiscal_venda where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from item_venda_loja where venda_compra_loja_virtu_id = "+idVenda+"; "
                        + " delete from status_rastreio where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from vd_cp_loja_virt where id = ?1; "
                        + " commit; ";

        jdbcTemplate.execute(value);
    }
}