package test.gfp.model;

import static junit.framework.Assert.assertEquals;
import gfp.dto.SaldoCategoriaDto;
import gfp.model.Categoria;
import gfp.model.Lancamento;
import gfp.model.Usuario;
import gfp.type.CategoriaType;
import gfp.type.FormaPagamentoType;

import java.util.List;

import logus.commons.datetime.AbstractDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../config/applicationContext-gfp.xml" })
public class LancamentoTest {
	
	private Usuario usuario;
	
	@Before
	public void setup() throws Exception {
		this.usuario = new Usuario("nome", "login", "senha").save();
	}
	
	@After
	public void tearDown() throws Exception {
		Usuario.dao.deleteAll();
	}
	
	@Test
	public void testCalcularDataCompensacao() throws Exception {
		final Categoria cr = new Categoria(this.usuario, "descricao",
				CategoriaType.RECEITA).save();
		
		Lancamento l;
		
		l = new Lancamento(this.usuario, cr, 300.0, FormaPagamentoType.CHEQUE);
		l.setDataPagamento(AbstractDateTime.date(5, 11, 2010));
		l.setValorPago(l.getValorOriginal());
		l = l.save();
		assertEquals(AbstractDateTime.date(9, 11, 2010), l.getDataCompensacao());
		
		l = new Lancamento(this.usuario, cr, 300.0, FormaPagamentoType.CHEQUE);
		l.setDataPagamento(AbstractDateTime.time(
				AbstractDateTime.date(4, 11, 2010), "23:00:00"));
		l.setValorPago(l.getValorOriginal());
		l = l.save();
		assertEquals(AbstractDateTime.date(9, 11, 2010), l.getDataCompensacao());
	}
	
	@Test
	public void testListarSaldoCategoriaMensal() throws Exception {
		final List<SaldoCategoriaDto> listaSaldoCategoriaMensal = Lancamento
				.listarSaldoCategoriaMensal(this.usuario.getId(),
						CategoriaType.RECEITA.ordinal(),
						AbstractDateTime.getToday(),
						AbstractDateTime.getToday());
		assertEquals(0, listaSaldoCategoriaMensal.size());
	}
	
}
