package gfp.model;

import gfp.type.CategoriaType;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import logus.commons.datetime.DateUtil;
import logus.commons.persistence.AbstractPersistentClass;
import logus.commons.persistence.hibernate.dao.HibernateDao;
import logus.commons.string.StringUtil;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Categoria extends AbstractPersistentClass<Categoria> {
	
	private static final long serialVersionUID = 1L;
	
	public static HibernateDao<Categoria> dao;
	
	public static void criarPadroes(final Usuario usuario) throws Exception {
		Categoria.obterTransferencia(usuario);
		new Categoria(usuario, "Salário", CategoriaType.RECEITA).save();
		new Categoria(usuario, "Supermercado", CategoriaType.DESPESA).save();
		new Categoria(usuario, "Combustível", CategoriaType.DESPESA).save();
		new Categoria(usuario, "Energia Elétrica", CategoriaType.DESPESA)
				.save();
		new Categoria(usuario, "Telefone", CategoriaType.DESPESA).save();
		new Categoria(usuario, "Internet", CategoriaType.DESPESA).save();
		new Categoria(usuario, "Água", CategoriaType.DESPESA).save();
		new Categoria(usuario, "Saque Caixa Eletrônico", CategoriaType.DESPESA,
				true).save();
		
		Conta.obterCarteira(usuario);
	}
	
	public static List<Categoria> listar(final Long usuarioId) throws Exception {
		return dao.allByFields("usuario.id", usuarioId);
	}
	
	public static Categoria obterTransferencia(final Usuario usuario)
			throws Exception {
		final Categoria template = new Categoria(usuario, "Transferência",
				CategoriaType.RECEITA);
		template.setEstatistica(false);
		template.setInterna(true);
		template.setTransferencia(true);
		
		Categoria result = Categoria.dao.first(template);
		
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
	@Size(max = 40)
	private String descricao;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	
	@NotNull
	private Integer tipo;
	
	@NotNull
	private boolean estatistica;
	
	@NotNull
	private boolean transferencia;
	
	@NotNull
	private boolean interna;
	
	@NotNull
	@Column(name = "valor_orcamento")
	private double valorOrcamento;
	
	public Categoria() {
		super();
	}
	
	public Categoria(final Usuario usuario, final String descricao,
			final CategoriaType tipo) {
		super();
		this.usuario = usuario;
		this.descricao = descricao;
		this.tipo = tipo.ordinal();
		this.estatistica = true;
	}
	
	public Categoria(final Usuario usuario, final String descricao,
			final CategoriaType tipo, final boolean transferencia) {
		super();
		this.usuario = usuario;
		this.descricao = descricao;
		this.tipo = tipo.ordinal();
		this.transferencia = transferencia;
	}
	
	@Override
	protected HibernateDao<Categoria> getDao() {
		return dao;
	}
	
	@Override
	protected void setDao(final HibernateDao<Categoria> arg0) {
		dao = arg0;
	}
	
	public double calcularMedia(final Date referencia, final int meses) {
		final int[] datePart = DateUtil.datePart(referencia);
		Date dataInicio = DateUtil.removeMonth(
				DateUtil.build(1, datePart[1], datePart[2]), meses);
		double totalPeriodo = 0.0;
		int mesesPeriodo = 0;
		
		for (int i = 1; i <= meses; i++) {
			final double total = Lancamento.obterTotal(this, dataInicio);
			
			if (total > 0) {
				totalPeriodo += total;
				mesesPeriodo++;
			}
			
			dataInicio = DateUtil.addMonth(dataInicio, 1);
		}
		
		return mesesPeriodo > 0 ? totalPeriodo / mesesPeriodo : 0.0;
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
		final Categoria other = (Categoria) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public Integer getTipo() {
		return this.tipo;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public double getValorOrcamento() {
		return this.valorOrcamento;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}
	
	public boolean isEstatistica() {
		return this.estatistica;
	}
	
	public boolean isInterna() {
		return this.interna;
	}
	
	public boolean isTransferencia() {
		return this.transferencia;
	}
	
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}
	
	public void setEstatistica(final boolean estatistica) {
		this.estatistica = estatistica;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setInterna(final boolean interna) {
		this.interna = interna;
	}
	
	public void setTipo(final Integer tipo) {
		this.tipo = tipo;
	}
	
	public void setTransferencia(final boolean transferencia) {
		this.transferencia = transferencia;
	}
	
	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setValorOrcamento(final double valorOrcamento) {
		this.valorOrcamento = valorOrcamento;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		this.descricao = StringUtil.capitalize(this.descricao);
	}
	
}
