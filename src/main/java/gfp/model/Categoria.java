package gfp.model;

import gfp.type.CategoriaType;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import commons.persistence.AbstractEntity;
import commons.util.StringUtils;

@Entity
public class Categoria extends AbstractEntity<Categoria> {
	
	private static final long serialVersionUID = 1L;
	
	public static List<Categoria> listar(final Long usuarioId) throws Exception {
		return new Categoria().where("usuario.id = ?1", usuarioId);
	}
	
	@Id
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
	
	private Boolean transferencia;
	
	private Boolean interna;
	
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
		this.transferencia = false;
	}
	
	public void criarPadroes(final Usuario arg0) throws Exception {
		new Categoria(arg0, "Salário", CategoriaType.RECEITA).save();
		new Categoria(arg0, "Supermercado", CategoriaType.DESPESA).save();
		new Categoria(arg0, "Combustível", CategoriaType.DESPESA).save();
		new Categoria(arg0, "Energia Elétrica", CategoriaType.DESPESA).save();
		new Categoria(arg0, "Telefone", CategoriaType.DESPESA).save();
		new Categoria(arg0, "Internet", CategoriaType.DESPESA).save();
		new Categoria(arg0, "Água", CategoriaType.DESPESA).save();
	}
	
	@Override
	public void delete() throws Exception {
		Lancamento.excluir(this);
		super.delete();
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public boolean getEstatistica() {
		return this.estatistica;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public Boolean getInterna() {
		return this.interna;
	}
	
	public Integer getTipo() {
		return this.tipo;
	}
	
	public Boolean getTransferencia() {
		return this.transferencia;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}
	
	public void setEstatistica(final boolean estatistica) {
		this.estatistica = estatistica;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setInterna(final Boolean interna) {
		this.interna = interna;
	}
	
	public void setTipo(final Integer tipo) {
		this.tipo = tipo;
	}
	
	public void setTransferencia(final Boolean transferencia) {
		this.transferencia = transferencia;
	}
	
	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		nextSequence("id");
		this.descricao = StringUtils.capitalize(this.descricao);
	}
	
}
