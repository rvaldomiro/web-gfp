package test.gfp.config;

import java.net.URI;

import logus.commons.persistence.hibernate.AbstractHibernateManager;
import logus.commons.persistence.hibernate.HibernateDatabase;

public class GfpHibernateManagerFactory extends AbstractHibernateManager {
	
	private static final String ID = "gfp-test";
	
	public GfpHibernateManagerFactory() throws Exception {
		super(ID, HibernateDatabase.findConfigurationFile(ID), new URI(
				"/gfp/model"));
	}
	
}
