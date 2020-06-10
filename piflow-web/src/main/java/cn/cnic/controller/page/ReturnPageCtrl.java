package cn.cnic.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class ReturnPageCtrl {

    //BootPageCtrl ---------------------------------------------- Start ----------------------------------------------
    @RequestMapping("/bootPage/index")
    public String index() {
        return "bootPage";
    }
    //BootPageCtrl ----------------------------------------------  End  ----------------------------------------------


    //DatasourceCtrl ---------------------------------------------- Start ----------------------------------------------
    @RequestMapping("/datasource/getDatasourceListPage")
    public String getDatasourceListPage() {
        return "indexRight/flow/data_source_List";
    }

    @RequestMapping("/datasource/getDataSourceInputPage")
    public String getDataSourceInputPage() {
        return "dataSource/dataSourceInput";
    }
    //DatasourceCtrl ----------------------------------------------  End  ----------------------------------------------


    //FlowCtrl
    @RequestMapping("/flow/getFlowListHtml")
    public String getFlowListHtml() {
        return "mxGraph/flow_List";
    }
    //FlowCtrl ----------------------------------------------  End  ----------------------------------------------


    //ProcessCtrl ---------------------------------------------- Start ----------------------------------------------
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
