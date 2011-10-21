package gfp.config;

import java.net.URI;

import commons.persistence.hibernate.AbstractHibernateDatabase;
import commons.persistence.hibernate.AbstractHibernateManager;

public class GfpHibernateManager extends AbstractHibernateManager {
	
	private static final String ID = "gfp";
	
	public GfpHibernateManager() throws Exception {
		super(ID, AbstractHibernateDatabase.findConfigFile(ID), new URI(
				"/gfp/model"));
	}
	
}
