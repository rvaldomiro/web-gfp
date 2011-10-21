package test.commons.config;

import java.net.URI;

import commons.persistence.hibernate.AbstractHibernateDatabase;
import commons.persistence.hibernate.AbstractHibernateManager;

public class LocalCommonHibernateManager extends AbstractHibernateManager {
	
	private static final String ID = "test-common";
	
	public LocalCommonHibernateManager() throws Exception {
		super(ID, AbstractHibernateDatabase.findConfigFile(ID), new URI(
				"/test/commons/persistence/model"));
	}
	
}
