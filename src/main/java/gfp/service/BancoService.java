package gfp.service;

import gfp.model.Banco;

import java.util.List;

import logus.commons.persistence.hibernate.transaction.TransactionClass;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class BancoService extends TransactionClass<BancoService> {
	
	public static BancoService getInstance() throws Exception {
		return new BancoService().getEnhancerInstance();
	}
	
	protected BancoService() {
		super();
	}
	
	@RemotingInclude
	public List<Banco> listarBancos() throws Exception {
		return Banco.listar();
	}
	
}
