//package com.nature.base.config;
//
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import com.nature.base.util.LoggerUtil;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//
//
//@Configuration
//@Primary //In the same "DataSource", first use the labeled "DataSource"
//public class DataSourceConfig {
//
//    private Logger logger = LoggerUtil.getLogger();
//
//    @Value("${spring.datasource.url}")
//    //jdbc:mysql://127.0.0.1:3306/insight?useUnicode=true&characterEncoding=utf8&failOverReadOnly=false&allowMultiQueries=true
//    private String datasourceUrl;
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//    @Value("${spring.datasource.username}")
//    private String username;
//    @Value("${spring.datasource.password}")
//    private String password;
//    @Value("sysParam.whitelist")
//    private String whitelist;
//    @Value("sysParam.blacklist")
//    private String blacklist;
//    @Value("sysParam.druidUser")
//    private String druidUser;
//    @Value("sysParam.druidPassword")
//    private String druidPassword;
//
//    /**
//     *  Configuration processing of WEB monitoring
//     */
//    @Bean
//    public ServletRegistrationBean druidServlet() {
//        // Now we're going to do configuration processing for'druid'monitoring
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
//                new StatViewServlet(), "/druid/*");
//        // White List, with multiple commas, allows all access if'allow'is not configured or empty
//        servletRegistrationBean.addInitParameter("allow", whitelist);
//        // Blacklists, multiple commas (when co-exist,'deny'takes precedence over'allow')
//        servletRegistrationBean.addInitParameter("deny", blacklist);
//        // Console Management User Name
//        servletRegistrationBean.addInitParameter("loginUsername", druidUser);
//        // Console Management Password
//        servletRegistrationBean.addInitParameter("loginPassword", druidPassword);
//        // Can you reset the data source and disable the "Reset All" function on HTML pages?
//        servletRegistrationBean.addInitParameter("resetEnable", "false");
//        return servletRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new WebStatFilter());
//        //All requests are monitored and processed
//        filterRegistrationBean.addUrlPatterns("/*");
//        //Add format information that you don't need to ignore
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.css,/druid/*");
//        return filterRegistrationBean;
//    }
//
//    @Bean     //Declare it as a bean instance
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        try {
//            Class.forName(driverClassName);
//
//            String url01 = datasourceUrl.substring(0, datasourceUrl.indexOf("?"));
//
//            String url02 = url01.substring(0, url01.lastIndexOf("/"));
//
//            String datasourceName = url01.substring(url01.lastIndexOf("/") + 1);
//            // Connect to existing databases, such as MySQL
//            Connection connection = DriverManager.getConnection(url02, username, password);
//            Statement statement = connection.createStatement();
//
//            // Create a database
//            statement.executeUpdate("create database if not exists `" + datasourceName + "` default character set utf8 COLLATE utf8_general_ci");
//
//            statement.close();
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        DruidDataSource datasource = new DruidDataSource();
//        return datasource;
//    }
//
//}
