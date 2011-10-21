package commons.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import commons.persistence.hibernate.HibernateManager;

public final class ApplicationContext {
	
	private static final Map<String, HibernateManager> MANAGERS = new HashMap<String, HibernateManager>();
	
	public static boolean closeSession(final HibernateManager manager) {
		return MANAGERS.get(manager.getContextId()).closeCurrentSession();
	}
	
	public static boolean closeSession(final String contextId) {
		return MANAGERS.get(contextId).closeCurrentSession();
	}
	
	public static void closeSessions() {
		for (final String id : getContextIds()) {
			get(id).closeCurrentSession();
		}
	}
	
	public static boolean contains(final String id) {
		return MANAGERS.containsKey(id);
	}
	
	public static HibernateManager get() {
		return new ArrayList<HibernateManager>(MANAGERS.values()).get(0);
	}
	
	public static HibernateManager get(final String id) {
		return MANAGERS.get(id);
	}
	
	public static Set<String> getContextIds() {
		return MANAGERS.keySet();
	}
	
	public static void put(final HibernateManager manager) {
		MANAGERS.put(manager.getContextId(), manager);
	}
	
	public static void remove(final String id) {
		MANAGERS.remove(id);
	}
	
	public static int size() {
		return MANAGERS.size();
	}
	
	private ApplicationContext() {
		super();
	}
	
}