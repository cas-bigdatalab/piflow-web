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

    private String groups;
    private String bundleDescription;
    private String inports;
    private String outports;
    private String owner;
    private String imageUrl;
    private List<StopsComponentPropertyVo> properties;

    //python component properties
    private Boolean isPythonComponent = false;
    private Boolean isHaveParams = false;
}
