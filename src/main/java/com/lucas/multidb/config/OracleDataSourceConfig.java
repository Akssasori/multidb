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
    public DataSource oracleDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(oracleDriveClass);
        dataSource.setJdbcUrl(oracleUrl);
        dataSource.setUsername(oracleUserName);
        dataSource.setPassword(oraclePassword);
        return dataSource;
    }
    // data source serve para fornecer uma conexão com um banco de dados, usado para configurar as propriedades da conexão
    //com o banco

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

    // LocalContainerEntityManagerFactoryBean
    // vamos configurar a integração entre a camada de persistência(banco de dados) e a lógica da aplicação
    //

    @Primary
    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager oracleTransactionManager () {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(oracleEntityManagerFactory().getObject());
        return transactionManager;
    }

    // precisamos configurar o PlatformTransactionManager para cada banco porque ele é responsável por coordebnar as transações
    //consistência nas transações
    // ele consegue coordernar as transações separadamente
    //significa que as transações em um banco de dados não afetarão automaticamente as transações em outro, mantendo atomicidade dentro do escopo

}
