package commons.persistence;

import java.io.Serializable;
import java.util.List;

public interface Entity<T> extends Serializable {
	
	List<T> all();
	
	Object allByQuery(final String conditions, final Object... params)
			throws Exception;
	
	void delete() throws Exception;
	
	void delete(final String conditions, final Object... params)
			throws Exception;
	
	void deleteAll() throws Exception;
	
	@Override
	boolean equals(final Object obj);
	
	T find(final Serializable id) throws Exception;
	
	T findOrCreate(final Serializable id) throws Exception;
	
	T first(final String conditions, final Object... params) throws Exception;
	
	Object firstByQuery(final String conditions, final Object... params)
			throws Exception;
	
	Long getId();
	
	@Override
	int hashCode();
	
	void nextSequence(final String... fields) throws Exception;
	
	T save() throws Exception;
	
	void setId(Long id);
	
	void validate() throws Exception;
	
	List<T> where(final String conditions, final List<Object> params)
			throws Exception;
	
	List<T> where(final String conditions, final Object... params)
			throws Exception;
	
}
