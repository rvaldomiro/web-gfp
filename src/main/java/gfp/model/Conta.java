package gfp.model;

import gfp.type.ContaType;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import commons.persistence.AbstractEntity;

@Entity
public class Conta extends AbstractEntity<Conta> {
	
	private static final long serialVersionUID = 1L;
	
	public static List<Conta> listar(final Long usuarioId) throws Exception {
		return new Conta().where("usuario.id = ?1", usuarioId);
	}
	
	public static List<Conta> listarAtivas(final Long usuarioId)
			throws Exception {
		return new Conta().where("usuario.id = ?1 and ativa = ?2", usuarioId,
				true);
	}
	
	@Id
	private Long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "banco")
	private Banco banco;
	
	@NotNull
	@Size(max = 30)
	private String identificacao;
	
	@NotNull
	private boolean ativa;
	
	@NotNull
	private Integer tipo;
	
	public Conta() {
		super();
	}
	
	public Conta(final Usuario usuario, final Banco banco,
			final String identificacao) {
		super();
		this.usuario = usuario;
		this.banco = banco;
		this.identificacao = identificacao;
		this.ativa = true;
		this.tipo = ContaType.CONTA_CORRENTE.ordinal();
	}
	
	public Conta(final Usuario usuario, final String identificacao) {
		super();
		this.usuario = usuario;
		this.identificacao = identificacao;
		this.ativa = true;
		this.tipo = ContaType.CARTEIRA.ordinal();
	}
	
	public void criarPadrao(final Usuario arg0) throws Exception {
		new Conta(arg0, "Carteira").save();
	}
	
	@Override
	public void delete() throws Exception {
		Lancamento.excluir(this);
		super.delete();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Conta other = (Conta) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	public boolean getAtiva() {
		return this.ativa;
	}
	
	public Banco getBanco() {
		return this.banco;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public String getIdentificacao() {
		return this.identificacao;
	}
	
	public Integer getTipo() {
		return this.tipo;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}
	
	public void setAtiva(final boolean ativa) {
		this.ativa = ativa;
	}
	
	public void setBanco(final Banco banco) {
		this.banco = banco;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setIdentificacao(final String identificacao) {
		this.identificacao = identificacao;
	}
	
	public void setTipo(final Integer tipo) {
		this.tipo = tipo;
	}
	
	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		nextSequence("id");
	}
	
}
