package com.nature.controller;

import com.nature.base.util.FlowXmlUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.DrawingBoardType;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.service.IFlowGroupService;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.vo.StopGroupVo;
import com.nature.component.group.service.IStopGroupService;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.service.IMxGraphModelService;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * grapheditorctrl
 */
@Controller
@RequestMapping("/mxGraph")
public class MxGraphCtrl {
    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IFlowService flowServiceImpl;

    @Resource
    private IFlowGroupService flowGroupServiceImpl;

    @Resource
    private IStopGroupService stopGroupServiceImpl;

    @Resource
    private IMxGraphModelService mxGraphModelServiceImpl;

    /**
     * Enter the front page of the drawing board
     *
     * @param request
     * @param model
     * @param drawingBoardType
     * @return
     */
    @RequestMapping("/drawingBoard")
    public String drawingBoard(HttpServletRequest request, Model model, DrawingBoardType drawingBoardType) {
        String load = request.getParameter("load");
        //set parentAccessPath
        String parentAccessPath = request.getParameter("parentAccessPath");
        model.addAttribute("parentAccessPath", parentAccessPath);
        // set current user
        UserVo user = SessionUserUtil.getCurrentUser();
        model.addAttribute("currentUser", user);
        //set drawingBoardType
        model.addAttribute("drawingBoardType", drawingBoardType);

        String pagePath = "errorPage";
        // Determine whether there is an'id'('load') of'Flow', and if there is, load it, otherwise generate'UUID' to return to the return page.
        if (StringUtils.isBlank(load)) {
            return "errorPage";
        }
        switch (drawingBoardType) {
            case FLOW: {
                Model flowHandleModel = flowHandle(model, load);
                if (null != flowHandleModel) {
                    model = flowHandleModel;
                    pagePath = "mxGraph/index";
                }
                break;
            }
            case GROUP: {
                Model groupHandleModel = groupHandle(model, load);
                if (null != groupHandleModel) {
                    model = groupHandleModel;
                    pagePath = "mxGraph/index";
                }
                break;
            }
            case TASK: {
                Model taskHandleModel = taskHandle(model, load);
                if (null != taskHandleModel) {
                    model = taskHandleModel;
                    pagePath = "grapheditor/index";
                }
                break;
            }
        }
        return pagePath;
    }

    private Model flowHandle(Model model, String load) {
        if (StringUtils.isBlank(load)) {
            return null;
        }
        if (null == model) {
            return null;
        }
        return model;
    }

    private Model groupHandle(Model model, String load) {
        if (StringUtils.isBlank(load)) {
            return null;
        }
        if (null == model) {
            return null;
        }
        // Query by loading'id'
        FlowGroup flowGroupById = flowGroupServiceImpl.getFlowGroupById(load);
        if (null == flowGroupById) {
            return null;
        }
        String maxStopPageId = flowServiceImpl.getMaxFlowPageIdByFlowGroupId(load);
        //'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
        maxStopPageId = StringUtils.isBlank(maxStopPageId) ? "2" : (Integer.parseInt(maxStopPageId) + 1) + "";
        model.addAttribute("maxStopPageId", maxStopPageId);
        MxGraphModelVo mxGraphModelVo = null;
        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
        // Change the query'mxGraphModelVo'to'XML'
        String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
        model.addAttribute("xmlDate", loadXml);
        model.addAttribute("load", load);
        model.addAttribute("isExample", (null == flowGroupById.getIsExample() ? false : flowGroupById.getIsExample()));
        return model;
    }

    private Model taskHandle(Model model, String load) {
        if (null == model) {
            return null;
        }
        // Query by loading'id'
        Flow flowById = flowServiceImpl.getFlowById(load);
        if (null == flowById) {
            return null;
        }
        if (null != flowById.getFlowGroup()) {
            String flowGroupId = flowById.getFlowGroup().getId();
            model.addAttribute("flowGroupId", flowGroupId);
        }
        // Group on the left and'stops'
        List<StopGroupVo> groupsVoList = stopGroupServiceImpl.getStopGroupAll();
        model.addAttribute("groupsVoList", groupsVoList);
        String maxStopPageId = flowServiceImpl.getMaxStopPageId(load);
        //'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
        maxStopPageId = StringUtils.isBlank(maxStopPageId) ? "2" : (Integer.parseInt(maxStopPageId) + 1) + "";
        model.addAttribute("maxStopPageId", maxStopPageId);
        MxGraphModelVo mxGraphModelVo = null;
        MxGraphModel mxGraphModel = flowById.getMxGraphModel();
        mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
        // Change the query'mxGraphModelVo'to'XML'
        String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
        model.addAttribute("xmlDate", loadXml);
        model.addAttribute("load", load);
        model.addAttribute("isExample", (null == flowById.getIsExample() ? false : flowById.getIsExample()));
        return model;
    }

    /**
     * save data
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/saveDataForTask")
    @ResponseBody
    public String saveDataForTask(HttpServletRequest request, Model model) {
        String imageXML = request.getParameter("imageXML");
        String loadId = request.getParameter("load");
        String operType = request.getParameter("operType");
        return mxGraphModelServiceImpl.saveDataForTask(imageXML, loadId, operType);
    }

    /**
     * save data
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/saveDataForGroup")
    @ResponseBody
    public String saveDataForGroup(HttpServletRequest request, Model model) {
        String imageXML = request.getParameter("imageXML");
        String loadId = request.getParameter("load");
        String operType = request.getParameter("operType");
        return flowGroupServiceImpl.saveOrUpdateFlowGroupAll(imageXML, loadId, operType, true);
    }

}
