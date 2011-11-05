package gfp.service;

import gfp.model.Categoria;

import java.util.List;

import logus.commons.persistence.hibernate.transaction.HibernateTransaction;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class CategoriaService {
	
	@RemotingInclude
	@HibernateTransaction
	public void excluir(final Categoria categoria) throws Exception {
		categoria.delete();
	}
	
	@RemotingInclude
	public List<Categoria> listarCategorias(final Long usuarioId)
			throws Exception {
		return Categoria.listar(usuarioId);
	}
	
	@RemotingInclude
	@HibernateTransaction
	public Categoria salvarCategoria(final Categoria categoria)
			throws Exception {
		return categoria.save();
	}
	
}
