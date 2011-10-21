package gfp.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import commons.persistence.AbstractEntity;

@Entity
public class Banco extends AbstractEntity<Banco> {
	
	private static final long serialVersionUID = 1L;
	
	public static List<Banco> listar() {
		return new Banco().all();
	}
	
	@Id
	private Long id;
	
	@NotNull
	@Size(max = 20)
	private String nome;
	
	public Banco() {
		super();
	}
	
	public Banco(final String nome) {
		super();
		this.nome = nome;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		nextSequence("id");
	}
	
}
