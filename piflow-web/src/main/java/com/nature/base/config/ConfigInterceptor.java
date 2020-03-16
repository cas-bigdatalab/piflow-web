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
                    loadSample();
                    return false;
                }
            } else {
                if (!requestURI.startsWith(contextPath + "/bootPage")) {
                    response.sendRedirect(contextPath + "/bootPage/index"); // Redirect to the boot page
                    loadSample();
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

    private Boolean loadSample() throws FileNotFoundException {
        if ("h2".equals(profilesType)) {
            List<SysMenu> sampleMenuList = sysMenuMapper.getSampleMenuList();
            boolean loadExample1 = true;
            boolean loadExample2 = true;
            if (null != sampleMenuList && sampleMenuList.size() > 0) {
                for (SysMenu sysMenu : sampleMenuList) {
                    if ("Example1".equals(sysMenu.getMenuName())) {
                        loadExample1 = false;
                    } else if ("Example2".equals(sysMenu.getMenuName())) {
                        loadExample2 = false;
                    }
                }
            }
            //String storagePathHead = System.getProperty("user.dir") + "/static/sample/";
            //String storagePathHead = ResourceUtils.getURL("classpath:").getPath() + "/static/sample/";
            String storagePathHead = "static/sample/";
            System.out.println(storagePathHead);
            List<String> exampleNames = new ArrayList<>();
            if (loadExample1) {
                exampleNames.add("Example1");
            }
            if (loadExample2) {
                exampleNames.add("Example2");
            }
            for (int i = 0; i < exampleNames.size(); i++) {
                String exampleName = exampleNames.get(i);
                if (StringUtils.isBlank(exampleName)) {
                    return false;
                }
                //The XML file is read and returned according to the saved file path
                String xmlFileToStr = FileUtils.XmlFileToStrByRelativePath(storagePathHead + exampleName + ".xml");
                if (StringUtils.isBlank(xmlFileToStr)) {
                    //logger.warn("XML file read failed, loading template failed");
                    return false;
                }
                Flow flowXml = FlowXmlUtils.xmlToFlow(xmlFileToStr, 2, "system");
                if (null == flowXml) {
                    return false;
                }
                MxGraphModel mxGraphModel = flowXml.getMxGraphModel();
                if (null == mxGraphModel) {
                    return false;
                }
                List<MxCell> root = mxGraphModel.getRoot();
                if (null == root) {
                    root = new ArrayList<>();
                }
                List<MxCell> initMxCellList = MxCellUtils.initMxCell("system", mxGraphModel);
                root.addAll(initMxCellList);
                mxGraphModel.setRoot(root);

                flowXml.setId(null);
                flowXml.setName(exampleName);
                flowXml.setIsExample(true);
                flowXml.setMxGraphModel(mxGraphModel);
                flowXml = flowDomain.saveOrUpdate(flowXml);

                SysMenu sysMenu = new SysMenu();
                sysMenu.setCrtDttm(new Date());
                sysMenu.setCrtUser("system");
                sysMenu.setLastUpdateDttm(new Date());
                sysMenu.setLastUpdateUser("system");
                sysMenu.setMenuJurisdiction(SysRoleType.USER);
                sysMenu.setMenuParent("Example");
                sysMenu.setMenuName(flowXml.getName());
                sysMenu.setMenuDescription(flowXml.getName());
                sysMenu.setMenuUrl("/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=" + flowXml.getId());
                sysMenu.setMenuSort(500002 + i);
                sysMenuDomain.saveOrUpdate(sysMenu);
            }
            return true;
        }
        return false;
    }
}
