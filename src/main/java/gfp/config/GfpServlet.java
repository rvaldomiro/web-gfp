package gfp.config;

import gfp.service.ApplicationService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import commons.log.LogBuilder;
import commons.persistence.ApplicationContext;
import commons.web.AbstractServlet;

public class GfpServlet extends AbstractServlet {
	
	@Override
	public void init(final ServletConfig arg0) throws ServletException {
		try {
			final String currentVersion = new ApplicationService()
					.obterVersaoAtual();
			final boolean productionMode = ApplicationContext.get()
					.getConnectionProperties()
					.getProperty("hibernate.connection.url").indexOf("prd") > 0;
			
			LogBuilder.configure("gfp", currentVersion, !productionMode);
		} catch (final Exception e) {
			LogBuilder.error(e);
		}
	}
	
}
