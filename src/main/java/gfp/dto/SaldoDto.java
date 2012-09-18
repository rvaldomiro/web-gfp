package gfp.dto;

import gfp.model.Conta;

public class SaldoDto {
	
	public static final String SALDO_DISPONIVEL = "Disponível";
	
	public static final String SALDO_DISPONIVEL_MASTERCARD = "Mastercard";
	
	public static final String SALDO_DISPONIVEL_VISA = "Visa";
	
	public static final String SALDO_BLOQUEADO = "Bloqueado";
	
	public static final String SALDO_TOTAL_CONTA = "Total";
	
	public static final String SALDO_ATUAL = "Saldo Atual";
	
	public Conta conta;
	
	public String situacao;
	
	public double saldo;
	
	public SaldoDto(final Conta conta, final String situacao, final double saldo) {
		super();
		this.conta = conta;
		this.situacao = situacao;
		this.saldo = saldo;
	}
	
	public SaldoDto(final String situacao, final double saldo) {
		super();
		this.situacao = situacao;
		this.saldo = saldo;
	}
	
}
