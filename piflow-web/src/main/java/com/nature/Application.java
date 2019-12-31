package com.nature;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@PropertySources({
        @PropertySource(value = "classpath:apiConfig.properties", encoding = "utf-8"),
        @PropertySource(value = "classpath:baseConfig.properties", encoding = "utf-8")
})
@MapperScan(basePackages = "com.nature.mapper.*.*")
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        createDatabase();
        ApplicationContext context = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(context);
        logger.warn("***************************************************************");
        logger.warn("***************** Spring Boot Startup Success *****************");
        logger.warn("***************************************************************");
    }

    @SuppressWarnings("resource")
	public static void createDatabase() {
        String path = System.getProperty("user.dir") + "/src/main/resources/application.properties";
        String path_msql = System.getProperty("user.dir") + "/src/main/resources/application-mysql.properties";
        String driverClassName = null;
        String username = null;
        String password = null;
        String datasourceUrl = null;
        try {
            File srcFile = new File(path);
            if (!srcFile.exists()) {
                return;
            }
            if (!srcFile.isFile()) {
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(path));
            String temp = null;
            Boolean flag = false;
            while ((temp = br.readLine()) != null) {//readLine()每次读取一行
                if ("spring.profiles.active=mysql".equals(temp.replaceAll(" ", ""))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return;
            }
            File srcFile_mysql = new File(path_msql);
            if (!srcFile_mysql.exists()) {
                return;
            }
            if (!srcFile_mysql.isFile()) {
                return;
            }
            BufferedReader br_mysql = new BufferedReader(new FileReader(path_msql));
            temp = null;
            flag = false;
            while ((temp = br_mysql.readLine()) != null) {//readLine()每次读取一行
                String temp_trim = temp.replaceAll(" ", "");
                if (temp_trim.contains("spring.datasource.driver-class-name")) {
                    String[] split = temp_trim.split("spring.datasource.driver-class-name=");
                    if (split.length != 2) {
                        return;
                    }
                    driverClassName = split[1];
                } else if (temp_trim.contains("spring.datasource.url")) {
                    String[] split = temp_trim.split("spring.datasource.url=");
                    if (split.length != 2) {
                        return;
                    }
                    datasourceUrl = split[1];
                } else if (temp_trim.contains("spring.datasource.username")) {
                    String[] split = temp_trim.split("spring.datasource.username=");
                    if (split.length != 2) {
                        return;
                    }
                    username = split[1];
                } else if (temp_trim.contains("spring.datasource.password")) {
                    String[] split = temp_trim.split("spring.datasource.password=");
                    if (split.length != 2) {
                        return;
                    }
                    password = split[1];
                }
            }
            if ("com.mysql.jdbc.Driver".equals(driverClassName)) {
                try {
                    Class.forName(driverClassName);

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
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
