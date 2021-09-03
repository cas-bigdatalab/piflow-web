package cn.cnic.component.dashboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.ReturnMapUtils;
import net.sf.json.JSONObject;

public class ResourceServiceTest  extends ApplicationTests {

    @Autowired
    private IResourceService resourceServiceImpl;

    @Test
    @Rollback(false)
    public void testGetResourceInfo() {
        String result = resourceServiceImpl.getResourceInfo();

        JSONObject resourceInfoObj = JSONObject.fromObject(result);
        String str = ReturnMapUtils.setSucceededCustomParamRtnJsonStr("resourceInfo", resourceInfoObj);
        System.out.println(str);

    }
}
