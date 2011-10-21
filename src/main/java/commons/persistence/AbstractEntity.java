package commons.persistence;

import static org.hibernate.criterion.Projections.max;
import static org.hibernate.criterion.Restrictions.eq;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import commons.log.LogBuilder;
import commons.persistence.hibernate.HibernateManager;

public abstract class AbstractEntity<T> implements Entity<T> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Map<Class<?>, HibernateManager> MANAGER_LOCATOR = new HashMap<Class<?>, HibernateManager>();
	
	private Transaction beginTransaction() {
		return currentSession().beginTransaction();
	}
	
	private void commitTransaction(final Transaction tx) {
		manager().commitTransaction(tx);
	}
	
	private Criteria createCriteria() {
		return currentSession().createCriteria(getClass());
	}
	
	private Object find(final boolean single, final String conditions,
			final Object... params) throws Exception {
		if (conditions == null) {
			throw new IllegalArgumentException("Conditions cannot be null");
		} else if (conditions.trim().isEmpty()) {
			throw new IllegalArgumentException("Conditions cannot be blank");
		}
		
		final String sqlPrefix = !conditions.toLowerCase().startsWith("select") ? "from "
				.concat(getClass().getSimpleName()).concat(" where ") : "";
		
		final Query query = currentSession().createQuery(
				sqlPrefix.concat(conditions));
		
		for (int i = 0; i < params.length; i++) {
			query.setParameter(String.valueOf(i + 1), params[i]);
		}
		
		if (single) {
			query.setMaxResults(1);
		}
		
		return single ? query.uniqueResult() : query.list();
	}
	
	private boolean inTransaction() {
		return manager().inTransaction();
	}
	
	private HibernateManager manager() {
		return MANAGER_LOCATOR.get(getClass());
	}
	
	private void rollbackTransaction(final Transaction tx) {
		manager().rollbackTransaction(tx);
	}
	
	@SuppressWarnings("unused")
	private void setManager(final HibernateManager arg0) {
		MANAGER_LOCATOR.put(getClass(), arg0);
	}
	
	protected Session currentSession() {
		return manager().currentSession();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> all() {
		return currentSession().createQuery(
				"from " + getClass().getSimpleName()).list();
	}
	
	@Override
	public Object allByQuery(final String conditions, final Object... params)
			throws Exception {
		return find(false, conditions, params);
	}
	
	@Override
	public void delete() throws Exception {
		Transaction tx = null;
		
		if (!inTransaction()) {
			tx = beginTransaction();
		}
		
		try {
			currentSession().delete(this);
			commitTransaction(tx);
		} catch (final HibernateException e) {
			LogBuilder.error("AbstractEntityException", e);
			rollbackTransaction(tx);
			throw e;
		}
	}
	
	@Override
	public void delete(final String conditions, final Object... params)
			throws Exception {
		if (conditions == null) {
			throw new IllegalArgumentException("Conditions cannot be null");
		} else if (conditions.trim().isEmpty()) {
			throw new IllegalArgumentException("Conditions cannot be blank");
		}
		
		final String sqlPrefix = "delete from ".concat(
				getClass().getSimpleName()).concat(" where ");
		
		final Query query = currentSession().createQuery(
				sqlPrefix.concat(conditions));
		
		for (int i = 0; i < params.length; i++) {
			query.setParameter(String.valueOf(i + 1), params[i]);
		}
		
		query.executeUpdate();
	}
	
	@Override
	public void deleteAll() throws Exception {
		Transaction tx = null;
		
		if (!inTransaction()) {
			tx = beginTransaction();
		}
		
		try {
			final String sqlPrefix = "delete from ".concat(getClass()
					.getSimpleName());
			
			if (currentSession().createQuery(sqlPrefix).executeUpdate() > 0) {
				commitTransaction(tx);
				manager().closeCurrentSession();
			}
		} catch (final HibernateException e) {
			LogBuilder.error("AbstractEntityException", e);
			rollbackTransaction(tx);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
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
		final Entity<T> other = (Entity<T>) obj;
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T find(final Serializable id) throws Exception {
		return (T) currentSession().get(getClass(), id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T findOrCreate(final Serializable id) throws Exception {
		T result = (T) currentSession().get(getClass(), id);
		
		if (result == null) {
			result = (T) getClass().newInstance();
			
			for (final Field field : result.getClass().getDeclaredFields()) {
				if (field.getAnnotations().length > 0 &&
						(field.getAnnotations()[0].annotationType().getName()
								.equals("javax.persistence.Id") || field
								.getAnnotations()[0].annotationType().getName()
								.equals("javax.persistence.EmbeddedId"))) {
					field.setAccessible(true);
					field.set(result, id);
				}
			}
		}
		
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T first(final String conditions, final Object... params)
			throws Exception {
		return (T) find(true, conditions, params);
	}
	
	@Override
	public Object firstByQuery(final String conditions, final Object... params)
			throws Exception {
		return find(true, conditions, params);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (getId() == null ? 0 : getId().hashCode());
		return result;
	}
	
	@Override
	public void nextSequence(final String... fields) throws Exception {
		final Field field = getClass().getDeclaredField(fields[0]);
		field.setAccessible(true);
		
		if (field.get(this) != null) {
			return;
		}
		
		final Criteria c = createCriteria();
		c.setProjection(field.getName() != "id" ? max("id." + fields[0])
				: max("id"));
		
		for (final Field f : getClass().getDeclaredFields()) {
			for (int index = 1; index < fields.length; index++) {
				if (f.getName().equals(fields[index])) {
					f.setAccessible(true);
					c.add(eq("id." + f.getName(), f.get(this)));
				}
			}
		}
		
		final Long nextId = (Long) c.uniqueResult();
		
		field.set(this, nextId != null ? nextId + 1 : 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T save() throws Exception {
		Transaction tx = null;
		
		if (!inTransaction()) {
			tx = beginTransaction();
		}
		
		try {
			this.validate();
			currentSession().saveOrUpdate(this);
			commitTransaction(tx);
			return (T) this;
		} catch (final HibernateException e) {
			LogBuilder.error("AbstractEntityException", e);
			rollbackTransaction(tx);
			throw e;
		}
	}
	
	@Override
	public String toString() {
		return getClass().getName().concat("#")
				.concat(String.valueOf(getId() != null ? getId() : "null"));
	}
	
	@Override
	public void validate() throws Exception {
		// Implement on decendent class if necessary
	}
	
	@Override
	public List<T> where(final String conditions, final List<Object> params)
			throws Exception {
		return where(conditions, params.toArray());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> where(final String conditions, final Object... params)
			throws Exception {
		return (List<T>) find(false, conditions, params);
	}
	
}
