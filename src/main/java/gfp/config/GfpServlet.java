package gfp.config;

import gfp.model.Banco;
import gfp.service.ApplicationService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import commons.log.LogBuilder;
import commons.persistence.ApplicationContext;
import commons.web.AbstractServlet;

public class GfpServlet extends AbstractServlet {
	
	private void setup() throws Exception {
		if (Banco.listar().size() == 0) {
			new Banco("Real").save();
			new Banco("Itaú").save();
			new Banco("Bradesco").save();
			new Banco("C.E.F.").save();
			new Banco("B.B.").save();
			new Banco("HSBC").save();
			new Banco("Safra").save();
			new Banco("Santander").save();
			new Banco("Unibanco").save();
		}
	}
	
	@Override
	public void init(final ServletConfig arg0) throws ServletException {
		try {
			final String currentVersion = new ApplicationService()
					.obterVersaoAtual();
			final boolean productionMode = ApplicationContext.get()
					.getConnectionProperties()
					.getProperty("hibernate.connection.url").indexOf("prd") > 0;
			
			LogBuilder.configure("gfp", currentVersion, !productionMode);
			
			setup();
		} catch (final Exception e) {
			LogBuilder.error(e);
		}
	}
	
}
