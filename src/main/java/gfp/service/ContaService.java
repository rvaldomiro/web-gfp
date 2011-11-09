package gfp.service;

import gfp.model.Conta;

import java.util.List;

import logus.commons.persistence.hibernate.transaction.HibernateTransaction;
import logus.commons.persistence.hibernate.transaction.TransactionClass;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class ContaService extends TransactionClass<ContaService> {
	
	public static ContaService getInstance() throws Exception {
		return new ContaService().getEnhancerInstance();
	}
	
	protected ContaService() {
		super();
	}
	
	@RemotingInclude
	@HibernateTransaction
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
	@HibernateTransaction
	public void salvarConta(final Conta conta) throws Exception {
		conta.save();
	}
	
}
