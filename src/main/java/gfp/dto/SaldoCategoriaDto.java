package gfp.dto;

import gfp.model.Categoria;

public class SaldoCategoriaDto {
	
	public Categoria categoria;
	
	public double valor;
	
	public SaldoCategoriaDto(final Categoria categoria, final double valor) {
		super();
		this.categoria = categoria;
		this.valor = valor;
	}
	
}
