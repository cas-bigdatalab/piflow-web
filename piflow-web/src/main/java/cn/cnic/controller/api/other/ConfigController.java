package cn.cnic.controller.api.other;

import cn.cnic.base.utils.DynamicPropertyUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private DynamicPropertyUpdater dynamicPropertyUpdater;

    @PostMapping("/update")
    public String updateConfig(@RequestParam String key, @RequestParam String value) {
        dynamicPropertyUpdater.updateProperty(key, value);
        return "Configuration updated: " + key + " = " + value;
    }

    @GetMapping("/get")
    public String getConfig(@RequestParam String key) {
        return "Current value: " + key + " = " + dynamicPropertyUpdater.getProperty(key);
    }
}
