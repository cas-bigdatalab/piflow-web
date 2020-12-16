package cn.cnic.third.service;

import cn.cnic.third.vo.sparkJar.SparkJarVo;

public interface ISparkJar {

    public String getSparkJarPath();

    public SparkJarVo mountSparkJar(String sparkJarName);

    public SparkJarVo unmountSparkJar(String sparkJarMountId);

}
