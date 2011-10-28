package test.gfp.config;

import java.net.URI;

import commons.persistence.hibernate.AbstractHibernateDatabase;
import commons.persistence.hibernate.AbstractHibernateManager;

public class LocalGfpHibernateManager extends AbstractHibernateManager {
	
	private static final String ID = "gfp-test";
	
	public LocalGfpHibernateManager() throws Exception {
		super(ID, AbstractHibernateDatabase.findConfigFile(ID), new URI(
				"/gfp/model"));
	}
	
}
