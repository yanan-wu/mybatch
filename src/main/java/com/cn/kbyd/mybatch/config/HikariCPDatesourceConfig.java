package com.cn.kbyd.mybatch.config;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.cn.kbyd.mybatch.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
@PropertySource("classpath:datasource.properties")
public class HikariCPDatesourceConfig {

    @Value("${applicationName}")
    private String applicationName;

    @Value("${hikariCP.driverClass:com.mysql.cj.jdbc.Driver}")
    private String driverClass;

    @Value("${hikariCP.jdbcUrl}")
    private String jdbcUrl;

    @Value("${hikariCP.user}")
    private String user;

    @Value("${hikariCP.password}")
    private String password;

    @Value("${hikariCP.minimumIdle:3}")
    private int minimumIdle;

    @Value("${hikariCP.maxPoolSize:10}")
    private int maxPoolSize;

    @Value("${hikariCP.connectionTestQuery:select 1 from dual}")
    private String connectionTestQuery;

    @Value("${hikariCP.validationTimeout:5000}")
    private long validationTimeout;

    @Value("${hikariCP.maxLifetime:600000}")
    private long maxLifetime;

    @Value("${hikariCP.connectionTimeout:300000}")
    private long connectionTimeout;

    @Value("${hikariCP.readOnly:false}")
    private boolean readOnly;

    @Value("${hikariCP.idleTimeout:600000}")
    private long idleTimeout;


    @Bean(value = "dataSource", destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        if(StringUtils.isNotEmpty(driverClass)) {
            config.setDriverClassName(driverClass);
        }

        // 连接池名称，用于日志打印或JMX监听
        config.setPoolName(applicationName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);

        // 最小空闲连接数
        config.setMinimumIdle(minimumIdle);

        // 最大连接数，推荐的公式：((core_count * 2) + effective_spindle_count)
        // https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing
        config.setMaximumPoolSize(maxPoolSize);

        // 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException
        config.setConnectionTimeout(connectionTimeout);

        // 连接心跳测试语句
        config.setConnectionTestQuery(connectionTestQuery);

        // 是否只读，连接只读数据库时配置为true
        config.setReadOnly(readOnly);

        // 一个连接idle状态的最大时长（毫秒），超时则被释放
        config.setIdleTimeout(idleTimeout);

        // 一个连接的生命时长（毫秒），超时而且没被使用则被释放
        // 建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';）
        config.setMaxLifetime(maxLifetime);

        //连接的心跳检查间隔（毫秒）
        config.setValidationTimeout(validationTimeout);

        // 开启jmx监听Mbeans
        //config.setRegisterMbeans(true);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }


    @Bean(value = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    @Autowired
    @Qualifier("masterPageInterceptor")
    PageInterceptor pageInterceptor;

    @Bean(value = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.cn.kbyd.dal.com.cn.kbyd.mybatch.mapper");
        String[] mapperLocations = new String[1];
        mapperLocations[0] = "classpath*:com.cn.kbyd.mybatch.mapper/**/*Mapper.xml";
        sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations(mapperLocations));
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        return sqlSessionFactoryBean.getObject();
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<Resource>();
        if (mapperLocations != null) {
            for (String mapperLocation : mapperLocations) {
                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {

                }
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }


    @Bean(value = "transactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        return transactionTemplate;
    }

//    @Bean(value = "transactionManager")
//    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
//        dataSourceTransactionManager.setDataSource(dataSource);
//        return dataSourceTransactionManager;
//    }
}
