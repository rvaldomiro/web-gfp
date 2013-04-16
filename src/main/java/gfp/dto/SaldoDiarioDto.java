package gfp.dto;

import gfp.model.Lancamento;
import gfp.service.LancamentoService;
import gfp.type.CategoriaType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logus.commons.util.DateUtil;

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
					: DateUtil.lastDayOfMonth(o.dataCompensacao);
			
			if (hoje) {
				saldoInicialDia = saldoAnterior;
				
				Criteria c = Lancamento.dao.createCriteria();
				c.createAlias("categoria", "_categoria");
				
				ProjectionList p = Projections.projectionList();
				p.add(Projections.sum("valorOriginal"));
				c.add(Restrictions.eq("usuario.id", usuarioId));
				c.add(Restrictions.between("dataCompensacao",
						o.dataCompensacao,
						DateUtil.time(dataCompensacaoFinal, "23:59:59")));
				c.add(Restrictions.eq("_categoria.tipo",
						CategoriaType.RECEITA.ordinal()));
				c.add(Restrictions.eq("_categoria.estatistica", true));
				c.setProjection(p);
				
				final Double totalReceitas = (Double) c.uniqueResult();
				
				if (totalReceitas != null && totalReceitas > o.receitas) {
					o.setReceitas(totalReceitas);
				}
				
				c = Lancamento.dao.createCriteria();
				c.createAlias("categoria", "_categoria");
				
				p = Projections.projectionList();
				p.add(Projections.sum("valorOriginal"));
				c.add(Restrictions.eq("usuario.id", usuarioId));
				c.add(Restrictions.between("dataCompensacao",
						o.dataCompensacao,
						DateUtil.time(dataCompensacaoFinal, "23:59:59")));
				c.add(Restrictions.eq("_categoria.tipo",
						CategoriaType.DESPESA.ordinal()));
				c.add(Restrictions.eq("_categoria.estatistica", true));
				c.setProjection(p);
				
				final Double totalDespesas = (Double) c.uniqueResult();
				
				if (totalDespesas != null && totalDespesas > o.despesas) {
					o.setDespesas(totalDespesas);
				}
				
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
		
		for (Date data = dataInicio; !data.after(DateUtil.time(dataFinal,
				"01:00:00")); data = modo == LancamentoService.MODO_PREVISAO_DIARIA ? DateUtil
				.add(data, 1) : DateUtil.addMonth(data, 1)) {
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
	
	public String dataCompensacaoString;
	
	public SaldoDiarioDto(final Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
		this.dataCompensacaoString = DateUtil
				.toStringDateFormat(dataCompensacao);
	}
	
	private void setDespesas(final Double arg0) {
		this.despesas = arg0 == null ? 0.0 : arg0;
	}
	
	private void setReceitas(final Double arg0) {
		this.receitas = arg0 == null ? 0.0 : arg0;
	}
	
	@Override
	public int compareTo(final SaldoDiarioDto o) {
		return this.dataCompensacaoString.compareTo(o.dataCompensacaoString);
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
		if (this.dataCompensacaoString == null) {
			if (other.dataCompensacaoString != null) {
				return false;
			}
		} else if (!this.dataCompensacaoString
				.equals(other.dataCompensacaoString)) {
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
				(this.dataCompensacaoString == null ? 0
						: this.dataCompensacaoString.hashCode());
		return result;
	}
	
}
