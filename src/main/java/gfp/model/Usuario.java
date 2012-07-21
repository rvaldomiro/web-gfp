package gfp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import logus.commons.persistence.AbstractPersistentClass;
import logus.commons.persistence.hibernate.dao.HibernateDao;
import logus.commons.string.StringUtil;

@Entity
public class Usuario extends AbstractPersistentClass<Usuario> {
	
	private static final long serialVersionUID = 1L;
	
	public static HibernateDao<Usuario> dao;
	
	@Id
	private Long id;
	
	@NotNull
	@Size(max = 50)
	private String nome;
	
	@NotNull
	@Size(max = 30)
	private String login;
	
	@Size(max = 30)
	private String senha;
	
	@Size(max = 50)
	private String email;
	
	@NotNull
	private boolean ativo;
	
	@NotNull
	private boolean administrador;
	
	public Usuario() {
		super();
	}
	
	public Usuario(final String login, final String senha) {
		super();
		this.login = login;
		this.senha = senha;
	}
	
	public Usuario(final String nome, final String login, final String senha) {
		super();
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.ativo = true;
	}
	
	public Usuario(final String nome, final String login, final String senha,
			final boolean administrador) {
		super();
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.administrador = administrador;
		this.ativo = true;
	}
	
	private void alterarSenha() {
		this.senha = String.valueOf(this.senha.hashCode());
	}
	
	@Override
	protected HibernateDao<Usuario> getDao() {
		return dao;
	}
	
	@Override
	protected void setDao(final HibernateDao<Usuario> arg0) {
		dao = arg0;
	}
	
	@Override
	public void delete() throws Exception {
		for (final Lancamento o : Lancamento.dao.allByFields("usuario", this)) {
			o.delete();
		}
		
		for (final Categoria o : Categoria.dao.allByFields("usuario", this)) {
			o.delete();
		}
		
		for (final Conta o : Conta.dao.allByFields("usuario", this)) {
			o.delete();
		}
		
		super.delete();
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getSenha() {
		return this.senha;
	}
	
	public boolean isAdministrador() {
		return this.administrador;
	}
	
	public boolean isAtivo() {
		return this.ativo;
	}
	
	@Override
	public Usuario save() throws Exception {
		final boolean novo = this.id == null;
		final Usuario result = super.save();
		
		if (novo) {
			Categoria.criarPadroes(result);
		}
		
		return result;
	}
	
	public void setAdministrador(final boolean administrador) {
		this.administrador = administrador;
	}
	
	public void setAtivo(final boolean ativo) {
		this.ativo = ativo;
	}
	
	public void setEmail(final String email) {
		this.email = email;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setLogin(final String login) {
		this.login = login;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
	public void setSenha(final String senha) {
		this.senha = senha;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		this.nome = StringUtil.capitalize(this.nome);
		
		if (this.id == null) {
			this.id = dao.getNextSequence(this, "id").longValue();
			alterarSenha();
		}
	}
	
}
