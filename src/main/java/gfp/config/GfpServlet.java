package gfp.config;

import gfp.dto.SaldoDto;
import gfp.model.Categoria;
import gfp.model.Conta;
import gfp.model.Lancamento;
import gfp.model.Usuario;
import gfp.service.ApplicationService;
import gfp.service.LancamentoService;
import gfp.service.UsuarioService;
import gfp.type.CategoriaType;
import gfp.type.ContaType;
import gfp.type.FormaPagamentoType;

import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import commons.log.LogBuilder;
import commons.persistence.ApplicationContext;
import commons.util.DateUtils;
import commons.web.AbstractServlet;

public class GfpServlet extends AbstractServlet {
	
	private void atualizarRegistros(final String currentVersion)
			throws Exception {
		if ("01.01.00.00".equals(currentVersion)) {
			final UsuarioService usuarioService = new UsuarioService();
			final LancamentoService lancamentoService = new LancamentoService();
			
			for (final Categoria categoria : new Categoria()
					.where("transferencia is null")) {
				categoria.setTransferencia(false);
				categoria.save();
			}
			
			for (final Usuario usuario : new Usuario().all()) {
				usuarioService.salvarUsuario(usuario);
				
				final Conta conta = new Conta().first(
						"usuario = ?1 and tipo = ?2", usuario,
						ContaType.CARTEIRA.ordinal());
				
				Date dataVencimento = null;
				
				for (final Lancamento lancamento : new Lancamento()
						.where("usuario = ?1 and conta is null order by dataVencimento",
								usuario)) {
					if (dataVencimento == null) {
						dataVencimento = lancamento.getDataVencimento();
					}
					
					double valor = lancamento.getDataPagamento() == null ? lancamento
							.getValorOriginal() : lancamento.getValorPago();
					
					if (lancamento.getCategoria().getTipo() == CategoriaType.DESPESA
							.ordinal()) {
						valor = valor * -1;
					}
					
					lancamento.setConta(conta);
					lancamentoService.salvarLancamento(lancamento);
				}
				
				if (dataVencimento == null) {
					continue;
				}
				
				Categoria categoria = new Categoria().first(
						"usuario = ?1 and tipo = ?2 and descricao = ?3",
						usuario, CategoriaType.RECEITA.ordinal(),
						"Saldo Inicial");
				
				if (categoria == null) {
					categoria = new Categoria(usuario, "Saldo Inicial",
							CategoriaType.RECEITA).save();
				}
				
				for (final SaldoDto dto : lancamentoService
						.listarSaldoPorConta(usuario.getId())) {
					if (conta.equals(dto.conta) && "Total".equals(dto.situacao)) {
						final Lancamento lancamento = new Lancamento(usuario,
								categoria, dto.saldo * -1,
								FormaPagamentoType.DINHEIRO);
						lancamento.setDataVencimento(DateUtils.removeDay(
								dataVencimento, 1));
						lancamento.setDataPrevisaoPagamento(lancamento
								.getDataVencimento());
						lancamento.setDataPagamento(lancamento
								.getDataVencimento());
						lancamento.setValorPago(lancamento.getValorOriginal());
						lancamento.setConta(conta);
						lancamento.save();
					}
				}
			}
			
			for (final Categoria categoria : new Categoria()
					.where("interna is null")) {
				categoria.setInterna(false);
				categoria.save();
			}
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
			
			atualizarRegistros(currentVersion);
		} catch (final Exception e) {
			LogBuilder.error(e);
		}
	}
	
}
