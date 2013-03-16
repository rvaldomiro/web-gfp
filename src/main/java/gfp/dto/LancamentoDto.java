package gfp.dto;

import gfp.model.Conta;
import gfp.model.Usuario;
import gfp.type.LancamentoPeriodoType;
import gfp.type.LancamentoSituacaoType;

import java.util.Date;

import logus.commons.datetime.DateUtc;
import logus.commons.util.DateUtil;

public class LancamentoDto {
	
	private Long idUsuario;
	
	private int tipoPeriodo;
	
	private int situacao;
	
	private Date dataInicio;
	
	private Date dataFinal;
	
	private Long categoria;
	
	private Conta conta;
	
	private String observacao;
	
	public LancamentoDto() {
		super();
	}
	
	public LancamentoDto(final Usuario usuario) throws Exception {
		super();
		this.idUsuario = usuario.getId();
		this.dataInicio = DateUtil.today();
		this.dataFinal = DateUtil.today();
		this.tipoPeriodo = LancamentoPeriodoType.VENCIMENTO.ordinal();
		this.situacao = LancamentoSituacaoType.INDEFINIDA.ordinal();
	}
	
	public Long getCategoria() {
		return this.categoria;
	}
	
	public Conta getConta() {
		return this.conta;
	}
	
	public Date getDataFinal() {
		return this.dataFinal;
	}
	
	public String getDataFinalUtc() {
		return DateUtc.get(this.dataFinal);
	}
	
	public Date getDataInicio() {
		return this.dataInicio;
	}
	
	public String getDataInicioUtc() {
		return DateUtc.get(this.dataInicio);
	}
	
	public Long getIdUsuario() {
		return this.idUsuario;
	}
	
	public String getObservacao() {
		return this.observacao;
	}
	
	public int getSituacao() {
		return this.situacao;
	}
	
	public int getTipoPeriodo() {
		return this.tipoPeriodo;
	}
	
	public void setCategoria(final Long categoria) {
		this.categoria = categoria;
	}
	
	public void setConta(final Conta conta) {
		this.conta = conta;
	}
	
	public void setDataFinal(final Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public void setDataFinalUtc(final String dataFinal) {
		this.dataFinal = DateUtc.set(dataFinal, "23:59:59");
	}
	
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public void setDataInicioUtc(final String dataInicio) {
		this.dataInicio = DateUtc.set(dataInicio, "00:00:00");
	}
	
	public void setIdUsuario(final Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public void setObservacao(final String observacao) {
		this.observacao = observacao;
	}
	
	public void setSituacao(final int situacao) {
		this.situacao = situacao;
	}
	
	public void setTipoPeriodo(final int tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}
	
}
