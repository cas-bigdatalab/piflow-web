package cn.cnic.third.service;

import cn.cnic.third.vo.sparkJar.SparkJarVo;
import cn.cnic.third.vo.stop.StopsHubVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;

import java.util.List;
import java.util.Map;

public interface ISparkJar {

    public String getSparkJarPath();

    public SparkJarVo mountSparkJar(String sparkJarName);

    public SparkJarVo unmountSparkJar(String sparkJarMountId);

}
