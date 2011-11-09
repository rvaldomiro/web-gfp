package gfp.service;

import logus.commons.persistence.ApplicationContext;
import logus.commons.persistence.hibernate.transaction.TransactionClass;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class ApplicationService extends TransactionClass<ApplicationService> {
	
	public static ApplicationService getInstance() throws Exception {
		return new ApplicationService().getEnhancerInstance();
	}
	
	protected ApplicationService() {
		super();
	}
	
	@RemotingInclude
	public String obterVersaoAtual() throws Exception {
		return ApplicationContext.getCurrentVersion();
	}
	
}
