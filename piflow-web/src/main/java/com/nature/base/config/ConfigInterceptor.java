package com.nature.base.config;

import com.nature.base.util.FileUtils;
import com.nature.base.util.FlowXmlUtils;
import com.nature.common.Eunm.SysRoleType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.utils.MxCellUtils;
import com.nature.component.system.model.SysInitRecords;
import com.nature.component.system.model.SysMenu;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.system.SysInitRecordsDomain;
import com.nature.domain.system.SysMenuDomain;
import com.nature.mapper.system.SysMenuMapper;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Defining interceptors
 */
@Component
@Log4j
public class ConfigInterceptor implements HandlerInterceptor {

    @Value("${sysParam.datasource.type}")
    private String profilesType;

    @Resource
    private SysInitRecordsDomain sysInitRecordsDomain;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private SysMenuDomain sysMenuDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = (null == SysParamsCache.SYS_CONTEXT_PATH ? "" : SysParamsCache.SYS_CONTEXT_PATH);
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(contextPath + "/error")) {
            return true;
        }
        if (requestURI.startsWith(contextPath + "/login")) {
            return true;
        }
        // Determine if the boot flag is true
        if (!SysParamsCache.IS_BOOT_COMPLETE) {
            // Query is boot record
            SysInitRecords sysInitRecordsLastNew = sysInitRecordsDomain.getSysInitRecordsLastNew(1);
            if (null != sysInitRecordsLastNew && sysInitRecordsLastNew.getIsSucceed()) {
                SysParamsCache.setIsBootComplete(true);
                if (requestURI.startsWith(contextPath + "/bootPage")) {
                    response.sendRedirect(contextPath); // Redirect to the boot page
                    return false;
                }
            } else {
                if (!requestURI.startsWith(contextPath + "/bootPage")) {
                    response.sendRedirect(contextPath + "/bootPage/index"); // Redirect to the boot page
                    return false;
                }
                log.info("No initialization, enter the boot page");
            }
        } else if (requestURI.startsWith(contextPath + "/bootPage")) {
            response.sendRedirect(contextPath); // Redirect to the boot page
            return false;
        }
        return true;
    }
}
