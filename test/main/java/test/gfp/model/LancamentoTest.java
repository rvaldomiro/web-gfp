package test.gfp.model;

import static junit.framework.Assert.assertEquals;
import gfp.model.Categoria;
import gfp.model.Lancamento;
import gfp.model.Usuario;
import gfp.type.CategoriaType;
import gfp.type.FormaPagamentoType;
import logus.commons.datetime.AbstractDateTime;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../config/applicationContext-gfp.xml" })
public class LancamentoTest {
	
	@After
	public void tearDown() throws Exception {
		new Lancamento().deleteAll();
		new Categoria().deleteAll();
		new Usuario().deleteAll();
	}
	
	@Test
	public void testCalcularDataCompensacao() throws Exception {
		final Usuario u = new Usuario("nome", "login", "senha").save();
		final Categoria cr = new Categoria(u, "descricao",
				CategoriaType.RECEITA).save();
		
		Lancamento l;
		
		l = new Lancamento(u, cr, 300.0, FormaPagamentoType.CHEQUE);
		l.setDataPagamento(AbstractDateTime.date(5, 11, 2010));
		l.setValorPago(l.getValorOriginal());
		l = l.save();
		assertEquals(AbstractDateTime.date(9, 11, 2010), l.getDataCompensacao());
		
		l = new Lancamento(u, cr, 300.0, FormaPagamentoType.CHEQUE);
		l.setDataPagamento(AbstractDateTime.time(
				AbstractDateTime.date(4, 11, 2010), "23:00:00"));
		l.setValorPago(l.getValorOriginal());
		l = l.save();
		assertEquals(AbstractDateTime.date(9, 11, 2010), l.getDataCompensacao());
	}
	
}
