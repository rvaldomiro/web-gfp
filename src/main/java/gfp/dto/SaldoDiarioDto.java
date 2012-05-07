package gfp.dto;

import gfp.model.Lancamento;
import gfp.service.LancamentoService;
import gfp.type.CategoriaType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logus.commons.datetime.AbstractDateTime;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class SaldoDiarioDto implements Comparable<SaldoDiarioDto> {
	
	public static List<SaldoDiarioDto> calcular(final Long usuarioId,
			final double saldoAnterior, final List<SaldoDiarioDto> result,
			final int modo) throws Exception {
		double saldoInicialDia = saldoAnterior;
		boolean hoje = true;
		
		for (final SaldoDiarioDto o : result) {
			final Date dataCompensacaoFinal = modo == 1 ? o.dataCompensacao
					: AbstractDateTime.getLastDayOfMonth(o.dataCompensacao);
			
			if (hoje) {
				saldoInicialDia = saldoAnterior;
				
				Criteria c = Lancamento.dao.createCriteria();
				c.createAlias("categoria", "_categoria");
				
				ProjectionList p = Projections.projectionList();
				p.add(Projections.sum("valorOriginal"));
				c.add(Restrictions.eq("usuario.id", usuarioId));
				c.add(Restrictions.between("dataCompensacao",
						o.dataCompensacao, AbstractDateTime.setTime(
								dataCompensacaoFinal, "23:59:59")));
				c.add(Restrictions.eq("_categoria.tipo",
						CategoriaType.RECEITA.ordinal()));
				c.add(Restrictions.eq("_categoria.estatistica", true));
				c.setProjection(p);
				
				final Double totalReceitas = (Double) c.uniqueResult();
				
				o.setReceitas(totalReceitas);
				
				c = Lancamento.dao.createCriteria();
				c.createAlias("categoria", "_categoria");
				
				p = Projections.projectionList();
				p.add(Projections.sum("valorOriginal"));
				c.add(Restrictions.eq("usuario.id", usuarioId));
				c.add(Restrictions.between("dataCompensacao",
						o.dataCompensacao, AbstractDateTime.setTime(
								dataCompensacaoFinal, "23:59:59")));
				c.add(Restrictions.eq("_categoria.tipo",
						CategoriaType.DESPESA.ordinal()));
				c.add(Restrictions.eq("_categoria.estatistica", true));
				c.setProjection(p);
				
				final Double totalDespesas = (Double) c.uniqueResult();
				
				o.setDespesas(totalDespesas);
				hoje = false;
			}
			
			o.saldoInicial = saldoInicialDia;
			o.saldoFinal = o.saldoInicial + o.receitas - o.despesas;
			o.saldoAcumulado += o.saldoFinal;
			
			saldoInicialDia = o.saldoAcumulado;
		}
		
		return result;
	}
	
	public static List<SaldoDiarioDto> getInstance(final Date dataInicio,
			final Date dataFinal, final int modo) {
		final List<SaldoDiarioDto> result = new ArrayList<SaldoDiarioDto>();
		
		for (Date data = dataInicio; !data.after(dataFinal); data = modo == LancamentoService.MODO_PREVISAO_DIARIA ? AbstractDateTime
				.addDay(data, 1) : AbstractDateTime.addMonth(data, 1)) {
			result.add(new SaldoDiarioDto(data));
		}
		
		return result;
	}
	
	public Date dataCompensacao;
	
	public double saldoInicial;
	
	public double receitas;
	
	public double despesas;
	
	public double saldoFinal;
	
	public double saldoAcumulado;
	
	public SaldoDiarioDto(final Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}
	
	private void setDespesas(final Double arg0) {
		this.despesas = arg0 == null ? 0.0 : arg0;
	}
	
	private void setReceitas(final Double arg0) {
		this.receitas = arg0 == null ? 0.0 : arg0;
	}
	
	@Override
	public int compareTo(final SaldoDiarioDto o) {
		return this.dataCompensacao.compareTo(o.dataCompensacao);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SaldoDiarioDto other = (SaldoDiarioDto) obj;
		if (this.dataCompensacao == null) {
			if (other.dataCompensacao != null) {
				return false;
			}
		} else if (!this.dataCompensacao.equals(other.dataCompensacao)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime *
				result +
				(this.dataCompensacao == null ? 0 : this.dataCompensacao
						.hashCode());
		return result;
	}
	
}
