package gfp.config;
import gfp.model.Banco;
import gfp.model.Usuario;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import logus.commons.log.LogBuilder;
import logus.commons.web.AbstractServlet;

public class GfpServlet extends AbstractServlet {
	
	private void setup() throws Exception {
		if (Usuario.dao.findAll().size() == 0) {
			new Usuario("Administrador", "admin", "admin", true).save();
		}
		
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
			LogBuilder.configure(arg0, getCurrentVersion(), getParameters());
			setup();
		} catch (final Exception e) {
			LogBuilder.error(e);
		}
	}
	
}
