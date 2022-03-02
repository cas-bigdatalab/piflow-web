package cn.cnic.component.flow.domain;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.CustomizedProperty;
import cn.cnic.component.flow.mapper.CustomizedPropertyMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class CustomizedPropertyDomain extends StopsDomain{

    private final CustomizedPropertyMapper customizedPropertyMapper;

    @Autowired
    public CustomizedPropertyDomain(CustomizedPropertyMapper customizedPropertyMapper) {
        this.customizedPropertyMapper = customizedPropertyMapper;
    }

    public int addCustomizedProperty(CustomizedProperty customizedProperty) throws Exception {
        if (null == customizedProperty) {
            throw new Exception("save failed");
        }
        String id = customizedProperty.getId();
        if (StringUtils.isBlank(id)) {
            customizedProperty.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = customizedPropertyMapper.addCustomizedProperty(customizedProperty);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int updateEnableFlagByStopId(String username, String id){
        return customizedPropertyMapper.updateEnableFlagByStopId(username, id);
    }

    public int updateCustomizedPropertyCustomValue(String username, String content, String id){
        return customizedPropertyMapper.updateCustomizedPropertyCustomValue(username, content, id);
    }

    public CustomizedProperty getCustomizedPropertyById(String id){
        return customizedPropertyMapper.getCustomizedPropertyById(id);
    }

    public List<CustomizedProperty> getCustomizedPropertyListByStopsIdAndName(String stopsId, String name){
       return customizedPropertyMapper.getCustomizedPropertyListByStopsIdAndName(stopsId, name);
    }
    
}
