package sbs.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import sbs.service.CustomUserDetailsService;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	
	@Profile("postgres")
	@Bean("dataSource")
	public DataSource postgreDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(System.getenv("JDBC_DATABASE_URL"));
		dataSource.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
		dataSource.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
		return dataSource;
	}
	@Profile("postgres")
	@Bean("sessionFactory")
	public SessionFactory postgreSessionFactory() {
		DataSource dataSource = postgreDataSource();
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "update");
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);	
		sessionBuilder.addProperties(properties);
		sessionBuilder.scanPackages("sbs.model");
		return sessionBuilder.buildSessionFactory();
	}
	@Profile("mysl")
	@Bean
	  public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://localhost:3306/sappdb");
	    dataSource.setUsername("root");
	    dataSource.setPassword("admin");

	    return dataSource;
	  }
	@Profile("mysl")
	@Bean
	public SessionFactory sessionFactory() {
		DataSource dataSource = dataSource();
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.hbm2ddl.auto", "create");
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);	
		sessionBuilder.addProperties(properties);
		sessionBuilder.scanPackages("sbs.model");
		return sessionBuilder.buildSessionFactory();
	}
	@Profile("h2")
	@Bean("dataSource")
	public DataSource h2DataSource(){
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				//.addScript("classpath:sql/schema.sql")
				//.addScript("classpath:sql/init-data.sql")
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
	
	@Bean
	public HibernateTransactionManager getTransactionManager() {
		SessionFactory sessionFactory = sessionFactory();
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}
	
	@Bean
	@Autowired
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService){
		/*JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
    	jdbcImpl.setDataSource(dataSource());
    	jdbcImpl.setUsersByUsernameQuery("select username, password, enabled from users where lower(username)=lower(?)");
    	jdbcImpl.setAuthoritiesByUsernameQuery("select b.username, a.role from user_roles a, users b where lower(b.username)=lower(?) and a.userid=b.userid");
    	return jdbcImpl;
    	*/
		return customUserDetailsService;
    }
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource());
		return db;
	}
}
