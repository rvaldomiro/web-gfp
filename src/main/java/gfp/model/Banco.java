package gfp.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import logus.commons.persistence.AbstractPersistentClass;
import logus.commons.persistence.hibernate.dao.HibernateDao;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Banco extends AbstractPersistentClass<Banco> {
	
	private static final long serialVersionUID = 1L;
	
	public static HibernateDao<Banco> dao;
	
	public static List<Banco> listar() {
		return dao.findAll();
	}
	
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "increment")
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
	
	public Long getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
	@Override
	protected HibernateDao<Banco> getDao() {
		return dao;
	}
	
	@Override
	protected void setDao(HibernateDao<Banco> arg0) {
		dao = arg0;
	}
	
}
