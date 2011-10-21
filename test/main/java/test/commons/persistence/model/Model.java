package test.commons.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import commons.persistence.AbstractEntity;

@Entity
public class Model extends AbstractEntity<Model> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Size(max = 50)
	@NotNull
	private String description;
	
	@NotNull
	private boolean enabled;
	
	public Model() {
		super();
	}
	
	public Model(final String description, final boolean enabled) {
		super();
		this.description = description;
		this.enabled = enabled;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
}