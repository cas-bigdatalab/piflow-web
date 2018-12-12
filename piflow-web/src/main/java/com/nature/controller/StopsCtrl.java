package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.service.IPropertyService;
import com.nature.component.workFlow.service.IStopsService;
import com.nature.component.workFlow.vo.StopsVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stops")
public class StopsCtrl {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    IPropertyService propertyServiceImpl;

    @Autowired
    IStopsService stopsServiceImpl;

    @RequestMapping("/queryIdInfo")
    public StopsVo getStopGroup(String fid, String id) {
        if (StringUtils.isNotBlank(fid) && StringUtils.isNotBlank(id)) {
            StopsVo queryInfo = propertyServiceImpl.queryAll(fid, id);
            if (null != queryInfo) {
                //对比stops模板属性并作出修改
                //propertyServiceImpl.checkStopTemplateUpdate(queryInfo.getId());
                return queryInfo;
            }
        }
        return null;
    }

    /**
     * 多个一保存起修改
     *
     * @param content
     * @param id
     * @return
     */
    @RequestMapping("/updateStops")
    public Integer updateStops(String[] content, String id) {
        int updateStops = 0;
        if (null != content && content.length > 0) {
            for (String string : content) {
                //使用#id#标记来截取数据,第一为内容，第二个为要修改记录的id
                String[] split = string.split("#id#");
                if (null != split && split.length == 2) {
                    String updateContent = split[0];
                    String updateId = split[1];
                    updateStops = propertyServiceImpl.updateProperty(updateContent, updateId);
                }
            }
        }
        if (updateStops > 0) {
            logger.info("stops属性修改成功:" + updateStops);
            return updateStops;
        } else {
            return 0;
        }
    }

    @RequestMapping("/updateStopsOne")
    public String updateStops(String content, String id) {
    	Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        int updateStops = 0;
        updateStops = propertyServiceImpl.updateProperty(content, id);
        if (updateStops > 0) {
        	 rtnMap.put("code", "1");
             rtnMap.put("errMsg", "保存成功");
             rtnMap.put("value", content);
            logger.info("stops属性修改成功:" + updateStops);
        } else {
        	   rtnMap.put("errMsg", "数据库保存失败");
               logger.info("数据库保存失败");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/updateStopsById")
    public String updateStopsById(HttpServletRequest request) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String id = request.getParameter("stopId");
        String isCheckpointStr = request.getParameter("isCheckpoint");
        if (!StringUtils.isAnyEmpty(id, isCheckpointStr)) {
            boolean isCheckpoint = false;
            if ("1".equals(isCheckpointStr)) {
                isCheckpoint = true;
            }
            int updateStopsCheckpoint = stopsServiceImpl.updateStopsCheckpoint(id, isCheckpoint);
            if(updateStopsCheckpoint>0){
                rtnMap.put("code", "1");
                rtnMap.put("errMsg", "保存成功");
                logger.info("保存成功");
            }else {
                rtnMap.put("errMsg", "数据库保存失败");
                logger.info("数据库保存失败");
            }
        }else {
            rtnMap.put("errMsg", "传入参数有空的");
            logger.info("传入参数有空的");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

}
