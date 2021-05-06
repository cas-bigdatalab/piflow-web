package cn.cnic.base.config;

import cn.cnic.base.util.LoggerUtil;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


@Configuration
@Primary //In the same "DataSource", first use the labeled "DataSource"
public class DataSourceConfig {

    private Logger logger = LoggerUtil.getLogger();

    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${sysParam.datasource.type}")
    private String profilesType;
    
    @Bean //Declare it as a bean instance
    public DataSource getDataSource() {
        createDatabase();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(datasourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    
    private void createDatabase() {
        if("mysql".equals(profilesType)) {
            try {
                Class.forName(driverClassName);
                //jdbc:mysql://127.0.0.1:3306/insight?useUnicode=true&characterEncoding=utf8&failOverReadOnly=false&allowMultiQueries=true
                String url01 = datasourceUrl.substring(0, datasourceUrl.indexOf("?"));

                String url02 = url01.substring(0, url01.lastIndexOf("/"));

                String datasourceName = url01.substring(url01.lastIndexOf("/") + 1);
                // Connect to existing databases, such as MySQL
                Connection connection = DriverManager.getConnection(url02, username, password);
                Statement statement = connection.createStatement();

                // Create a database
                statement.executeUpdate("create database if not exists `" + datasourceName + "` default character set utf8 COLLATE utf8_general_ci");

                statement.close();
                connection.close();
            } catch (Exception e) {
                logger.error("error",e);
            }
        }
    }
}

