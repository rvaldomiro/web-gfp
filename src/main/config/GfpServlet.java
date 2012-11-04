import gfp.model.Banco;
import gfp.model.Conta;
import gfp.model.Usuario;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import logus.commons.log.LogBuilder;
import logus.commons.web.AbstractServlet;

public class GfpServlet extends AbstractServlet {
	
	private void setup() throws Exception {
		if (Usuario.dao.all().size() == 0) {
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
		
		for (final Conta conta : Conta.dao.all()) {
			if (conta.getBanco() == null) {
				continue;
			}
			
			if ("Real".equals(conta.getBanco().getNome())) {
				conta.setBanco(Banco.dao.findByFields("nome", "Santander"));
				conta.save();
			} else if ("Unibanco".equals(conta.getBanco().getNome())) {
				conta.setBanco(Banco.dao.findByFields("nome", "Itaú"));
				conta.save();
			}
		}
		
		final Banco bancoReal = Banco.dao.findByFields("nome", "Real");
		
		if (bancoReal != null) {
			bancoReal.delete();
		}
		
		final Banco bancoUnibanco = Banco.dao.findByFields("nome", "Unibanco");
		
		if (bancoUnibanco != null) {
			bancoUnibanco.delete();
		}
		
// for (final Lancamento lancamento : Lancamento.dao.allByFields(
// "dataPagamento", null)) {
// lancamento.save();
// }
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
