package com.wyjun.config;


import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

// 有了这个SpringConfig类，就不用编写spring.xml配置文件了。

// 用它标注表示一个配置类。代替spring.xml配置文件的。本身也会纳入IoC容器的管理，它本身也是一个Bean
@Configuration

// 组件包扫描   代替<context:component-scan base-package="com.wyjun"/>
@ComponentScan("com.wyjun")

// 将Mapper所有对应的隐式生成的动态代理类对象全部纳入IoC容器的管理
@MapperScan("com.wyjun.mapper")

// 启用自动代理，并且使用CGLIB的动态代理技术。代替<aop:aspectj-autoproxy proxy-target-class="true"/>
@EnableAspectJAutoProxy(proxyTargetClass = true)

// 开启事务注解支持，这是一个开关。(开启之后支持 @Transactional注解。)
@EnableTransactionManagement

// 引入配置文件，classpath:表示默认从类的根路径下开始查找
@PropertySource("classpath:application.properties")

public class SpringConfig {

     /* public DataSource dataSource()方法的作用
     作用：手动创建数据库连接池对象（HikariDataSource），交给Spring容器管理，给MyBatis使用。

     逐部分拆解:
     1.@Bean
     把这个方法返回的对象，注册成Spring容器中的Bean。
     之后Spring、MyBatis需要数据源的时候，直接从容器拿这个DataSource。

     2.方法参数上的@Value("${xxx}")
     从application.properties配置文件读取配置值，注入到方法参数。
     需要properties文件要有这几项内容：
     mybatis.data.source.driver=com.mysql.cj.jdbc.Driver
     mybatis.data.source.url=jdbc:mysql://localhost:3306/你的数据库名?useSSL=false...
     mybatis.data.source.username=root
     mybatis.data.source.password=数据库密码

     3.HikariDataSource
     HikariCP，Spring6默认的数据库连接池。
     >连接池：提前创建好一批数据库连接，程序来拿连接直接用，用完归还，不用每次新建销毁连接，提升性能。

     4.设置4个数据库核心参数
     -setDriverClassName：数据库驱动类名
     -setJdbcUrl：数据库连接地址（连接哪个库、哪个主机端口）
     -setUsername：数据库账号
     -setPassword：数据库密码

     5.return dataSource;返回连接池对象，交给Spring。

     6.和MyBatis的关系:
     之后写的SqlSessionFactoryBean，需要注入这个DataSource数据源。
     MyBatis通过这个DataSource连接MySQL，执行Mapper里面写的SQL。
     */

    //配置数据源
    @Bean
    public DataSource dataSource(
            @Value("${mybatis.data.source.driver}") String driverClassName,
            @Value("${mybatis.data.source.url}") String url,
            @Value("${mybatis.data.source.username}") String userName,
            @Value("${mybatis.data.source.password}") String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    /* public SqlSessionFactoryBean sqlSessionFactoryBean()方法的作用：
    这是Spring整合MyBatis的Java配置代码，手动创建SqlSessionFactoryBean，用来生成MyBatis核心对象SqlSessionFactory，交给Spring容器管理MyBatis。
    SqlSessionFactoryBean是MyBatis‑Spring提供的FactoryBean，Spring最终放入容器的不是这个Bean本身，而是它getObject()返回的SqlSessionFactory对象。

    各个参数含义：
    1.@Value("${mybatis.config.location}")：读取配置文件application.properties里mybatis.config.location的值，指定mybatis全局配置文件路径，例如mybatis-config.xml。
    2.DataSourcedataSource：Spring自动注入的数据源（数据库连接池），MyBatis通过它获取数据库连接。
    3.@Value("${mybatis.type.aliases.package}")：读取配置指定实体类包路径，开启别名，xml中写Account代替com.xxx.entity.Account。
    */
    //注意:因为上面已经纳入管理了，SpringIoC容器会自动注入方法参数 dataSource 。
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(
            @Value("${mybatis.config.location}") String configLocation,
            DataSource dataSource,
            @Value("${mybatis.type.aliases.package}") String typeAliasesPackage) {
        // 实例化工厂Bean
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置mybatis‑config.xml核心配置文件位置（classpath下）
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
        // 设置数据源：数据库连接池，MyBatis拿它操作数据库
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 设置实体类别名扫描包，mapper xml里可以直接写类名，不用写全限定类名
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);

        return sqlSessionFactoryBean;
    }

    /*public DataSourceTransactionManager dataSourceTransactionManager() {}方法的作用：
    作用：注册Spring的事务管理器Bean，用来管理数据库事务，支持@Transactional注解。

    逐段拆解：
    1.@Bean
    把方法返回的DataSourceTransactionManager对象交给Spring容器。

    2.参数DataSourcedataSource
    Spring自动把上面你写的Hikari数据源Bean注入进来。事务管理器必须绑定数据源，知道要管理哪一个数据库的事务。
    3.newDataSourceTransactionManager()DataSourceTransactionManager是SpringJDBC/MyBatis使用的事务管理器实现类。

    >专门用于普通JDBC、MyBatis技术；如果是JPA则用JpaTransactionManager。

    4.dataSourceTransactionManager.setDataSource(dataSource)
    把数据库连接池（数据源）设置给事务管理器。
    事务管理器要从这个数据源获取数据库连接，做：开启事务、提交事务、回滚事务。

    5.return返回对象，存入Spring容器。
    */

    //配置事务管理器。(注意:只有这个配置是不行的，你需要开启事务注解，这样 @Transactional才可以使用。因此需要再配置一个开关)
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        //DataSourceTransactionManager 是单机事务。不支持分布式事务管理。
        //支持分布式事务管理的事务管理器spring也实现了:JtaTransactionManager
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
