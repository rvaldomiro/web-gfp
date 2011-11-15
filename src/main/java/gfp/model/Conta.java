package gfp.model;

import gfp.type.ContaType;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import logus.commons.persistence.AbstractPersistentClass;
import logus.commons.persistence.hibernate.dao.HibernateDao;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Conta extends AbstractPersistentClass<Conta> {
	
	private static final long serialVersionUID = 1L;
	
	public static HibernateDao<Conta> dao;
	
	public static List<Conta> listar(final Long usuarioId) throws Exception {
		return dao.findAllByField("usuario.id", usuarioId);
	}
	
	public static List<Conta> listarAtivas(final Long usuarioId)
			throws Exception {
		return dao.findAllByFields("usuario.id", usuarioId, "ativa", true);
	}
	
	public static Conta obterCarteira(final Usuario usuario) throws Exception {
		final Conta template = new Conta(usuario, ContaType.CARTEIRA,
				"Carteira");
		Conta result = Conta.dao.findFirstByTemplate(template);
		
		if (result == null) {
			result = template.save();
		}
		
		return result;
	}
	
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "increment")
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
	
	public Conta(final Usuario usuario, final ContaType tipo) {
		super();
		this.usuario = usuario;
		this.tipo = tipo.ordinal();
	}
	
	public Conta(final Usuario usuario, final ContaType tipo,
			final String identificacao) {
		super();
		this.usuario = usuario;
		this.tipo = tipo.ordinal();
		this.identificacao = identificacao;
		this.ativa = true;
	}
	
	public Conta(final Usuario usuario, final String identificacao) {
		super();
		this.usuario = usuario;
		this.identificacao = identificacao;
		this.ativa = true;
		this.tipo = ContaType.CARTEIRA.ordinal();
	}
	
	@Override
	protected HibernateDao<Conta> getDao() {
		return dao;
	}
	
	@Override
	protected void setDao(final HibernateDao<Conta> arg0) {
		dao = arg0;
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
	
	public Banco getBanco() {
		return this.banco;
	}
	
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
	
	public boolean isAtiva() {
		return this.ativa;
	}
	
	public void setAtiva(final boolean ativa) {
		this.ativa = ativa;
	}
	
	public void setBanco(final Banco banco) {
		this.banco = banco;
	}
	
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
	
}
