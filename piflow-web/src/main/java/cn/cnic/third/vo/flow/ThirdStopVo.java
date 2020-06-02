package cn.cnic.third.vo.flow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ThirdStopVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;
    private String name;
    private String bundle;
    private Map<String, Object> properties = new HashMap<String, Object>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
