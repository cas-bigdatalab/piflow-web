package cn.cnic.component.sparkJar.domain;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.sparkJar.entity.SparkJarComponent;
import cn.cnic.component.sparkJar.mapper.SparkJarMapper;
import cn.cnic.component.sparkJar.mapper.provider.SparkJarMapperProvider;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SparkJarDomain {

    @Autowired
    private SparkJarMapper sparkJarMapper;

    /**
     * add SparkJarComponent
     *
     * @param sparkJarComponent
     * @return
     */
    public int addSparkJarComponent(SparkJarComponent sparkJarComponent) {
        return sparkJarMapper.addSparkJarComponent(sparkJarComponent);
    }

    /**
     * update SparkJarComponent
     *
     * @param sparkJarComponent
     * @return
     */
    public int updateSparkJarComponent(SparkJarComponent sparkJarComponent) {
        return sparkJarMapper.updateSparkJarComponent(sparkJarComponent);
    }

    /**
     * query all SparkJarComponent
     *
     * @return
     */
    public List<SparkJarComponent> getSparkJarList(String username, boolean isAdmin) {
        return sparkJarMapper.getSparkJarList(username, isAdmin);
    }

    public List<SparkJarComponent> getSparkJarListByName(String username, boolean isAdmin, String jarName) {
        return sparkJarMapper.getSparkJarListByName(username, isAdmin, jarName);
    }

    @SelectProvider(type = SparkJarMapperProvider.class, method = "getSparkJarById")
    public SparkJarComponent getSparkJarById(String username, boolean isAdmin, String id) {
        return sparkJarMapper.getSparkJarById(username, isAdmin, id);
    }

    public int deleteSparkJarById(String username, String id) {
        return sparkJarMapper.deleteSparkJarById(username, id);
    }

    public List<SparkJarComponent> getSparkJarListParam(String username, boolean isAdmin, String param) {
        return sparkJarMapper.getSparkJarListParam(username, isAdmin, param);
    }

}
