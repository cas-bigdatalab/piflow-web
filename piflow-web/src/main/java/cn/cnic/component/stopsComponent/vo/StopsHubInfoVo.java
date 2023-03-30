package cn.cnic.component.stopsComponent.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
//TODO
@Setter
@Getter
public class StopsHubInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String stopHubId;
    private String stopBundle;
    private String useInstructions;
    private String description;
    private String author;
    private String authorEmail;
    private String teamName;
    private String chineseName;
    private String stopName;


    //Python组件查询单个算法详情,返回给前台,以下是python特有的字段
    private String groups;              //所属组
    private String bundleDescription;         //组件描述
    private String inports;
    private String outports;
    private String owner;
    private String imageUrl;
    private Boolean isComponent = false;        //是否是Python组件
    private Boolean isHaveParams = false;           //Python脚本是否有参数
    private List<StopsComponentPropertyVo> properties;     //对应的python属性值集合
}
