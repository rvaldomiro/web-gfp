package gfp.model;

import gfp.type.ContaType;

import java.util.List;

import javax.persistence.Column;
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
		return dao.allByFields("usuario.id", usuarioId);
	}
	
	public static List<Conta> listarAtivas(final Long usuarioId)
			throws Exception {
		return dao.allByFields("usuario.id", usuarioId, "ativa", true);
	}
	
	public static Conta obterCarteira(final Usuario usuario) throws Exception {
		final Conta template = new Conta(usuario, ContaType.CARTEIRA,
				"Carteira");
		Conta result = Conta.dao.first(template);
		
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
	
	@NotNull
	@Column(name = "opera_cheque")
	private boolean operaCheque;
	
	@NotNull
	@Column(name = "opera_cartao_debito")
	private boolean operaCartaoDebito;
	
	@NotNull
	@Column(name = "opera_cartao_mastercard")
	private boolean operaCartaoMastercard;
	
	@NotNull
	@Column(name = "limite_mastercard")
	private double limiteMastercard;
	
	@NotNull
	@Column(name = "fechamento_mastercard")
	private int fechamentoMastercard;
	
	@NotNull
	@Column(name = "vencimento_mastercard")
	private int vencimentoMastercard;
	
	@NotNull
	@Column(name = "opera_cartao_visa")
	private boolean operaCartaoVisa;
	
	@NotNull
	@Column(name = "limite_visa")
	private double limiteVisa;
	
	@NotNull
	@Column(name = "fechamento_visa")
	private int fechamentoVisa;
	
	@NotNull
	@Column(name = "vencimento_visa")
	private int vencimentoVisa;
	
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
	
	public int getFechamentoMastercard() {
		return this.fechamentoMastercard;
	}
	
	public int getFechamentoVisa() {
		return this.fechamentoVisa;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getIdentificacao() {
		return this.identificacao;
	}
	
	public double getLimiteMastercard() {
		return this.limiteMastercard;
	}
	
	public double getLimiteVisa() {
		return this.limiteVisa;
	}
	
	public Integer getTipo() {
		return this.tipo;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public int getVencimentoMastercard() {
		return this.vencimentoMastercard;
	}
	
	public int getVencimentoVisa() {
		return this.vencimentoVisa;
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
	
	public boolean isOperaCartaoDebito() {
		return this.operaCartaoDebito;
	}
	
	public boolean isOperaCartaoMastercard() {
		return this.operaCartaoMastercard;
	}
	
	public boolean isOperaCartaoVisa() {
		return this.operaCartaoVisa;
	}
	
	public boolean isOperaCheque() {
		return this.operaCheque;
	}
	
	public void setAtiva(final boolean ativa) {
		this.ativa = ativa;
	}
	
	public void setBanco(final Banco banco) {
		this.banco = banco;
	}
	
	public void setFechamentoMastercard(final int fechamentoMastercard) {
		this.fechamentoMastercard = fechamentoMastercard;
	}
	
	public void setFechamentoVisa(final int fechamentoVisa) {
		this.fechamentoVisa = fechamentoVisa;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setIdentificacao(final String identificacao) {
		this.identificacao = identificacao;
	}
	
	public void setLimiteMastercard(final double limiteMastercard) {
		this.limiteMastercard = limiteMastercard;
	}
	
	public void setLimiteVisa(final double limiteVisa) {
		this.limiteVisa = limiteVisa;
	}
	
	public void setOperaCartaoDebito(final boolean operaCartaoDebito) {
		this.operaCartaoDebito = operaCartaoDebito;
	}
	
	public void setOperaCartaoMastercard(final boolean operaCartaoMastercard) {
		this.operaCartaoMastercard = operaCartaoMastercard;
	}
	
	public void setOperaCartaoVisa(final boolean operaCartaoVisa) {
		this.operaCartaoVisa = operaCartaoVisa;
	}
	
	public void setOperaCheque(final boolean operaCheque) {
		this.operaCheque = operaCheque;
	}
	
	public void setTipo(final Integer tipo) {
		this.tipo = tipo;
	}
	
	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setVencimentoMastercard(final int vencimentoMastercard) {
		this.vencimentoMastercard = vencimentoMastercard;
	}
	
	public void setVencimentoVisa(final int vencimentoVisa) {
		this.vencimentoVisa = vencimentoVisa;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		
		if (this.tipo.equals(ContaType.POUPANCA.ordinal()) ||
				this.tipo.equals(ContaType.CARTEIRA.ordinal())) {
			this.operaCartaoMastercard = false;
			this.operaCartaoVisa = false;
			
			if (this.tipo.equals(ContaType.CARTEIRA.ordinal())) {
				this.operaCheque = false;
				this.operaCartaoDebito = false;
			}
		}
		
		if (!this.operaCartaoMastercard) {
			this.limiteMastercard = 0.0;
			this.fechamentoMastercard = 1;
			this.vencimentoMastercard = 1;
		}
		
		if (!this.operaCartaoVisa) {
			this.limiteVisa = 0.0;
			this.fechamentoVisa = 1;
			this.vencimentoVisa = 1;
		}
	}
	
}
