package test.gfp.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import gfp.dto.AgendamentoDto;
import gfp.dto.LancamentoDto;
import gfp.dto.SaldoDiarioDto;
import gfp.model.Banco;
import gfp.model.Categoria;
import gfp.model.Conta;
import gfp.model.Lancamento;
import gfp.model.Usuario;
import gfp.service.LancamentoService;
import gfp.type.CategoriaType;
import gfp.type.ContaType;
import gfp.type.FormaPagamentoType;
import gfp.type.FrequenciaAgendamentoType;
import gfp.type.LancamentoPeriodoType;
import gfp.type.LancamentoSituacaoType;

import java.util.Date;
import java.util.List;

import logus.commons.datetime.AbstractDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../config/applicationContext-gfp.xml" })
public class LancamentoServiceTest {
	
	private LancamentoService controller;
	
	private void testarAgendamentos(final List<Lancamento> lancamentos,
			final Lancamento lancamento, final Date[] vencimentos,
			final Date[] previsoes) throws Exception {
		final Integer quantidadeParcelas = vencimentos.length;
		assertEquals(quantidadeParcelas.intValue(), lancamentos.size());
		
		Integer parcela = 1;
		
		for (final Lancamento l : lancamentos) {
			assertNotNull(l.getId());
			assertEquals(lancamento.getUsuario(), l.getUsuario());
			assertEquals(lancamento.getCategoria(), l.getCategoria());
			assertEquals(lancamento.getConta(), l.getConta());
			assertEquals(vencimentos[parcela - 1], l.getDataVencimento());
			assertEquals(lancamento.getCategoria(), l.getCategoria());
			assertEquals(lancamento.getValorOriginal(), l.getValorOriginal());
			assertEquals(lancamento.getFormaPagamento(), l.getFormaPagamento());
			assertEquals(lancamento.getObservacao(), l.getObservacao());
			assertEquals(parcela, l.getParcelaNumero());
			assertEquals(quantidadeParcelas, l.getParcelaQuantidade());
			assertEquals(previsoes[parcela - 1], l.getDataPrevisaoPagamento());
			assertNull(l.getDataPagamento());
			assertEquals(0.0, l.getValorPago());
			parcela++;
		}
	}
	
	@Before
	public void setUp() throws Exception {
		this.controller = LancamentoService.getInstance();
	}
	
	@After
	public void tearDown() throws Exception {
		Usuario.dao.deleteAll();
		Banco.dao.deleteAll();
	}
	
	@Test
	public void testAgendarLancamentos() throws Exception {
		final Usuario u = new Usuario("nome", "login", "senha").save();
		final Categoria cr = new Categoria(u, "descricao",
				CategoriaType.RECEITA).save();
		final Banco b = new Banco("banco").save();
		final Conta c = new Conta(u, b, "conta").save();
		final Lancamento lancamento = new Lancamento(u, cr, 100.0,
				FormaPagamentoType.DINHEIRO);
		lancamento.setConta(c);
		
		final AgendamentoDto dto = new AgendamentoDto();
		dto.setLancamento(lancamento);
		dto.setFrequencia(FrequenciaAgendamentoType.MENSAL.ordinal());
		dto.setDia(10);
		dto.setDataInicio(AbstractDateTime.date(7, 3, 2011));
		dto.setDataFinal(AbstractDateTime.date(31, 12, 2011));
		
		final Date[] vencimentos = { AbstractDateTime.date(10, 3, 2011),
				AbstractDateTime.date(10, 4, 2011),
				AbstractDateTime.date(10, 5, 2011),
				AbstractDateTime.date(10, 6, 2011),
				AbstractDateTime.date(10, 7, 2011),
				AbstractDateTime.date(10, 8, 2011),
				AbstractDateTime.date(10, 9, 2011),
				AbstractDateTime.date(10, 10, 2011),
				AbstractDateTime.date(10, 11, 2011),
				AbstractDateTime.date(10, 12, 2011) };
		
		dto.setAnteciparFinaisSemana(true);
		final Date[] previsoesAntecipadas = {
				AbstractDateTime.date(10, 3, 2011),
				AbstractDateTime.date(8, 4, 2011),
				AbstractDateTime.date(10, 5, 2011),
				AbstractDateTime.date(10, 6, 2011),
				AbstractDateTime.date(8, 7, 2011),
				AbstractDateTime.date(10, 8, 2011),
				AbstractDateTime.date(9, 9, 2011),
				AbstractDateTime.date(10, 10, 2011),
				AbstractDateTime.date(10, 11, 2011),
				AbstractDateTime.date(9, 12, 2011) };
		testarAgendamentos(this.controller.agendarLancamentos(dto), lancamento,
				vencimentos, previsoesAntecipadas);
		
		dto.setAnteciparFinaisSemana(false);
		final Date[] previsoesAtrasadas = { AbstractDateTime.date(10, 3, 2011),
				AbstractDateTime.date(11, 4, 2011),
				AbstractDateTime.date(10, 5, 2011),
				AbstractDateTime.date(10, 6, 2011),
				AbstractDateTime.date(11, 7, 2011),
				AbstractDateTime.date(10, 8, 2011),
				AbstractDateTime.date(12, 9, 2011),
				AbstractDateTime.date(10, 10, 2011),
				AbstractDateTime.date(10, 11, 2011),
				AbstractDateTime.date(12, 12, 2011) };
		testarAgendamentos(this.controller.agendarLancamentos(dto), lancamento,
				vencimentos, previsoesAtrasadas);
		
		dto.setFrequencia(FrequenciaAgendamentoType.DIA_UTIL_MES.ordinal());
		dto.setDia(5);
		final Date[] vencimentosDiasUteis = {
				AbstractDateTime.date(7, 4, 2011),
				AbstractDateTime.date(6, 5, 2011),
				AbstractDateTime.date(7, 6, 2011),
				AbstractDateTime.date(7, 7, 2011),
				AbstractDateTime.date(5, 8, 2011),
				AbstractDateTime.date(7, 9, 2011),
				AbstractDateTime.date(7, 10, 2011),
				AbstractDateTime.date(7, 11, 2011),
				AbstractDateTime.date(7, 12, 2011) };
		testarAgendamentos(this.controller.agendarLancamentos(dto), lancamento,
				vencimentosDiasUteis, vencimentosDiasUteis);
	}
	
	@Test
	public void testListarLancamentos() throws Exception {
		final Usuario u = new Usuario("nome", "login", "senha").save();
		final Categoria cr = new Categoria(u, "descricao",
				CategoriaType.RECEITA).save();
		final Categoria cd = new Categoria(u, "descricao",
				CategoriaType.DESPESA).save();
		final Banco b = new Banco("nome").save();
		final Conta c = new Conta(u, b, "identificacao").save();
		new Lancamento(u, cr, 10.0, FormaPagamentoType.DINHEIRO).save();
		new Lancamento(u, cd, 2.0, FormaPagamentoType.DINHEIRO).save();
		
		final Lancamento l1 = new Lancamento(u, cr, 100.0,
				FormaPagamentoType.CARTAO);
		l1.setDataPagamento(l1.getDataVencimento());
		l1.setValorPago(l1.getValorOriginal());
		l1.setConta(c);
		l1.save();
		
		final Lancamento l2 = new Lancamento(u, cd, 10.0,
				FormaPagamentoType.CARTAO);
		l2.setDataVencimento(AbstractDateTime.addDay(1));
		l2.setConta(c);
		l2.save();
		
		final Lancamento l3 = new Lancamento(u, cd, 50.0,
				FormaPagamentoType.DINHEIRO);
		l3.setDataVencimento(AbstractDateTime.addDay(2));
		l3.save();
		
		final LancamentoDto dto = new LancamentoDto(u);
		List<Lancamento> result = this.controller.listarLancamentos(dto);
		assertEquals(3, result.size());
		
		dto.setCategoria(cr.getId());
		dto.setDataFinal(AbstractDateTime.addDay(2));
		result = this.controller.listarLancamentos(dto);
		assertEquals(2, result.size());
		
		dto.setCategoria(cd.getId());
		result = this.controller.listarLancamentos(dto);
		assertEquals(3, result.size());
		
		dto.setCategoria(0L);
		dto.setSituacao(LancamentoSituacaoType.PAGO.ordinal());
		result = this.controller.listarLancamentos(dto);
		assertEquals(1, result.size());
		
		dto.setCategoria(0L);
		dto.setSituacao(LancamentoSituacaoType.INDEFINIDA.ordinal());
		dto.setTipoPeriodo(LancamentoPeriodoType.PAGAMENTO.ordinal());
		result = this.controller.listarLancamentos(dto);
		assertEquals(1, result.size());
	}
	
	@Test
	public void testListarPrevisaoSaldoDiario() throws Exception {
		final Usuario u = new Usuario("nome", "login", "senha").save();
		final Categoria cr = new Categoria(u, "descricao",
				CategoriaType.RECEITA).save();
		final Categoria cd = new Categoria(u, "descricao",
				CategoriaType.DESPESA).save();
		final Banco b = new Banco("nome").save();
		final Conta c = new Conta(u, b, "identificacao").save();
		new Lancamento(u, cr, 10.0, FormaPagamentoType.DINHEIRO).save();
		new Lancamento(u, cd, 2.0, FormaPagamentoType.DINHEIRO).save();
		
		List<SaldoDiarioDto> result = this.controller
				.listarPrevisaoSaldoDiario(u.getId());
		assertEquals(31, result.size());
		
		SaldoDiarioDto dto = result.get(0);
		assertEquals(AbstractDateTime.today(), dto.dataCompensacao);
		
		assertEquals(0.0, dto.saldoInicial);
		assertEquals(10.0, dto.receitas);
		assertEquals(2.0, dto.despesas);
		assertEquals(8.0, dto.saldoAcumulado);
		assertEquals(8.0, dto.saldoFinal);
		
		dto = result.get(30);
		assertEquals(AbstractDateTime.addDay(30), dto.dataCompensacao);
		
		Lancamento l;
		
		Lancamento.dao.deleteAll();
		
		l = new Lancamento(u, cr, 200.0, FormaPagamentoType.CARTAO);
		l.setDataPagamento(AbstractDateTime.date(26, 10, 2010));
		l.setValorPago(l.getValorOriginal());
		l.setConta(c);
		l.save();
		
		l = new Lancamento(u, cr, 43.32, FormaPagamentoType.CHEQUE);
		l.setDataPagamento(AbstractDateTime.date(26, 10, 2010));
		l.setValorPago(l.getValorOriginal());
		l.setConta(c);
		l.save();
		
		l = new Lancamento(u, cd, 75.0, FormaPagamentoType.CARTAO);
		l.setDataPagamento(AbstractDateTime.date(26, 10, 2010));
		l.setValorPago(l.getValorOriginal());
		l.setConta(c);
		l.save();
		
		result = this.controller.listarPrevisaoSaldoDiario(u.getId(),
				AbstractDateTime.date(26, 10, 2010));
		
		dto = result.get(0);
		assertEquals(AbstractDateTime.date(26, 10, 2010), dto.dataCompensacao);
		assertEquals(0.0, dto.saldoInicial);
		assertEquals(200.0, dto.receitas);
		assertEquals(75.0, dto.despesas);
		assertEquals(125.0, dto.saldoFinal);
		
		dto = result.get(3);
		assertEquals(AbstractDateTime.date(29, 10, 2010), dto.dataCompensacao);
		assertEquals(125.0, dto.saldoInicial);
		assertEquals(43.32, dto.receitas);
		assertEquals(0.0, dto.despesas);
		assertEquals(168.32, dto.saldoFinal);
		
		Lancamento.dao.deleteAll();
		
		l = new Lancamento(u, cr, 100.0, FormaPagamentoType.CARTAO);
		l.setDataPagamento(l.getDataVencimento());
		l.setValorPago(l.getValorOriginal());
		l.setConta(c);
		l.save();
		
		l = new Lancamento(u, cd, 10.0, FormaPagamentoType.CARTAO);
		l.setConta(c);
		l.save();
		
		l = new Lancamento(u, cd, 50.0, FormaPagamentoType.DINHEIRO);
		l.setDataVencimento(AbstractDateTime.addDay(1));
		l.setDataPrevisaoPagamento(l.getDataVencimento());
		l.save();
		
		result = this.controller.listarPrevisaoSaldoDiario(u.getId());
		
		dto = result.get(0);
		assertEquals(0.0, dto.saldoInicial);
		assertEquals(100.0, dto.receitas);
		assertEquals(10.0, dto.despesas);
		assertEquals(90.0, dto.saldoFinal);
		
		dto = result.get(1);
		assertEquals(90.0, dto.saldoInicial);
		assertEquals(0.0, dto.receitas);
		assertEquals(50.0, dto.despesas);
		assertEquals(40.0, dto.saldoFinal);
	}
	
	@Test
	public void testTransferencia() throws Exception {
		final Usuario u = new Usuario("nome", "login", "senha").save();
		
		final Categoria cd = new Categoria(u, "categoria",
				CategoriaType.DESPESA);
		cd.setTransferencia(true);
		cd.save();
		
		final Banco b = new Banco("banco").save();
		
		final Conta corrente = new Conta(u, "conta db");
		corrente.setTipo(ContaType.CONTA_CORRENTE.ordinal());
		corrente.setBanco(b);
		corrente.save();
		
		final Conta carteira = new Conta(u, "conta cr");
		carteira.setTipo(ContaType.CARTEIRA.ordinal());
		carteira.save();
		
		Lancamento l = new Lancamento(u, cd, 10, FormaPagamentoType.CARTAO);
		l.setConta(corrente);
		l.setContaTransferencia(carteira);
		this.controller.salvarLancamento(l);
		
		l = Lancamento.dao.find(l.getId());
		assertNotNull(l);
		assertNotNull(l.getVinculados());
		assertEquals(1, l.getVinculados().size());
		assertEquals(corrente, l.getConta());
		assertEquals(true, l.getCategoria().isTransferencia());
		assertEquals(Categoria.obterTransferencia(u), l.getVinculados().get(0)
				.getCategoria());
		
// final Categoria template = new Categoria(u, "Transferência",
// CategoriaType.RECEITA);
// template.setEstatistica(false);
// template.setInterna(true);
// final Categoria ct = Categoria.dao.findFirstByTemplate(template);
//
		
// final Categoria ct = new Categoria()
// .first("usuario = ?1 and descricao = ?2 and tipo = ?3 and estatistica is false and transferencia is false and interna is true",
// u, "Transferência", CategoriaType.RECEITA.ordinal());
// assertNotNull(ct);
		
		Lancamento l2 = l.getVinculados().get(0);
		assertEquals(carteira, l2.getConta());
		assertEquals(FormaPagamentoType.DINHEIRO.ordinal(), l2
				.getFormaPagamento().intValue());
		assertEquals(l.getCategoria(), l2.getCategoria());
		assertEquals(l.getId() + 1, l2.getId().intValue());
		assertNull(l2.getContaTransferencia());
		
		l.setConta(carteira);
		l.setContaTransferencia(corrente);
		l.setFormaPagamento(FormaPagamentoType.DINHEIRO.ordinal());
		this.controller.salvarLancamento(l);
		l2 = l.getVinculados().get(0);
		assertEquals(corrente, l2.getConta());
		assertEquals(FormaPagamentoType.CARTAO.ordinal(), l2
				.getFormaPagamento().intValue());
		assertEquals(l.getCategoria(), l2.getCategoria());
		assertEquals(l.getId() + 1, l2.getId().intValue());
		assertNull(l2.getContaTransferencia());
		
		final Date d = AbstractDateTime.addDay(1);
		l.setDataCompensacao(d);
		l.setDataPagamento(d);
		l.setDataPrevisaoPagamento(d);
		l.setDataVencimento(d);
		l.setValorOriginal(1);
		l.setValorPago(1);
		this.controller.salvarLancamento(l);
		
		l2 = l.getVinculados().get(0);
		assertEquals(d, l2.getDataCompensacao());
		assertEquals(d, l2.getDataPagamento());
		assertEquals(d, l2.getDataPrevisaoPagamento());
		assertEquals(d, l2.getDataVencimento());
		assertEquals(1.0, l2.getValorOriginal());
		assertEquals(1.0, l2.getValorPago());
		
		Long idVinculado = l2.getId();
		l.setContaTransferencia(null);
		this.controller.salvarLancamento(l);
		assertEquals(0, l.getVinculados().size());
		assertNull(Lancamento.dao.find(idVinculado));
		
		l = new Lancamento(u, cd, 10, FormaPagamentoType.CARTAO);
		l.setContaTransferencia(carteira);
		this.controller.salvarLancamento(l);
		final Long id = l.getId();
		idVinculado = l.getVinculados().get(0).getId();
		assertNotNull(Lancamento.dao.find(idVinculado));
		this.controller.excluir(l);
		assertNull(Lancamento.dao.find(id));
		assertNull(Lancamento.dao.find(idVinculado));
	}
	
}
