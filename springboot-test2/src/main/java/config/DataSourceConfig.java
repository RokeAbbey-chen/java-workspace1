package config;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.xqtv.paopao.dataaccess.dao" })
@EnableMongoRepositories(basePackages = { "com.xqtv.paopao.dataaccess.mongo.dao" })
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "db.master")
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDruidDataSource() {
        return new DruidDataSource();
    }

    @ConfigurationProperties(prefix = "db.slave")
    @Bean(name = "slaveDataSource")
    public DataSource slaveDruidDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dataSourceRW")
    public ReadWriteDataSourceRouter dataSourceRouting() {
        return new ReadWriteDataSourceRouter();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource(ReadWriteDataSourceRouter router) {
        LazyConnectionDataSourceProxy ds = new LazyConnectionDataSourceProxy();
        ds.setTargetDataSource(router);
        return ds;
    }

    @Bean(name = "entityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return entityManagerFactory(builder, dataSource).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
            DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.xqtv.paopao.dataaccess.domain").build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        JpaTransactionManager tx = new JpaTransactionManager(entityManagerFactory(builder, dataSource).getObject());
        tx.setJpaDialect(new HibernateJpaDialect());
        return tx;
    }

}
