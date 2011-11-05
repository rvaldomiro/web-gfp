package gfp.dto;

import gfp.model.Lancamento;
import gfp.type.CategoriaType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import logus.commons.datetime.AbstractDateTime;

public class SaldoDiarioDto implements Comparable<SaldoDiarioDto> {
	
	public static List<SaldoDiarioDto> calcular(final Long usuarioId,
			final double saldoAnterior, final List<SaldoDiarioDto> result)
			throws Exception {
		double saldoInicialDia = saldoAnterior;
		boolean hoje = true;
		
		for (final SaldoDiarioDto o : result) {
			if (hoje) {
				saldoInicialDia = saldoAnterior;
				
				Criteria c = Lancamento.dao.createCriteria();
				c.createAlias("categoria", "_categoria");
				
				 ProjectionList p = Projections.projectionList();
				p.add(Projections.sum("valorOriginal"));
				c.add(Restrictions.eq("usuario.id", usuarioId));
				c.add(Restrictions.eq("dataCompensacao", o.dataCompensacao));
				c.add(Restrictions.eq("_categoria.tipo", CategoriaType.RECEITA.ordinal()));
				c.add(Restrictions.eq("_categoria.estatistica", true));
				c.setProjection(p);
				
				final Double totalReceitas = (Double) c.uniqueResult();
				
				o.setReceitas(totalReceitas);
//				o.setReceitas((Double) new Lancamento()
//						.firstByQuery(
//								"select sum(valorOriginal) from Lancamento where usuario.id = ?1 and dataCompensacao = ?2 and categoria.tipo = ?3 and categoria.estatistica = true",
//								usuarioId, o.dataCompensacao,
//								CategoriaType.RECEITA.ordinal()));
				
				 c = Lancamento.dao.createCriteria();
				 c.createAlias("categoria", "_categoria");
				 
				  p = Projections.projectionList();
				p.add(Projections.sum("valorOriginal"));
				c.add(Restrictions.eq("usuario.id", usuarioId));
				c.add(Restrictions.eq("dataCompensacao", o.dataCompensacao));
				c.add(Restrictions.eq("_categoria.tipo", CategoriaType.DESPESA.ordinal()));
				c.add(Restrictions.eq("_categoria.estatistica", true));
				c.setProjection(p);
				
				final Double totalDespesas = (Double) c.uniqueResult();
				
				
				o.setDespesas(totalDespesas);
//				o.setDespesas((Double) new Lancamento()
//						.firstByQuery(
//								"select sum(valorOriginal) from Lancamento where usuario.id = ?1 and dataCompensacao = ?2 and categoria.tipo = ?3 and categoria.estatistica = true",
//								usuarioId, o.dataCompensacao,
//								CategoriaType.DESPESA.ordinal()));
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
			final Date dataFinal) {
		final List<SaldoDiarioDto> result = new ArrayList<SaldoDiarioDto>();
		
		for (Date data = dataInicio; !data.after(dataFinal); data = AbstractDateTime
				.addDay(data, 1)) {
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
