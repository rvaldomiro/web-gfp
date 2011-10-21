package gfp.service;

import gfp.model.Categoria;
import gfp.model.Conta;
import gfp.model.Usuario;
import gfp.type.ContaType;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class UsuarioService {
	
	@RemotingInclude
	public Usuario login(final String login, final String senha)
			throws Exception {
		final Usuario result = new Usuario().first("login = ?1 and senha = ?2",
				login, String.valueOf(senha.hashCode()));
		
		if (result == null) {
			throw new Exception("Usuário e/ou Senha inválidos!");
		}
		
		if (!result.isAtivo()) {
			throw new Exception("Usuário desativado!");
		}
		
		return result;
	}
	
	@RemotingInclude
	public void salvarUsuario(final Usuario usuario) throws Exception {
		if (usuario.getId() == null) {
			if (usuario.where("login = ?1", usuario.getLogin()).size() > 0) {
				throw new Exception(
						"Nome de usuário não disponível para cadastro!");
			}
			
			if (usuario.getEmail() != null && !usuario.getEmail().isEmpty() &&
					usuario.where("email = ?1", usuario.getEmail()).size() > 0) {
				throw new Exception("E-Mail já está em uso!");
			}
			
			new Categoria().criarPadroes(usuario.save());
		}
		
		if (new Conta().first("usuario = ?1 and tipo = ?2", usuario,
				ContaType.CARTEIRA.ordinal()) == null) {
			new Conta().criarPadrao(usuario);
		}
	}
	
}
