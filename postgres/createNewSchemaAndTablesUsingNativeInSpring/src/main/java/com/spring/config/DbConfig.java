package com.spring.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration @EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "configEntityManagerFactory",
    transactionManagerRef = "configTransactionManager",
    basePackages = {"com.spring.dao"})
public class DbConfig {

    @Value("${db.user}") private String user;

    @Value("${db.password}") private String password;

    @Value("${postgresIP}") private String postgresIP;

    @Value("${db.name}") private String dbName;

    @Bean(name = "configDataSource") public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl("jdbc:postgresql://" + postgresIP + ":5432/" + dbName);
        driverManagerDataSource.setUsername(user);
        driverManagerDataSource.setPassword(password);
        return driverManagerDataSource;
    }

    @Bean(name = "configEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean configEntityManagerFactory(
        EntityManagerFactoryBuilder builder, @Qualifier("configDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return builder.dataSource(dataSource).properties(properties)
            .packages("com.spring.entity").persistenceUnit("Admin").build();
    }

    @Bean(name = "configTransactionManager")
    public PlatformTransactionManager configTransactionManager(
        @Qualifier("configEntityManagerFactory") EntityManagerFactory configEntityManagerFactory) {
        return new JpaTransactionManager(configEntityManagerFactory);
    }
}
