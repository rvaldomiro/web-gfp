package gfp.service;

import gfp.model.Conta;

import java.util.List;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class ContaService {
	
	@RemotingInclude
	public void excluir(final Conta conta) throws Exception {
		conta.delete();
	}
	
	@RemotingInclude
	public List<Conta> listarContas(final Long usuarioId) throws Exception {
		return Conta.listar(usuarioId);
	}
	
	@RemotingInclude
	public List<Conta> listarContasAtivas(final Long usuarioId)
			throws Exception {
		return Conta.listarAtivas(usuarioId);
	}
	
	@RemotingInclude
	public void salvarConta(final Conta conta) throws Exception {
		conta.save();
	}
	
}
