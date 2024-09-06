package cn.cnic.base.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicPropertyUpdater {

    @Autowired
    private ConfigurableEnvironment env;

    public void updateProperty(String key, String value) {
        MutablePropertySources propertySources = env.getPropertySources();
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        propertySources.addFirst(new MapPropertySource("dynamicProperties", map));
    }

    public String getProperty(String key) {
        return env.getProperty(key);
    }
}
