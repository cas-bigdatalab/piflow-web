package com.nature.base.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.LoggerUtil;

@Configuration
public class MybatisTableConfig {

	Logger logger = LoggerUtil.getLogger();

	@Value("${spring.datasource.driver-class-name}")
	private String driver;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean
	public DruidDataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxActive(30);
		dataSource.setInitialSize(10);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestOnBorrow(true);
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}

//	@Bean
//	public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(dataSource());
//		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		sqlSessionFactoryBean.setMapperLocations(
//				resolver.getResources("classpath*:com/gitee/sunchenbin/mybatis/actable/mapping/*/*.xml"));
//		sqlSessionFactoryBean.setTypeAliasesPackage("com.nature.**.model.*");
//		return sqlSessionFactoryBean;
//	}

	/**
	 * 分页插件
	 *
	 * @param dataSource
	 * @return
	 * @author SHANHY
	 * @create 2016年2月18日
	 */
	@Bean
	public PageHelper pageHelper(DataSource dataSource) {
		logger.info("注册MyBatis分页插件PageHelper");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}

}
