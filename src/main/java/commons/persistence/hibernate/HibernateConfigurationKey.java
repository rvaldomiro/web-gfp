package commons.persistence.hibernate;

public enum HibernateConfigurationKey {
	
	URL(
			"hibernate.connection.url"),
	USER_NAME(
			"hibernate.connection.username"),
	PASSWORD(
			"hibernate.connection.password"),
	DRIVER_CLASS(
			"hibernate.connection.driver_class"),
	DIALECT(
			"hibernate.dialect"),
	USE_POOL(
			"hibernate.use_c3p0_pool");
	
	private final String value;
	
	HibernateConfigurationKey(final String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
}
