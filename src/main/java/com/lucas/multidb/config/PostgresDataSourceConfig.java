package com.lucas.multidb.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager",
        basePackages = {"com.lucas.multidb.postgres.repository"}
)
public class PostgresDataSourceConfig {

    @Value("${spring.datasource.postgres.username}")
    private String postgresUserName;

    @Value("${spring.datasource.postgres.password}")
    private String postgresPassword;

    @Value("${spring.datasource.postgres.jdbcUrl}")
    private String postgresUrl;

    @Value("${spring.datasource.postgres.driver-class-name}")
    private String postgresDriveClass;

    @Bean(name = "postgresDataSource")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource postgresDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(postgresDriveClass);
        dataSource.setJdbcUrl(postgresUrl);
        dataSource.setUsername(postgresUserName);
        dataSource.setPassword(postgresPassword);
        return dataSource;
    }

//    @Primary
    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        managerFactoryBean.setDataSource(postgresDataSource()); // setando um datasource
        managerFactoryBean.setPersistenceUnitName("oracle"); // noma para unidade de persistência
        managerFactoryBean.setPackagesToScan("com.lucas.multidb.oracle.model"); // rastreiar o models
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        managerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        managerFactoryBean.setJpaPropertyMap(properties);

        return managerFactoryBean;
    }

//    @Primary
    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager () {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(postgresEntityManagerFactory().getObject());
        return transactionManager;
    }


}
