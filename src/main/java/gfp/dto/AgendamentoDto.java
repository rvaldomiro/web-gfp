package gfp.dto;

import gfp.model.Lancamento;

import java.util.Date;

public class AgendamentoDto {
	
	private Date dataInicio;
	
	private Date dataFinal;
	
	private int frequencia;
	
	private int dia;
	
	private Lancamento lancamento;
	
	private boolean anteciparFinaisSemana;
	
	public Date getDataFinal() {
		return this.dataFinal;
	}
	
	public Date getDataInicio() {
		return this.dataInicio;
	}
	
	public int getDia() {
		return this.dia;
	}
	
	public int getFrequencia() {
		return this.frequencia;
	}
	
	public Lancamento getLancamento() {
		return this.lancamento;
	}
	
	public boolean isAnteciparFinaisSemana() {
		return this.anteciparFinaisSemana;
	}
	
	public void setAnteciparFinaisSemana(final boolean anteciparFinaisSemana) {
		this.anteciparFinaisSemana = anteciparFinaisSemana;
	}
	
	public void setDataFinal(final Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public void setDia(final int dia) {
		this.dia = dia;
	}
	
	public void setFrequencia(final int frequencia) {
		this.frequencia = frequencia;
	}
	
	public void setLancamento(final Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	
}
