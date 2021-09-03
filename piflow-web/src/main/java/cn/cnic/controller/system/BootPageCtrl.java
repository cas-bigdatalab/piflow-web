package cn.cnic.controller.system;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.ISysInitRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/bootPage")
public class BootPageCtrl {

    @Autowired
    private ISysInitRecordsService sysInitRecordsServiceImpl;

    @RequestMapping(value = "/isInBootPage", method = RequestMethod.GET)
    @ResponseBody
    public String isInBootPage() {
        boolean inBootPage = sysInitRecordsServiceImpl.isInBootPage();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("isIn", inBootPage);
    }

    @RequestMapping(value = "/initComponents", method = RequestMethod.GET)
    @ResponseBody
    public String initComponents() {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return sysInitRecordsServiceImpl.initComponents(currentUsername);
    }

    @RequestMapping(value = "/threadMonitoring", method = RequestMethod.GET)
    @ResponseBody
    public String threadMonitoring() {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return sysInitRecordsServiceImpl.threadMonitoring(currentUsername);
    }


}
