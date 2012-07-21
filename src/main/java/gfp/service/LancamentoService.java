package gfp.service;

import gfp.dto.AgendamentoDto;
import gfp.dto.LancamentoDto;
import gfp.dto.SaldoCategoriaDto;
import gfp.dto.SaldoDiarioDto;
import gfp.dto.SaldoDto;
import gfp.model.Conta;
import gfp.model.Lancamento;
import gfp.type.CategoriaType;
import gfp.type.FrequenciaAgendamentoType;
import gfp.type.LancamentoPeriodoType;
import gfp.type.LancamentoSituacaoType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logus.commons.datetime.AbstractDateTime;
import logus.commons.number.Number;
import logus.commons.persistence.hibernate.transaction.HibernateTransaction;
import logus.commons.persistence.hibernate.transaction.TransactionClass;
import logus.commons.string.StringUtil;

import org.hibernate.Query;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class LancamentoService extends TransactionClass<LancamentoService> {
	
	public static final int MODO_PREVISAO_DIARIA = 1;
	
	public static final int MODO_PREVISAO_MENSAL = 2;
	
	public static LancamentoService getInstance() throws Exception {
		return new LancamentoService().getEnhancerInstance();
	}
	
	protected LancamentoService() {
		super();
	}
	
	@RemotingInclude
	@HibernateTransaction
	public List<Lancamento> agendarLancamentos(final AgendamentoDto dto)
			throws Exception {
		final List<Lancamento> result = new ArrayList<Lancamento>();
		
		final Date dataInicio = AbstractDateTime.parseBRST(dto.getDataInicio());
		final Date dataFinal = AbstractDateTime.parseBRST(dto.getDataFinal());
		Date dataVencimento = dataInicio;
		
		if (dto.getFrequencia() == FrequenciaAgendamentoType.MENSAL.ordinal()) {
			dataVencimento = AbstractDateTime.date(dto.getDia(),
					AbstractDateTime.month(dataVencimento),
					AbstractDateTime.year(dataVencimento));
			
			if (dataVencimento.compareTo(dataInicio) < 0) {
				dataVencimento = AbstractDateTime.addMonth(dataVencimento, 1);
			}
		} else if (dto.getFrequencia() == FrequenciaAgendamentoType.DIA_UTIL_MES
				.ordinal()) {
			dataVencimento = AbstractDateTime.usefulDayOfMonth(dataVencimento,
					dto.getDia());
			
			if (dataVencimento.compareTo(dataInicio) < 0) {
				dataVencimento = AbstractDateTime.usefulDayOfMonth(
						AbstractDateTime.addMonth(dataVencimento, 1),
						dto.getDia());
			}
		}
		
		int parcela = 1;
		
		while (dataVencimento.compareTo(dataFinal) <= 0) {
			Date dataPrevisaoPagamento = dataVencimento;
			
			final int diaDaSemana = AbstractDateTime.dayOfWeek(dataVencimento);
			
			if (diaDaSemana == AbstractDateTime.DOMINGO) {
				if (dto.isAnteciparFinaisSemana()) {
					dataPrevisaoPagamento = AbstractDateTime.removeDays(
							dataVencimento, 2);
				} else {
					dataPrevisaoPagamento = AbstractDateTime.addDay(
							dataVencimento, 1);
				}
			} else if (diaDaSemana == AbstractDateTime.SABADO) {
				if (dto.isAnteciparFinaisSemana()) {
					dataPrevisaoPagamento = AbstractDateTime.removeDays(
							dataVencimento, 1);
				} else {
					dataPrevisaoPagamento = AbstractDateTime.addDay(
							dataVencimento, 2);
				}
			}
			
			final Lancamento l = new Lancamento();
			l.setUsuario(dto.getLancamento().getUsuario());
			l.setDataVencimento(dataVencimento);
			l.setCategoria(dto.getLancamento().getCategoria());
			l.setConta(dto.getLancamento().getConta());
			l.setValorOriginal(dto.getLancamento().getValorOriginal());
			l.setFormaPagamento(dto.getLancamento().getFormaPagamento());
			l.setObservacao(dto.getLancamento().getObservacao());
			l.setParcelaNumero(parcela);
			l.setDataPrevisaoPagamento(dataPrevisaoPagamento);
			l.setDataPagamento(null);
			l.setValorPago(0);
			
			result.add(l);
			
			if (dto.getFrequencia() == FrequenciaAgendamentoType.MENSAL
					.ordinal()) {
				dataVencimento = AbstractDateTime.addMonth(dataVencimento, 1);
			} else if (dto.getFrequencia() == FrequenciaAgendamentoType.DIA_UTIL_MES
					.ordinal()) {
				dataVencimento = AbstractDateTime.usefulDayOfMonth(
						AbstractDateTime.addMonth(dataVencimento, 1),
						dto.getDia());
			}
			
			parcela++;
		}
		
		for (final Lancamento l : result) {
			l.setParcelaQuantidade(result.size());
			l.save();
		}
		
		return result;
	}
	
	@RemotingInclude
	@HibernateTransaction
	public void excluir(final Lancamento lancamento) throws Exception {
		lancamento.delete();
	}
	
	@RemotingInclude
	public List<Lancamento> listarDespesasVencer(final Long usuarioId)
			throws Exception {
		return Lancamento.listarAVencer(usuarioId,
				AbstractDateTime.daysAhead(15), CategoriaType.DESPESA);
	}
	
	@SuppressWarnings("unchecked")
	@RemotingInclude
	public List<Lancamento> listarLancamentos(final LancamentoDto dto)
			throws Exception {
		final List<String> cdt = new ArrayList<String>();
		final List<Object> prm = new ArrayList<Object>();
		
		cdt.add("usuario.id = :params1");
		prm.add(dto.getIdUsuario());
		
		if (dto.getTipoPeriodo() == LancamentoPeriodoType.VENCIMENTO.ordinal()) {
			cdt.add("dataVencimento between :params2 and :params3");
		} else if (dto.getTipoPeriodo() == LancamentoPeriodoType.PREVISAO_PAGAMENTO
				.ordinal()) {
			cdt.add("dataPrevisaoPagamento between :params2 and :params3");
		} else if (dto.getTipoPeriodo() == LancamentoPeriodoType.PAGAMENTO
				.ordinal()) {
			cdt.add("dataPagamento between :params2 and :params3");
		} else if (dto.getTipoPeriodo() == LancamentoPeriodoType.COMPENSACAO
				.ordinal()) {
			cdt.add("dataCompensacao between :params2 and :params3");
		}
		
		prm.add(AbstractDateTime.parseBRST(dto.getDataInicio()));
		prm.add(AbstractDateTime.parseBRST(dto.getDataFinal()));
		
		if (dto.getSituacao() == LancamentoSituacaoType.EM_ABERTO.ordinal()) {
			cdt.add("dataPagamento is null");
		} else if (dto.getSituacao() == LancamentoSituacaoType.PAGO.ordinal()) {
			cdt.add("dataPagamento is not null");
		}
		
		if (dto.getCategoria() != null && dto.getCategoria() > 0) {
			cdt.add("categoria.id = :params" + (prm.size() + 1));
			prm.add(dto.getCategoria());
		}
		
		if (dto.getConta() != null) {
			cdt.add("conta = :params" + (prm.size() + 1));
			prm.add(dto.getConta());
		}
		
		if (dto.getObservacao() != null && dto.getObservacao().length() > 0) {
			cdt.add("upper(observacao) like :params" + (prm.size() + 1));
			prm.add("%" + dto.getObservacao().toUpperCase() + "%");
		}
		
		final Query q = Lancamento.dao.createQuery("from Lancamento where " +
				StringUtil.join(cdt, " and "));
		
		for (int i = 1; i <= prm.size(); i++) {
			q.setParameter("params" + i, prm.get(i - 1));
		}
		
		return q.list();
	}
	
	@RemotingInclude
	public List<SaldoDiarioDto> listarPrevisaoSaldoDiario(final Long usuarioId,
			Date dataInicio, final int modo) throws Exception {
		final boolean modoDiario = modo == MODO_PREVISAO_DIARIA;
		
		dataInicio = modoDiario ? AbstractDateTime.parseBRST(dataInicio)
				: AbstractDateTime.getFirstDayOfMonth(AbstractDateTime
						.parseBRST(dataInicio));
		final Date dataFinal = modoDiario ? AbstractDateTime.addDay(dataInicio,
				45) : AbstractDateTime.getLastDayOfMonth(AbstractDateTime
				.addYear(dataInicio, 1));
		final List<SaldoDiarioDto> result = SaldoDiarioDto.getInstance(
				dataInicio, dataFinal, modo);
		final CategoriaType[] categorias = new CategoriaType[] {
				CategoriaType.RECEITA, CategoriaType.DESPESA };
		
		for (final CategoriaType categoria : categorias) {
			final List<Object[]> saldoDiario = modoDiario ? Lancamento
					.listarPrevisaoSaldoDiario(usuarioId, categoria,
							dataInicio, dataFinal) : Lancamento
					.listarPrevisaoSaldoMensal(usuarioId, categoria,
							dataInicio, dataFinal);
			
			for (final Object[] o : saldoDiario) {
				final Date dataCompensacao = AbstractDateTime.time(new Date(
						((Timestamp) o[0]).getTime()), "00:00:00");
				final Double saldo = (Double) o[1];
				final SaldoDiarioDto dto = result.get(result
						.indexOf(new SaldoDiarioDto(dataCompensacao)));
				
				if (categoria == CategoriaType.RECEITA) {
					dto.receitas = saldo;
				} else {
					dto.despesas = saldo;
				}
			}
		}
		
		final List<SaldoDto> saldoAnterior = listarSaldoPorConta(usuarioId,
				AbstractDateTime.removeDays(dataInicio, 1));
		
		return SaldoDiarioDto
				.calcular(usuarioId,
						saldoAnterior.get(saldoAnterior.size() - 1).saldo,
						result, modo);
	}
	
	@RemotingInclude
	public List<SaldoDiarioDto> listarPrevisaoSaldoDiario(final Long usuarioId,
			final int modo) throws Exception {
		return listarPrevisaoSaldoDiario(usuarioId, AbstractDateTime.today(),
				modo);
	}
	
	@RemotingInclude
	public List<Lancamento> listarReceitasVencer(final Long usuarioId)
			throws Exception {
		return Lancamento.listarAVencer(usuarioId,
				AbstractDateTime.daysAhead(15), CategoriaType.RECEITA);
	}
	
	@RemotingInclude
	public List<SaldoCategoriaDto> listarSaldoCategoriaMensal(
			final Long usuarioId, final Integer mes, final Integer ano,
			final Integer tipoCategoria) throws Exception {
		final Date dataInicio = AbstractDateTime.firstDayOfMonth(mes, ano);
		final Date dataFinal = AbstractDateTime.lastDayOfMonth(mes, ano);
		
		return Lancamento.listarSaldoCategoriaMensal(usuarioId, tipoCategoria,
				dataInicio, dataFinal);
	}
	
	@RemotingInclude
	public List<SaldoDto> listarSaldoPorConta(final Long usuarioId)
			throws Exception {
		return listarSaldoPorConta(usuarioId, AbstractDateTime.today());
	}
	
	@RemotingInclude
	public List<SaldoDto> listarSaldoPorConta(final Long usuarioId,
			Date dataSaldo) throws Exception {
		dataSaldo = AbstractDateTime.parseBRST(dataSaldo);
		final List<SaldoDto> result = new ArrayList<SaldoDto>();
		final List<Conta> contas = Conta.listarAtivas(usuarioId);
		
		double saldoAtual = 0.0;
		
		for (final Conta conta : contas) {
			final double receitaConciliada = Lancamento.obterSaldoDisponivel(
					conta, CategoriaType.RECEITA, dataSaldo);
			final double despesaConciliada = Lancamento.obterSaldoDisponivel(
					conta, CategoriaType.DESPESA, dataSaldo);
			final double saldoBloqueado = Lancamento.obterSaldoBloqueado(conta,
					CategoriaType.RECEITA, dataSaldo);
			final double saldoDisponivel = Number.round(receitaConciliada -
					despesaConciliada, 2);
			final double saldoTotalConta = Number.round(receitaConciliada +
					saldoBloqueado - despesaConciliada, 2);
			
			saldoAtual += saldoDisponivel;
			saldoAtual = Number.round(saldoAtual, 2);
			
			result.add(new SaldoDto(conta, SaldoDto.SALDO_DISPONIVEL,
					saldoDisponivel));
			
			if (saldoBloqueado > 0) {
				result.add(new SaldoDto(conta, SaldoDto.SALDO_BLOQUEADO,
						saldoBloqueado));
			}
			
			result.add(new SaldoDto(conta, SaldoDto.SALDO_TOTAL_CONTA,
					saldoTotalConta));
		}
		
		result.add(new SaldoDto(SaldoDto.SALDO_ATUAL, saldoAtual));
		
		return result;
	}
	
	@RemotingInclude
	@HibernateTransaction
	public void salvarLancamento(final Lancamento lancamento) throws Exception {
		lancamento.save();
	}
	
}
