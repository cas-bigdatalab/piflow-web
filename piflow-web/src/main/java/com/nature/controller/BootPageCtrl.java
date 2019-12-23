package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.dataSource.service.IDataSource;
import com.nature.component.dataSource.vo.DataSourceVo;
import com.nature.component.group.service.IStopGroupService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bootPage")
public class BootPageCtrl {
    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @RequestMapping("/initPage")
    public ModelAndView initPage(ModelAndView modelAndView) {
        return new ModelAndView("initPage");
    }

    @RequestMapping("/loadStops")
    @ResponseBody
    public String loadStops(ModelAndView modelAndView) {
        UserVo user = SessionUserUtil.getCurrentUser();
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        stopGroupServiceImpl.addGroupAndStopsList(user);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }


}
