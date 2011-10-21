package commons.persistence.hibernate;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface HibernateManager {
	
	Transaction beginTransaction();
	
	boolean closeCurrentSession();
	
	boolean closeSessionFactory();
	
	void commitTransaction();
	
	void commitTransaction(Transaction tx);
	
	Session currentSession();
	
	Properties getConnectionProperties();
	
	String getContextId();
	
	boolean inTransaction();
	
	boolean isFactoryClosed();
	
	void rollbackTransaction();
	
	void rollbackTransaction(Transaction tx);
	
}
