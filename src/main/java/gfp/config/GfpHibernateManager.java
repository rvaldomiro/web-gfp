package gfp.config;

import java.net.URI;

import logus.commons.persistence.hibernate.AbstractHibernateManager;
import logus.commons.persistence.hibernate.HibernateDatabase;

public class GfpHibernateManager extends AbstractHibernateManager {
	
//	private static final String ID = "gfp";
	private static final String ID = "gfp-test";
	
	public GfpHibernateManager() throws Exception {
		super(ID, HibernateDatabase.findConfigurationFile(ID), new URI(
				"/gfp/model"));
	}
	
}
