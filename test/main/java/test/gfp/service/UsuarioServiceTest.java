package test.gfp.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import gfp.model.Categoria;
import gfp.model.Conta;
import gfp.model.Usuario;
import gfp.service.UsuarioService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../config/applicationContext-gfp.xml" })
public class UsuarioServiceTest {
	
	private UsuarioService controller;
	
	@Before
	public void setUp() throws Exception {
		this.controller = UsuarioService.getInstance();
	}
	
	@After
	public void tearDown() throws Exception {
		Usuario.dao.deleteAll();
	}
	
	@Test
	public void testLogin() throws Exception {
		try {
			this.controller.login("login", "senha");
			fail("Não é permitido login com um usuário inexistente!");
		} catch (final Exception e) {
			assertEquals("Usuário e/ou Senha inválidos!", e.getMessage());
		}
		
		new Usuario("login", "senha").save();
		
		try {
			this.controller.login("login", "senha");
			fail("Não é permitido login de usuário ativo!");
		} catch (final Exception e) {
			assertEquals("Usuário desativado!", e.getMessage());
		}
	}
	
	@Test
	public void testSalvarUsuario() throws Exception {
		final Usuario u = new Usuario("nome sobrenome", "login", "senha");
		u.setEmail("a@a.com");
		
		this.controller.salvarUsuario(u);
		assertEquals("login", u.getLogin());
		assertEquals("Nome Sobrenome", u.getNome());
		assertTrue(u.isAtivo());
		assertFalse(u.isAdministrador());
		assertEquals("senha".hashCode(), Integer.parseInt(u.getSenha()));
		assertEquals(9, Categoria.dao.allByFields("usuario", u).size());
		assertEquals(1, Conta.dao.allByFields("usuario", u).size());
		
		final Usuario u1 = new Usuario("nome sobrenome", "login", "senha");
		
		try {
			this.controller.salvarUsuario(u1);
			fail("Não é permitido mais de um usuário com o mesmo login");
		} catch (final Exception e) {
			assertEquals("Nome de usuário não disponível para cadastro!",
					e.getMessage());
		}
		
		final Usuario u2 = new Usuario("nome sobrenome", "login2", "senha");
		u2.setEmail("a@a.com");
		
		try {
			this.controller.salvarUsuario(u2);
			fail("Não é permitido mais de um usuário com o mesmo e-mail!");
		} catch (final Exception e) {
			assertEquals("E-Mail já está em uso!", e.getMessage());
		}
	}
	
}
