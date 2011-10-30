package commons.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import logus.commons.log.LogBuilder;
import logus.commons.sql.QueryLoader;

public class JdbcConnection implements Connection {
	
	private Connection connection;
	
	private final Map<ResultSet, Statement> statements = new HashMap<ResultSet, Statement>();
	
	private final boolean logSQL;
	
	public JdbcConnection(final Connection connection) {
		super();
		this.connection = connection;
		this.logSQL = false;
		
	}
	
	public JdbcConnection(final Connection connection, final boolean logSQL) {
		super();
		this.connection = connection;
		this.logSQL = logSQL;
	}
	
	private void closeResultSets() throws SQLException {
		for (final ResultSet rs : new HashSet<ResultSet>(
				this.statements.keySet())) {
			closeResultSet(rs);
			
		}
		
	}
	
	private ResultSet getResultSet(final String sql) throws SQLException {
		final Statement stmt = getStatement();
		final ResultSet resultSet = stmt.executeQuery(sql);
		this.statements.put(resultSet, stmt);
		return resultSet;
	}
	
	private ResultSet getResultSet(final String sql, final Statement stmt)
			throws SQLException {
		final ResultSet resultSet = stmt.executeQuery(sql);
		this.statements.put(resultSet, stmt);
		return resultSet;
	}
	
	@Override
	public void clearWarnings() throws SQLException {
		this.connection.clearWarnings();
	}
	
	@Override
	public void close() throws SQLException {
		closeResultSets();
		this.connection.close();
		this.connection = null;
	}
	
	public void closePreparedStatement(final PreparedStatement ps)
			throws SQLException {
		if (ps == null) {
			return;
		}
		
		ps.close();
	}
	
	public void closeResultSet(ResultSet arg0) throws SQLException {
		if (arg0 == null) {
			return;
		}
		
		Statement stmt = this.statements.remove(arg0);
		
		if (stmt != null) {
			stmt.close();
			stmt = null;
		} else {
			arg0.close();
			arg0 = null;
		}
	}
	
	@Override
	public void commit() throws SQLException {
		this.connection.commit();
	}
	
	@Override
	public Array createArrayOf(final String typeName, final Object[] elements)
			throws SQLException {
		return null;
	}
	
	@Override
	public Blob createBlob() throws SQLException {
		return null;
	}
	
	@Override
	public Clob createClob() throws SQLException {
		return null;
	}
	
	@Override
	public NClob createNClob() throws SQLException {
		return null;
	}
	
	@Override
	public SQLXML createSQLXML() throws SQLException {
		return null;
	}
	
	@Override
	public Statement createStatement() throws SQLException {
		return this.connection.createStatement();
	}
	
	@Override
	public Statement createStatement(final int resultSetType,
			final int resultSetConcurrency) throws SQLException {
		return getStatement();
	}
	
	@Override
	public Statement createStatement(final int resultSetType,
			final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		return this.connection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}
	
	@Override
	public Struct createStruct(final String typeName, final Object[] attributes)
			throws SQLException {
		return null;
	}
	
	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.connection.getAutoCommit();
	}
	
	@Override
	public String getCatalog() throws SQLException {
		return this.connection.getCatalog();
	}
	
	@Override
	public Properties getClientInfo() throws SQLException {
		return null;
	}
	
	@Override
	public String getClientInfo(final String name) throws SQLException {
		return null;
	}
	
	@Override
	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}
	
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.connection.getMetaData();
	}
	
	public Statement getStatement() throws SQLException {
		return this.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
	}
	
	public Statement getStatementUpdatable() throws SQLException {
		return this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_UPDATABLE);
	}
	
	@Override
	public int getTransactionIsolation() throws SQLException {
		return this.connection.getTransactionIsolation();
	}
	
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return this.connection.getTypeMap();
	}
	
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.connection.getWarnings();
	}
	
	@Override
	public boolean isClosed() throws SQLException {
		return this.connection == null ? true : this.connection.isClosed();
	}
	
	@Override
	public boolean isReadOnly() throws SQLException {
		
		return this.connection.isReadOnly();
	}
	
	@Override
	public boolean isValid(final int timeout)

	throws SQLException {
		return false;
	}
	
	@Override
	public boolean isWrapperFor(final Class<?> arg0) throws SQLException {
		return false;
	}
	
	@Override
	public String nativeSQL(final String sql) throws SQLException {
		
		return this.connection.nativeSQL(sql);
	}
	
	public ResultSet openResultSet(final QueryLoader arg0) throws SQLException {
		final long inicio = System.currentTimeMillis();
		
		if (this.logSQL) {
			LogBuilder.info("LOG: ".concat(arg0.getSql()));
		}
		
		try {
			return getResultSet(arg0.getSql());
		} finally {
			if (this.logSQL) {
				LogBuilder.info("LOG: ".concat(String.valueOf(System
						.currentTimeMillis() - inicio)));
			}
		}
	}
	
	public ResultSet openResultSet(final QueryLoader arg0, final Statement stmt)
			throws SQLException {
		return openResultSet(arg0.toString(), stmt);
	}
	
	public ResultSet openResultSet(final String arg0) throws SQLException {
		
		final long inicio = System.currentTimeMillis();
		
		if (this.logSQL)

		{
			LogBuilder.info("LOG: ".concat(arg0));
		}
		
		try {
			return getResultSet(arg0);
		} finally {
			if (this.logSQL) {
				LogBuilder.info("LOG: ".concat(String.valueOf(System
						.currentTimeMillis() - inicio)));
			}
		}
	}
	
	public ResultSet openResultSet(final String arg0, final Statement stmt)
			throws SQLException {
		
		final long inicio = System.currentTimeMillis();
		
		if (this.logSQL) {
			LogBuilder.info("LOG: ".concat(arg0));
		}
		
		try {
			return getResultSet(arg0, stmt);
		} finally {
			if (this.logSQL) {
				LogBuilder.info("LOG: ".concat(String.valueOf(System

				.currentTimeMillis() - inicio)));
			}
		}
	}
	
	public ResultSet openResultSet(final StringBuffer arg0) throws SQLException {
		final long inicio = System.currentTimeMillis();
		
		if (this.logSQL) {
			LogBuilder.info("LOG: ".concat(arg0.toString()));
			
		}
		
		try {
			return getResultSet(arg0.toString());
		} finally {
			if (this.logSQL) {
				LogBuilder.info("LOG: ".concat(String.valueOf(System
						.currentTimeMillis() - inicio)));
			}
		}
	}
	
	public ResultSet openResultSet(final StringBuilder arg0)
			throws SQLException {
		final long inicio = System.currentTimeMillis();
		
		if (this.logSQL) {
			LogBuilder.info("LOG: ".concat(arg0.toString()));
		}
		
		try {
			return getResultSet(arg0.toString());
		} finally {
			if (this.logSQL) {
				LogBuilder.info("LOG: ".concat(String.valueOf(System
						.currentTimeMillis() - inicio)));
			}
		}
	}
	
	public ResultSet openResultSetUpdatable(final QueryLoader arg0)
			throws SQLException {
		return openResultSet(arg0.getSql(), getStatementUpdatable());
	}
	
	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		return this.connection.prepareCall(sql);
	}
	
	@Override
	public CallableStatement prepareCall(final String sql,
			final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		return this.connection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}
	
	@Override
	public CallableStatement prepareCall(final String sql,
			final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		return this.connection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}
	
	public PreparedStatement prepareStatement(final QueryLoader query)
			throws SQLException {
		return this.connection.prepareStatement(query.getSql());
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql)
			throws SQLException {
		return this.connection.prepareStatement(sql);
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql,
			final int autoGeneratedKeys) throws SQLException {
		return this.connection.prepareStatement(sql, autoGeneratedKeys);
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql,
			final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		return this.connection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql,
			final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		return this.connection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql,
			final int[] columnIndexes) throws SQLException {
		return this.connection.prepareStatement(sql, columnIndexes);
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql,
			final String[] columnNames) throws SQLException {
		return this.connection.prepareStatement(sql, columnNames);
	}
	
	public PreparedStatement prepareStatement(final StringBuffer sql)
			throws SQLException {
		return prepareStatement(sql.toString());
	}
	
	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		this.connection.releaseSavepoint(savepoint);
	}
	
	public ResultSet reopenResultSet(final ResultSet arg0, final String arg1)
			throws SQLException {
		closeResultSet(arg0);
		return getResultSet(arg1);
	}
	
	@Override
	public void rollback() throws SQLException {
		this.connection.rollback();
	}
	
	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		this.connection.rollback(savepoint);
	}
	
	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		this.connection.setAutoCommit(autoCommit);
	}
	
	@Override
	public void setCatalog(final String catalog) throws SQLException {
		this.connection.setCatalog(catalog);
	}
	
	@Override
	public void setClientInfo(final Properties properties)
			throws SQLClientInfoException {
		//
	}
	
	@Override
	public void setClientInfo(final String name, final String value)
			throws SQLClientInfoException {
		//
	}
	
	@Override
	public void setHoldability(final int holdability) throws SQLException {
		this.connection.setHoldability(holdability);
	}
	
	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		this.connection.setReadOnly(readOnly);
	}
	
	@Override
	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}
	
	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		return this.connection.setSavepoint(name);
	}
	
	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		this.connection.setTransactionIsolation(level);
	}
	
	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		this.connection.setTypeMap(map);
	}
	
	@Override
	public <T> T unwrap(final Class<T> arg0) throws SQLException {
		return null;
	}
	
}
