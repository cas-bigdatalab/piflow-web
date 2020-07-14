package cn.cnic.controller.page;

import cn.cnic.base.util.MxGraphUtils;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.DrawingBoardType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.vo.MxCellVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.vo.ProcessGroupVo;
import cn.cnic.component.process.vo.ProcessStopVo;
import cn.cnic.component.process.vo.ProcessVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ReturnPageCtrl {

    @RequestMapping("/error/404")
    public String error_404() {
        return "errorPage";
    }

    //BootPageCtrl ---------------------------------------------- Start ----------------------------------------------
    @RequestMapping("/bootPage/index")
    public String index() {
        return "bootPage";
    }
    //BootPageCtrl ----------------------------------------------  End  ----------------------------------------------


    //DatasourceCtrl ---------------------------------------------- Start ----------------------------------------------
    @RequestMapping("/datasource/getDatasourceListPage")
    public String getDatasourceListPage() {
        return "indexRight/datasource/data_source_List";
    }

    @RequestMapping("/datasource/getDataSourceInputPage")
    public String getDataSourceInputPage() {
        return "dataSource/dataSourceInput";
    }
    //DatasourceCtrl ----------------------------------------------  End  ----------------------------------------------


    //FlowCtrl ---------------------------------------------- Start ----------------------------------------------
    @RequestMapping("/flow/drawingBoard")
    public String flowDrawingBoard(HttpServletRequest request, Model model) {
        String load = request.getParameter("load");
        if (StringUtils.isBlank(load)) {
            return "errorPage";
        }
        model.addAttribute("parentAccessPath", request.getParameter("parentAccessPath"));
        model.addAttribute("load", load);
        return "flow/mxGraph/index";
    }
    @RequestMapping("/flow/getFlowListHtml")
    public String getFlowListHtml() {
        return "mxGraph/flow_List";
    }
    //FlowCtrl ----------------------------------------------  End  ----------------------------------------------


    //ProcessCtrl ---------------------------------------------- Start ----------------------------------------------
    @RequestMapping("/process/drawingBoard")
    public String processDrawingBoard(HttpServletRequest request, Model model) {
        String load = request.getParameter("load");
        if (StringUtils.isBlank(load)) {
            return "errorPage";
        }
        model.addAttribute("parentAccessPath", request.getParameter("parentAccessPath"));
        model.addAttribute("load", load);
        return "process/mxGraph/index";
    }

    @RequestMapping("/process/queryProcess")
    public String processListPage(HttpServletRequest request, Model model) {
        model.addAttribute("processId", request.getParameter("processId"));
        return "process/inc/process_Info_Inc";
    }

    @RequestMapping("/process/queryProcessStop")
    public String queryProcessStop(HttpServletRequest request, Model model) {
        model.addAttribute("processId", request.getParameter("processId"));
        model.addAttribute("pageId", request.getParameter("pageId"));
        return "process/inc/process_Property_Inc";
    }

    @RequestMapping("/process/queryProcessPath")
    public String queryProcessPath(HttpServletRequest request, Model model) {
        model.addAttribute("processId", request.getParameter("processId"));
        model.addAttribute("pageId", request.getParameter("pageId"));
        return "process/inc/process_Path_Inc";
    }
    @RequestMapping("/process/getCheckpoint")
    public String getCheckpoint(HttpServletRequest request, Model model) {
        model.addAttribute("pID",request.getParameter("pID"));
        model.addAttribute("parentProcessId",request.getParameter("parentProcessId"));
        return "process/inc/process_Checkpoint_Inc";
    }

    @RequestMapping("/process/getDebugDataHtml")
    public String getDebugDataHtml(HttpServletRequest request, Model model) {
        model.addAttribute("appId", request.getParameter("appId"));
        model.addAttribute("stopName", request.getParameter("stopName"));
        model.addAttribute("portName", request.getParameter("portName"));
        return "process/inc/debug_Data_Inc";
    }

    @RequestMapping("/process/getRunningProcessList")
    public String getRunningProcessList(HttpServletRequest request, Model model) {
        model.addAttribute("flowId", request.getParameter("flowId"));
        return "mxGraph/rightPage/runningProcess";
    }
    //ProcessCtrl ----------------------------------------------  End  ----------------------------------------------

}
