package gfp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import commons.persistence.AbstractEntity;
import commons.util.StringUtils;

@Entity
public class Usuario extends AbstractEntity<Usuario> {
	
	private static final long serialVersionUID = 1L;
	
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
	
	public Usuario(final String nome, final String login, final String senha) {
		super();
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.ativo = true;
	}
	
	private void alterarSenha() {
		this.senha = String.valueOf(this.senha.hashCode());
	}
	
	public String getEmail() {
		return this.email;
	}
	
	@Override
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
	
	public void setAdministrador(final boolean administrador) {
		this.administrador = administrador;
	}
	
	public void setAtivo(final boolean ativo) {
		this.ativo = ativo;
	}
	
	public void setEmail(final String email) {
		this.email = email;
	}
	
	@Override
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
		this.nome = StringUtils.capitalize(this.nome);
		
		if (this.id == null) {
			nextSequence("id");
			alterarSenha();
		}
	}
	
}
