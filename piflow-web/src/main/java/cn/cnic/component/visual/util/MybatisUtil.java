package cn.cnic.component.visual.util;

import cn.cnic.component.visual.mapper.piflowdb.DataBaseInfoMapper;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


/**
 * TODO
 * author:hmh
 * date:2023-11-10
 */
public class MybatisUtil {


    public static SqlSession getSqlSession(HikariDataSource dataSource){
        SqlSession sqlSession = null;
        try {
            MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean ();
            sqlSessionFactoryBean.setDataSource(dataSource);
            MybatisConfiguration configuration = new MybatisConfiguration();
//            configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
            configuration.setMapUnderscoreToCamelCase(true);
            //添加mapper
//            configuration.addMappers("com.nesdc.piflow_visual.mapper.piflowdb"); //这种方式打包运行，mapper添加出错
            configuration.addMapper(DataBaseInfoMapper.class);
            sqlSessionFactoryBean.setConfiguration(configuration);
            SqlSessionFactory sqlSessionFactory3 = sqlSessionFactoryBean.getObject();
            sqlSession = sqlSessionFactory3.openSession();
        } catch (Exception e) {
            //如果出错，关闭会话
            sqlSession.close();
            dataSource.close();
            e.printStackTrace();
        }
        return sqlSession;
    }

    //        DruidDataSource dataSource = new DruidDataSource();
//            dataSource.setDriverClassName(driverName);
//            dataSource.setUrl(url + "?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true");
//            dataSource.setUsername(userName);
//            dataSource.setPassword(password);
//            dataSource.setMaxWait(30000);
//            dataSource.setMaxActive(50);
//            dataSource.setMinIdle(30);
//            dataSource.setInitialSize(10000);

    public static HikariDataSource getDataSource(String driverName,String url,String userName,String password){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setJdbcUrl(url + "?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setAutoCommit(true);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(30000);
        dataSource.setMaximumPoolSize(1); //就设置1个连接,用完就关闭
        dataSource.setMaxLifetime(180000);//默认值30min
        return dataSource;
    }


}
