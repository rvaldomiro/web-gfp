package gfp.model;

import gfp.dto.SaldoCategoriaDto;
import gfp.type.CategoriaType;
import gfp.type.ContaType;
import gfp.type.FormaPagamentoType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import logus.commons.datetime.AbstractDateTime;

import org.apache.commons.beanutils.BeanUtils;

import commons.persistence.AbstractEntity;

@Entity
public class Lancamento extends AbstractEntity<Lancamento> {
	
	private static final long serialVersionUID = 1L;
	
	public static void excluir(final Categoria categoria) throws Exception {
		new Lancamento().delete("categoria = ?1", categoria);
	}
	
	public static void excluir(final Conta conta) throws Exception {
		new Lancamento().delete("conta = ?1", conta);
	}
	
	public static List<Lancamento> listarAVencer(final Long usuarioId,
			final Date previsaoPagamento, final CategoriaType categoria)
			throws Exception {
		return new Lancamento()
				.where("usuario.id = ?1 and dataPrevisaoPagamento <= ?2 and categoria.tipo = ?3 and dataPagamento is null",
						usuarioId, previsaoPagamento, categoria.ordinal());
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object[]> listarPrevisaoSaldoDiario(
			final Long usuarioId, final CategoriaType categoria,
			final Date dataInicio, final Date dataFinal) throws Exception {
		return (List<Object[]>) new Lancamento()
				.allByQuery(
						"select dataCompensacao, sum(valorOriginal) from Lancamento where usuario.id = ?1 and categoria.tipo = ?2 and categoria.estatistica = true and dataCompensacao between ?3 and ?4 and (dataPagamento is null or dataPagamento <> dataCompensacao) group by dataCompensacao",
						usuarioId, categoria.ordinal(), dataInicio, dataFinal);
	}
	
	@SuppressWarnings("unchecked")
	public static List<SaldoCategoriaDto> listarSaldoCategoriaMensal(
			final Long usuarioId, final Integer tipoCategoria,
			final Date dataInicio, final Date dataFinal) throws Exception {
		final List<SaldoCategoriaDto> result = new ArrayList<SaldoCategoriaDto>();
		
		final List<Object[]> saldoCategoria = (List<Object[]>) new Lancamento()
				.allByQuery(
						"select categoria.id, sum(valorOriginal) from Lancamento where usuario.id = ?1 and categoria.tipo = ?2 and categoria.estatistica = true and dataCompensacao between ?3 and ?4 group by categoria.id",
						usuarioId, tipoCategoria, dataInicio, dataFinal);
		
		for (final Object[] o : saldoCategoria) {
			result.add(new SaldoCategoriaDto(new Categoria()
					.find((Serializable) o[0]), (Double) o[1]));
		}
		
		return result;
	}
	
	public static double obterSaldoBloqueado(final Conta conta,
			final CategoriaType categoria, final Date dataSaldo)
			throws Exception {
		final Double result = (Double) new Lancamento()
				.firstByQuery(
						"select sum(valorPago) from Lancamento where conta = ?1 and categoria.tipo = ?2 and dataPagamento <= ?3 and dataCompensacao > ?3",
						conta, categoria.ordinal(), dataSaldo);
		return result != null ? result : 0.0;
	}
	
	public static double obterSaldoDisponivel(final Conta conta,
			final CategoriaType categoria, final Date dataSaldo)
			throws Exception {
		final Double result = (Double) new Lancamento()
				.firstByQuery(
						"select sum(valorPago) from Lancamento where conta = ?1 and categoria.tipo = ?2 and dataPagamento <= ?3 and dataCompensacao <= ?3",
						conta, categoria.ordinal(), dataSaldo);
		return result != null ? result : 0.0;
	}
	
	@Id
	private Long id;
	
	@NotNull
	@Column(name = "valor_original")
	private double valorOriginal;
	
	@Size(max = 200)
	private String observacao;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categoria categoria;
	
	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;
	
	@NotNull
	@Column(name = "valor_pago")
	private double valorPago;
	
	@NotNull
	@Column(name = "parcela_numero")
	private Integer parcelaNumero;
	
	@NotNull
	@Column(name = "parcela_quantidade")
	private Integer parcelaQuantidade;
	
	@NotNull
	@Column(name = "forma_pagamento")
	private Integer formaPagamento;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_compensacao")
	private Date dataCompensacao;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_previsao_pagamento")
	private Date dataPrevisaoPagamento;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_vencimento")
	private Date dataVencimento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_pagamento")
	private Date dataPagamento;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "original", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Lancamento> vinculados = new ArrayList<Lancamento>();
	
	@ManyToOne
	private Lancamento original;
	
	@ManyToOne
	@JoinColumn(name = "conta_transferencia")
	private Conta contaTransferencia;
	
	public Lancamento() {
		super();
	}
	
	public Lancamento(final Usuario usuario, final Categoria categoria,
			final double valorOriginal, final FormaPagamentoType formaPagamento) {
		super();
		this.usuario = usuario;
		this.categoria = categoria;
		this.valorOriginal = valorOriginal;
		this.valorPago = 0.0;
		this.dataVencimento = AbstractDateTime.today();
		this.dataPrevisaoPagamento = AbstractDateTime.today();
		this.formaPagamento = formaPagamento.ordinal();
		this.parcelaNumero = 1;
		this.parcelaQuantidade = 1;
	}
	
	private void calcularDataCompensacao() {
		Date previsaoPagamento = this.dataPagamento != null ? this.dataPagamento
				: this.dataPrevisaoPagamento;
		
		if (this.formaPagamento == FormaPagamentoType.CHEQUE.ordinal()) {
			final int prazoCompensacao = this.valorOriginal < 300 ? 3 : 2;
			int dias = 1;
			
			while (dias <= prazoCompensacao) {
				previsaoPagamento = AbstractDateTime.addDay(previsaoPagamento,
						1);
				
				if (AbstractDateTime.dayOfWeek(previsaoPagamento) != Calendar.SATURDAY &&
						AbstractDateTime.dayOfWeek(previsaoPagamento) != Calendar.SUNDAY) {
					dias++;
				}
				
				if (dias == prazoCompensacao &&
						AbstractDateTime.dayOfWeek(previsaoPagamento) == Calendar.SATURDAY) {
					break;
				}
			}
		}
		
		this.dataCompensacao = previsaoPagamento;
	}
	
	private void corrigirHorarioDeVerao() {
		this.dataVencimento = AbstractDateTime.parseBRST(this.dataVencimento);
		this.dataPrevisaoPagamento = AbstractDateTime
				.parseBRST(this.dataPrevisaoPagamento);
		this.dataPagamento = AbstractDateTime.parseBRST(this.dataPagamento);
		this.dataCompensacao = AbstractDateTime.parseBRST(this.dataCompensacao);
	}
	
	private void sincronizarVinculado() throws Exception {
		if (this.contaTransferencia != null) {
			if (this.vinculados == null || this.vinculados.size() == 0) {
				final Lancamento l = new Lancamento();
				BeanUtils.copyProperties(l, this);
				l.setId(this.id + 1);
				l.setConta(this.contaTransferencia);
				l.setOriginal(this);
				l.setContaTransferencia(null);
				
				final Categoria ct = new Categoria()
						.first("usuario = ?1 and descricao = ?2 and tipo = ?3 and estatistica is false and transferencia is false",
								this.usuario, "Transferência",
								CategoriaType.RECEITA.ordinal());
				
				l.setCategoria(ct);
				l.setFormaPagamento(this.contaTransferencia.getTipo().equals(
						ContaType.CARTEIRA.ordinal()) ? FormaPagamentoType.DINHEIRO
						.ordinal() : FormaPagamentoType.CARTAO.ordinal());
				
				if (this.vinculados == null) {
					this.vinculados = new ArrayList<Lancamento>();
				}
				
				this.vinculados.add(l);
			} else {
				final Lancamento vinculado = this.vinculados.get(0);
				
				vinculado
						.setFormaPagamento(this.contaTransferencia.getTipo()
								.equals(ContaType.CARTEIRA.ordinal()) ? FormaPagamentoType.DINHEIRO
								.ordinal() : FormaPagamentoType.CARTAO
								.ordinal());
				
				vinculado.setConta(this.contaTransferencia);
				vinculado.setDataCompensacao(this.dataCompensacao);
				vinculado.setDataPagamento(this.dataPagamento);
				vinculado.setDataPrevisaoPagamento(this.dataPrevisaoPagamento);
				vinculado.setDataVencimento(this.dataVencimento);
				vinculado.setValorOriginal(this.valorOriginal);
				vinculado.setValorPago(this.valorPago);
			}
		} else {
			if (this.vinculados != null && this.vinculados.size() > 0) {
				this.vinculados.remove(0).delete();
				save();
			}
		}
	}
	
	public Categoria getCategoria() {
		return this.categoria;
	}
	
	public Conta getConta() {
		return this.conta;
	}
	
	public Conta getContaTransferencia() {
		return this.contaTransferencia;
	}
	
	public Date getDataCompensacao() {
		return this.dataCompensacao;
	}
	
	public Date getDataPagamento() {
		return this.dataPagamento;
	}
	
	public Date getDataPrevisaoPagamento() {
		return this.dataPrevisaoPagamento;
	}
	
	public Date getDataVencimento() {
		return this.dataVencimento;
	}
	
	public Integer getFormaPagamento() {
		return this.formaPagamento;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public String getObservacao() {
		return this.observacao;
	}
	
	public Lancamento getOriginal() {
		return this.original;
	}
	
	public Integer getParcelaNumero() {
		return this.parcelaNumero;
	}
	
	public Integer getParcelaQuantidade() {
		return this.parcelaQuantidade;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public double getValorOriginal() {
		return this.valorOriginal;
	}
	
	public double getValorPago() {
		return this.valorPago;
	}
	
	public List<Lancamento> getVinculados() {
		return this.vinculados;
	}
	
	public void setCategoria(final Categoria categoria) {
		this.categoria = categoria;
	}
	
	public void setConta(final Conta conta) {
		this.conta = conta;
	}
	
	public void setContaTransferencia(final Conta contaTransferencia) {
		this.contaTransferencia = contaTransferencia;
	}
	
	public void setDataCompensacao(final Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}
	
	public void setDataPagamento(final Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	public void setDataPrevisaoPagamento(final Date dataPrevisaoPagamento) {
		this.dataPrevisaoPagamento = dataPrevisaoPagamento;
	}
	
	public void setDataVencimento(final Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public void setFormaPagamento(final Integer formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setObservacao(final String observacao) {
		this.observacao = observacao;
	}
	
	public void setOriginal(final Lancamento original) {
		this.original = original;
	}
	
	public void setParcelaNumero(final Integer parcelaNumero) {
		this.parcelaNumero = parcelaNumero;
	}
	
	public void setParcelaQuantidade(final Integer parcelaQuantidade) {
		this.parcelaQuantidade = parcelaQuantidade;
	}
	
	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setValorOriginal(final double valorOriginal) {
		this.valorOriginal = valorOriginal;
	}
	
	public void setValorPago(final double valorPago) {
		this.valorPago = valorPago;
	}
	
	public void setVinculados(final List<Lancamento> vinculados) {
		this.vinculados = vinculados;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		nextSequence("id");
		corrigirHorarioDeVerao();
		calcularDataCompensacao();
		sincronizarVinculado();
	}
}
