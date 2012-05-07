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
		List<SaldoCategoriaDto> listaSaldoCategoriaMensal;
		
		listaSaldoCategoriaMensal = Lancamento.listarSaldoCategoriaMensal(
				this.usuario.getId(), CategoriaType.DESPESA.ordinal(),
				AbstractDateTime.date(1, 1, 2012),
				AbstractDateTime.date(31, 3, 2012));
		assertEquals(0, listaSaldoCategoriaMensal.size());
		
		final Categoria db = new Categoria(this.usuario, "descricao",
				CategoriaType.DESPESA).save();
		
		Lancamento l;
		
		l = new Lancamento(this.usuario, db, 100.0, FormaPagamentoType.DINHEIRO);
		l.setDataPrevisaoPagamento(AbstractDateTime.date(1, 1, 2012));
		l = l.save();
		
		l = new Lancamento(this.usuario, db, 200.0, FormaPagamentoType.DINHEIRO);
		l.setDataPrevisaoPagamento(AbstractDateTime.date(1, 2, 2012));
		l = l.save();
		
		l = new Lancamento(this.usuario, db, 300.0, FormaPagamentoType.DINHEIRO);
		l.setDataPrevisaoPagamento(AbstractDateTime.date(1, 3, 2012));
		l = l.save();
		
		l = new Lancamento(this.usuario, db, 50.0, FormaPagamentoType.DINHEIRO);
		l.setDataPrevisaoPagamento(AbstractDateTime.date(1, 4, 2012));
		l = l.save();
		
		listaSaldoCategoriaMensal = Lancamento.listarSaldoCategoriaMensal(
				this.usuario.getId(), CategoriaType.DESPESA.ordinal(),
				AbstractDateTime.date(1, 4, 2012),
				AbstractDateTime.date(30, 4, 2012));
		assertEquals(1, listaSaldoCategoriaMensal.size());
		assertEquals(db, listaSaldoCategoriaMensal.get(0).categoria);
		assertEquals(50.0, listaSaldoCategoriaMensal.get(0).valor);
		assertEquals(200.0, listaSaldoCategoriaMensal.get(0).previsao);
	}
	
}
