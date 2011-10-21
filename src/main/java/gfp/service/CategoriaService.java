package gfp.service;

import gfp.model.Categoria;

import java.util.List;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class CategoriaService {
	
	@RemotingInclude
	public void excluir(final Categoria categoria) throws Exception {
		categoria.delete();
	}
	
	@RemotingInclude
	public List<Categoria> listarCategorias(final Long usuarioId)
			throws Exception {
		return Categoria.listar(usuarioId);
	}
	
	@RemotingInclude
	public Categoria salvarCategoria(final Categoria categoria)
			throws Exception {
		return categoria.save();
	}
	
}
