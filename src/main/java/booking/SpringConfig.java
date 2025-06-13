package booking;

import java.util.*;

import javax.persistence.*;
import javax.sql.*;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.jdbc.datasource.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.*;
import org.springframework.transaction.*;
import org.springframework.transaction.annotation.*;

@Configuration
@ComponentScan("booking")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "booking.data")
public class SpringConfig {
	// Use simple database - stored in your home dir: ~/bookings/db
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:file:~/bookings/db");
		ds.setUsername("sa");
		ds.setPassword("");
		return ds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(ds);
		emf.setPackagesToScan("booking.data");
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties props = new Properties();
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

		emf.setJpaProperties(props);
		return emf;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}