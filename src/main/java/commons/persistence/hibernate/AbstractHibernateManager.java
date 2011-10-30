package commons.persistence.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import logus.commons.log.LogBuilder;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import commons.file.ClassFileFilter;
import commons.file.JarFileFilter;
import commons.persistence.ApplicationContext;
import commons.persistence.Entity;

public abstract class AbstractHibernateManager implements HibernateManager {
	
	private Configuration configuration;
	
	private final boolean annotationBased = true;
	
	private final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
	
	private SessionFactory factory;
	
	private Transaction currentTransaction;
	
	private final String contextId;
	
	private final Properties connectionProperties;
	
	protected AbstractHibernateManager(final String id,
			final Properties connectionProperties, final URI... entityPath)
			throws IllegalArgumentException, SecurityException,
			URISyntaxException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, IOException {
		this.connectionProperties = connectionProperties;
		
		TimeZone.setDefault(new SimpleTimeZone(-3 * 60 * 60 * 1000, "GMT-3:00"));
		
		if (ApplicationContext.contains(id)) {
			throw new InstantiationException(
					"ContextId já inicializado anteriormente!");
		}
		
		this.contextId = id;
		
		try {
			openConnection(entityPath);
		} catch (final InstantiationException e) {
			LogBuilder
					.error("Classe deve implementar um construtor padrão sem argumentos!");
			throw e;
		}
		
		ApplicationContext.put(this);
	}
	
	private void injectManager(final Class<Entity<?>> p)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException, NoSuchMethodException,
			InvocationTargetException {
		final Entity<?> pInstance = p.newInstance();
		final Method m = pInstance.getClass().getSuperclass()
				.getDeclaredMethod("setManager", HibernateManager.class);
		m.setAccessible(true);
		m.invoke(pInstance, this);
	}
	
	@SuppressWarnings("unchecked")
	private void loadClasses(final List<String> classes)
			throws ClassNotFoundException, IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final Map<String, String> modelClasses = new HashMap<String, String>();
		
		for (String entityClass : classes) {
			entityClass = entityClass.replaceAll(".class", "");
			
			final String className = entityClass.substring(
					entityClass.lastIndexOf("/") + 1).toLowerCase();
			
			if (!className.endsWith("id")) {
				modelClasses.put(className, entityClass);
			}
		}
		
		for (final String modelClassName : modelClasses.keySet()) {
			final String modelClassPath = modelClasses.get(modelClassName);
			final Class<?> c = Class.forName(modelClassPath
					.replaceAll("/", "."));
			
			if (this.configuration instanceof AnnotationConfiguration) {
				((AnnotationConfiguration) this.configuration)
						.addAnnotatedClass(c);
			} else {
				this.configuration.addClass(c);
			}
			
// injectDao((Class<Entity<?>>) c, GenericHibernateDao.class);
			injectManager((Class<Entity<?>>) c);
		}
	}
	
	private void loadClassFromFile(final URI... entityPath)
			throws URISyntaxException, IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {
		final List<String> classes = new ArrayList<String>();
		
		for (final URI uri : entityPath) {
			final String uriPath = uri.toString();
			final File uriDir;
			
			uriDir = new File(getClass().getResource(uriPath).toURI());
			
			if (!uriDir.isDirectory()) {
				continue;
			}
			
			for (final File file : uriDir.listFiles(new ClassFileFilter())) {
				classes.add(uriPath.substring(1).concat("/")
						.concat(file.getName()));
			}
		}
		
		loadClasses(classes);
	}
	
	private void loadClassFromJar(final File[] fi, final URI... entityPath)
			throws IOException, IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {
		final List<String> classes = new ArrayList<String>();
		
		for (final File f : fi) {
			for (final URI uri : entityPath) {
				final URL jar = f.toURI().toURL();
				final InputStream in = jar.openStream();
				final ZipInputStream zis = new ZipInputStream(in);
				ZipEntry ze;
				
				while ((ze = zis.getNextEntry()) != null) {
					if (ze.getName().endsWith("class") &&
							ze.getName()
									.startsWith(uri.toString().substring(1))) {
						classes.add(ze.getName());
					}
				}
				
				zis.close();
			}
		}
		
		loadClasses(classes);
	}
	
	private void openConnection(final URI... entityPath)
			throws URISyntaxException, IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, IOException {
		if (!isFactoryClosed()) {
			return;
		}
		
		this.configuration = this.annotationBased ? new AnnotationConfiguration()
				: new Configuration();
		this.configuration.setProperties(this.connectionProperties);
		
		final File[] jars = new File(".").listFiles(new JarFileFilter());
		
		final boolean environmentWebOrProduction = jars != null &&
				jars.length > 0;
		
		if (!environmentWebOrProduction) {
			loadClassFromFile(entityPath);
		} else {
			loadClassFromJar(jars, entityPath);
		}
		
		this.factory = this.configuration.buildSessionFactory();
	}
	
	@Override
	public Transaction beginTransaction() {
		if (this.currentTransaction == null) {
			this.currentTransaction = currentSession().beginTransaction();
			return this.currentTransaction;
		}
		
		return null;
	}
	
	@Override
	public boolean closeCurrentSession() {
		final Session s = this.threadSession.get();
		this.threadSession.set(null);
		
		if (s != null) {
			try {
				final Transaction tx = s.getTransaction();
				rollbackTransaction(tx);
				s.clear();
			} catch (final Exception e) {
				return false;
			} finally {
				s.close();
			}
		}
		
		return true;
	}
	
	@Override
	public boolean closeSessionFactory() {
		if (this.factory != null) {
			closeCurrentSession();
			
			this.factory.close();
			this.factory = null;
			this.configuration = null;
			
			ApplicationContext.remove(this.contextId);
		}
		
		return isFactoryClosed();
	}
	
	@Override
	public void commitTransaction() {
		commitTransaction(this.currentTransaction);
	}
	
	@Override
	public void commitTransaction(Transaction tx) {
		if (tx != null) {
			if (tx.isActive() && !tx.wasCommitted()) {
				tx.commit();
			}
			
			tx = null;
			this.currentTransaction = null;
		}
	}
	
	@Override
	public Session currentSession() {
		Session s = this.threadSession.get();
		
		if (s == null) {
			s = this.factory.openSession();
			this.threadSession.set(s);
		}
		
		return s;
	}
	
	@Override
	public Properties getConnectionProperties() {
		return this.connectionProperties;
	}
	
	@Override
	public String getContextId() {
		return this.contextId;
	}
	
	@Override
	public boolean inTransaction() {
		return this.currentTransaction != null;
	}
	
	@Override
	public boolean isFactoryClosed() {
		return this.factory == null || this.factory.isClosed();
	}
	
	@Override
	public void rollbackTransaction() {
		rollbackTransaction(this.currentTransaction);
	}
	
	@Override
	public void rollbackTransaction(Transaction tx) {
		if (tx != null) {
			if (tx.isActive() && !tx.wasRolledBack()) {
				tx.rollback();
				closeCurrentSession();
			}
			
			tx = null;
			this.currentTransaction = null;
		}
	}
	
	public SessionFactory sessionFactory() {
		return this.factory;
	}
	
}
