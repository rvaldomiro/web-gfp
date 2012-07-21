import gfp.model.Banco;
import gfp.model.Lancamento;
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
			new Banco("Itaú").save();
			new Banco("Bradesco").save();
			new Banco("C.E.F.").save();
			new Banco("B.B.").save();
			new Banco("HSBC").save();
			new Banco("Safra").save();
			new Banco("Santander").save();
		}
		
		for (final Lancamento lancamento : Lancamento.dao.findAllByField(
				"dataPagamento", null)) {
			lancamento.save();
		}
	}
	
	@Override
	public void init(final ServletConfig arg0) throws ServletException {
		try {
			LogBuilder.configure(arg0, getCurrentVersion(), getParameters());
			GfpHibernateManagerFactory.getInstance();
			setup();
		} catch (final Exception e) {
			LogBuilder.error(e);
		}
	}
	
}
