package commons.persistence.hibernate.transaction;

import org.springframework.beans.factory.annotation.Autowired;

import commons.persistence.hibernate.HibernateManager;

public class TransactionSupport {
	
	private HibernateManager manager = null;
	
	protected void beginTransaction() {
		this.manager.beginTransaction();
	}
	
	protected void commitTransaction() {
		this.manager.commitTransaction();
	}
	
	protected void rollbackTransaction() {
		this.manager.rollbackTransaction();
	}
	
	@Autowired
	public void setManager(final HibernateManager manager) {
		this.manager = manager;
	}
	
}
