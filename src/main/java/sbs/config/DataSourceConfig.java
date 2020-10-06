package sbs.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import sbs.service.users.CustomUserDetailsService;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	@SuppressWarnings("unused")
	private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
	// private static final String
	// SQLSERVER_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerConnection";
	private static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	/*
	 * MYSQL DATASOURCE - MAIN APP DATABASE
	 */
	@Profile("mysql")
	@Primary
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(MYSQL_DRIVER);
		dataSource.setUrl("jdbc:mysql://localhost:3306/adrp?useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("pass");
		return dataSource;
	}

	/*
	 * HIBERNATE SESSION FACTORY - MAIN APP DATABASE SESSION FACTORY
	 */
	@Profile("mysql")
	@Bean
	public SessionFactory sessionFactory() {
		DataSource dataSource = dataSource();
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.hbm2ddl.auto", "update");
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(properties);
		sessionBuilder.scanPackages("sbs.model");
		return sessionBuilder.buildSessionFactory();
	}

	/*
	 * HIBERNATE TRANSACTION MANAGER - MAIN APP TRANSACTION MANAGER
	 */
	@Bean
	public HibernateTransactionManager getTransactionManager() {
		SessionFactory sessionFactory = sessionFactory();
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}

	/*
	 * USER DETAILS SERVICE - USING MAIN APP DATABASE
	 */
	@Bean
	@Primary
	@Autowired
	public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
		return customUserDetailsService;
		/*
		 * JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
		 * jdbcImpl.setDataSource(dataSource()); jdbcImpl.
		 * setUsersByUsernameQuery("select username, password, enabled from users where lower(username)=lower(?)"
		 * ); jdbcImpl.
		 * setAuthoritiesByUsernameQuery("select b.username, a.role from user_roles a, users b where lower(b.username)=lower(?) and a.userid=b.userid"
		 * ); return jdbcImpl;
		 */
	}

	/*
	 * PERSISTENT TOKEN - STORED IN MAIN APP DATABASE
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource());
		return db;
	}

	/*
	 * ------------------------ ADDITIONAL DATASOURCES --------------------------
	 */

	/*
	 * ORACLE X3 DATASOURCE
	 */
	@Bean("oracleX3v6DataSource")
	public DataSource oracleX3v6DataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(ORACLE_DRIVER);
		dataSource.setUrl("jdbc:oracle:thin:@192.168.1.6:1521:X3V6POL");
		dataSource.setUsername("x3ro");
		dataSource.setPassword("readonly");
		return dataSource;
	}

	@Bean(name = "oracleX3v6JdbcTemplate")
	public JdbcTemplate oracleX3v6JdbcTemplate() throws SQLException {
		return new JdbcTemplate(oracleX3v6DataSource());
	}
	
	/*
	 * ORACLE X3v11 DATASOURCE
	 */
	@Bean("oracleX3v11DataSource")
	public DataSource oracleX3v11DataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(ORACLE_DRIVER);
		dataSource.setUrl("jdbc:oracle:thin:@192.168.1.16:1521:SAGEX3P");
		dataSource.setUsername("x3");
		dataSource.setPassword("tiger");
		return dataSource;
	}
	
	@Bean(name = "oracleX3v11JdbcTemplate")
	public JdbcTemplate oracleX3v11JdbcTemplate() throws SQLException {
		return new JdbcTemplate(oracleX3v11DataSource());
	}

	/*
	 * ORACLE GEODE DATASOURCE
	 */
	@Bean("oracleGeodeDataSource")
	public DataSource oracleGeodeDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(ORACLE_DRIVER);
		dataSource.setUrl("jdbc:oracle:thin:@192.168.1.26:1521:GEODEV6");
		dataSource.setUsername("system");
		dataSource.setPassword("manager");
		return dataSource;
	}

	@Bean(name = "oracleGeodeJdbcTemplate")
	public JdbcTemplate oracleGeodeJdbcTemplate() throws SQLException {
		return new JdbcTemplate(oracleGeodeDataSource());
	}

	/*
	 * SQL SERVER - OPTIMA - ADR
	 */
	@Bean("optimaAdrDataSource")
	public DataSource optimaAdrDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(SQLSERVER_DRIVER);
		dataSource.setUrl("jdbc:sqlserver://ATWSRV-OPTIMA\\OPTIMA:50066;databaseName=CDN_ADR_S_A_2017;");
		dataSource.setUsername("sa");
		dataSource.setPassword("Optima123!");
		return dataSource;
	}

	@Bean(name = "optimaAdrJdbcTemplate")
	public JdbcTemplate optimaAdrJdbcTemplate() throws SQLException {
		return new JdbcTemplate(optimaAdrDataSource());
	}
	/*
	 * SQL SERVER - OPTIMA - WPS
	 */
	@Bean("optimaWpsDataSource")
	public DataSource optimaWpsDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(SQLSERVER_DRIVER);
		dataSource.setUrl("jdbc:sqlserver://ATWSRV-OPTIMA\\OPTIMA:50066;databaseName=CDN_W_P_S__Sp__z_o_o_;");
		dataSource.setUsername("sa");
		dataSource.setPassword("Optima123!");
		return dataSource;
	}
	
	@Bean(name = "optimaWpsJdbcTemplate")
	public JdbcTemplate optimaWpsJdbcTemplate() throws SQLException {
		return new JdbcTemplate(optimaWpsDataSource());
	}
	/*
	 * SQL SERVER - OPTIMA - ADECCO
	 */
	@Bean("optimaAdeccoDataSource")
	public DataSource optimaAdeccoDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(SQLSERVER_DRIVER);
		dataSource.setUrl("jdbc:sqlserver://ATWSRV-OPTIMA\\OPTIMA:50066;databaseName=CDN_Adecco;");
		dataSource.setUsername("sa");
		dataSource.setPassword("Optima123!");
		return dataSource;
	}
	
	@Bean(name = "optimaAdeccoJdbcTemplate")
	public JdbcTemplate optimaAdeccoJdbcTemplate() throws SQLException {
		return new JdbcTemplate(optimaAdeccoDataSource());
	}
	/*
	 * SQL SERVER - OPTIMA - UA
	 */
	@Bean("optimaUaDataSource")
	public DataSource optimaUaDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(SQLSERVER_DRIVER);
		dataSource.setUrl("jdbc:sqlserver://ATWSRV-OPTIMA\\OPTIMA:50066;databaseName=CDN_Ukrai_cy;");
		dataSource.setUsername("sa");
		dataSource.setPassword("Optima123!");
		return dataSource;
	}
	
	@Bean(name = "optimaUaJdbcTemplate")
	public JdbcTemplate optimaUaJdbcTemplate() throws SQLException {
		return new JdbcTemplate(optimaUaDataSource());
	}

	/*
	 * MySQL SERVER - REDMINE
	 */
	@Bean("redmineDataSource")
	public DataSource redmineDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(MYSQL_DRIVER);
		dataSource.setUrl("jdbc:mysql://10.1.1.226:3307/bitnami_redmine?useSSL=false");
		dataSource.setUsername("redminedefault");
		dataSource.setPassword("reddef1");
		return dataSource;
	}

	@Bean(name = "redmineJdbcTemplate")
	public JdbcTemplate redmineJdbcTemplate() throws SQLException {
		return new JdbcTemplate(redmineDataSource());
	}

	/*
	 * ------------------------ DEVELOPMENT DATASOURCES --------------------------
	 */

	/*
	 * H2 - DEVELOPMENT IN-MEMORY DATABASE
	 */
	@Profile("h2")
	@Primary
	@Bean("dataSource")
	public DataSource h2DataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				// .addScript("classpath:sql/schema.sql")
				// .addScript("classpath:sql/init-data.sql")
				.build();
	}

	@Profile("h2")
	@Bean("sessionFactory")
	public SessionFactory h2SessionFactory() {
		DataSource dataSource = dataSource();
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		properties.put("hibernate.connection.url", "jdbc:h2:~/testdb");
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("connection.driver_class", "org.h2.Driver");
		properties.put("connection.username", "sa");
		properties.put("connection.password.", "");
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(properties);
		sessionBuilder.scanPackages("sbs.model");
		return sessionBuilder.buildSessionFactory();
	}

}
