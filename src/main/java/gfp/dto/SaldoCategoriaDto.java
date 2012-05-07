package gfp.dto;

import gfp.model.Categoria;

import java.util.Date;

public class SaldoCategoriaDto {
	
	public Categoria categoria;
	
	public double valor;
	
	public double previsao;
	
	public SaldoCategoriaDto(final Date referencia, final Categoria categoria,
			final double valor) {
		super();
		this.categoria = categoria;
		this.valor = valor;
		this.previsao = categoria.calcularMedia(referencia, 3);
	}
	
}
