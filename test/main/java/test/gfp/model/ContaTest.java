package test.gfp.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import gfp.model.Conta;
import gfp.model.Usuario;
import gfp.type.ContaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../config/applicationContext-gfp.xml" })
public class ContaTest {
	
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
	public void testResetarCartoes() throws Exception {
		Conta conta = new Conta(this.usuario, ContaType.CONTA_CORRENTE,
				"Conta 1");
		conta.setAtiva(true);
		conta.setOperaCheque(true);
		conta.setOperaCartaoDebito(true);
		
		conta.setOperaCartaoMastercard(true);
		conta.setLimiteMastercard(1.0);
		conta.setFechamentoMastercard(10);
		conta.setVencimentoMastercard(15);
		
		conta.setOperaCartaoVisa(true);
		conta.setLimiteVisa(2.0);
		conta.setFechamentoVisa(20);
		conta.setVencimentoVisa(25);
		conta = conta.save();
		
		assertEquals("Conta 1", conta.getIdentificacao());
		assertTrue(conta.isAtiva());
		assertTrue(conta.isOperaCheque());
		assertTrue(conta.isOperaCartaoDebito());
		assertTrue(conta.isOperaCartaoMastercard());
		assertEquals(1.0, conta.getLimiteMastercard());
		assertEquals(10, conta.getFechamentoMastercard());
		assertEquals(15, conta.getVencimentoMastercard());
		assertTrue(conta.isOperaCartaoVisa());
		assertEquals(2.0, conta.getLimiteVisa());
		assertEquals(20, conta.getFechamentoVisa());
		assertEquals(25, conta.getVencimentoVisa());
		
		conta.setTipo(ContaType.POUPANCA.ordinal());
		conta = conta.save();
		
		assertTrue(conta.isOperaCheque());
		assertTrue(conta.isOperaCartaoDebito());
		assertFalse(conta.isOperaCartaoMastercard());
		assertEquals(0.0, conta.getLimiteMastercard());
		assertEquals(1, conta.getFechamentoMastercard());
		assertEquals(1, conta.getVencimentoMastercard());
		assertFalse(conta.isOperaCartaoVisa());
		assertEquals(0.0, conta.getLimiteVisa());
		assertEquals(1, conta.getFechamentoVisa());
		assertEquals(1, conta.getVencimentoVisa());
		
		conta.setTipo(ContaType.CARTEIRA.ordinal());
		conta = conta.save();
		
		assertFalse(conta.isOperaCheque());
		assertFalse(conta.isOperaCartaoDebito());
	}
	
}
