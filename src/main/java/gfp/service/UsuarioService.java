package gfp.service;

import gfp.model.Usuario;
import logus.commons.persistence.hibernate.transaction.HibernateTransaction;
import logus.commons.persistence.hibernate.transaction.TransactionClass;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class UsuarioService extends TransactionClass<UsuarioService> {
	
	public static UsuarioService getInstance() throws Exception {
		return new UsuarioService().getEnhancerInstance();
	}
	
	protected UsuarioService() {
		super();
	}
	
	@RemotingInclude
	public Usuario login(final String login, final String senha)
			throws Exception {
		final Usuario result = Usuario.dao.findByFields("login", login,
				"senha", String.valueOf(senha.hashCode()));
// final Usuario result = new Usuario().first("login = ?1 and senha = ?2",
// login, String.valueOf(senha.hashCode()));
		
		if (result == null) {
			throw new Exception("Usuário e/ou Senha inválidos!");
		}
		
		if (!result.isAtivo()) {
			throw new Exception("Usuário desativado!");
		}
		
		return result;
	}
	
	@RemotingInclude
	@HibernateTransaction
	public void salvarUsuario(final Usuario usuario) throws Exception {
		if (usuario.getId() == null) {
// if (usuario.where("login = ?1", usuario.getLogin()).size() > 0) {
			if (Usuario.dao.findByField("login", usuario.getLogin()) != null) {
				throw new Exception(
						"Nome de usuário não disponível para cadastro!");
			}
			
			if (usuario.getEmail() != null &&
					!usuario.getEmail().isEmpty() &&
// usuario.where("email = ?1", usuario.getEmail()).size() > 0) {
					Usuario.dao.findByField("email", usuario.getEmail()) != null) {
				throw new Exception("E-Mail já está em uso!");
			}
			
			usuario.save();
// Categoria.criarPadroes(usuario.save());
		}
		
// Conta.obterCarteira(usuario);
// if (new Conta().first("usuario = ?1 and tipo = ?2", usuario,
// Conta template=new Conta(usuario, ContaType.CARTEIRA);
		
// if (Conta.dao.findFirstByTemplate(template) == null) {
// template.save();
// new Conta().criarPadrao(usuario);
// }
	}
	
}
