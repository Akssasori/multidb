package com.lucas.multidb.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource("classpath:application.yml")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager",
        basePackages = {"com.lucas.multidb.oracle.repository"}
)
public class OracleDataSourceConfig {

    @Value("${spring.datasource.oracle.username}")
    private String oracleUserName;

    @Value("${spring.datasource.oracle.password}")
    private String oraclePassword;

    @Value("${spring.datasource.oracle.jdbcUrl}")
    private String oracleUrl;

    @Value("${spring.datasource.oracle.driver-class-name}")
    private String oracleDriveClass;

    @Bean(name = "oracleDataSource")
    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(oracleDriveClass);
        dataSource.setJdbcUrl(oracleUrl);
        dataSource.setUsername(oracleUserName);
        dataSource.setPassword(oraclePassword);
        return dataSource;
    }

    @Primary
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        managerFactoryBean.setDataSource(oracleDataSource()); // setando um datasource
        managerFactoryBean.setPersistenceUnitName("oracle"); // noma para unidade de persistência
        managerFactoryBean.setPackagesToScan("com.lucas.multidb.oracle.model"); // rastreiar o models
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        managerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.Oracle12cDialect");
        managerFactoryBean.setJpaPropertyMap(properties);

        return managerFactoryBean;
    }

    @Primary
    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager oracleTransactionManager () {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(oracleEntityManagerFactory().getObject());
        return transactionManager;
    }


}
