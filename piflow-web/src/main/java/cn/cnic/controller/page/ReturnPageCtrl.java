package cn.cnic.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
        String processId = request.getParameter("processId");
        model.addAttribute("processId", processId);
        return "process/inc/process_Info_Inc";
    }

    @RequestMapping("/process/queryProcessStop")
    public String queryProcessStop(HttpServletRequest request, Model model) {
        String processId = request.getParameter("processId");
        String pageId = request.getParameter("pageId");
        model.addAttribute("processId", processId);
        model.addAttribute("pageId", pageId);
        return "process/inc/process_Property_Inc";
    }

    @RequestMapping("/process/queryProcessPath")
    public String queryProcessPath(HttpServletRequest request, Model model) {
        String processId = request.getParameter("processId");
        String pageId = request.getParameter("pageId");
        model.addAttribute("processId", processId);
        model.addAttribute("pageId", pageId);
        return "process/inc/process_Path_Inc";
    }
    //ProcessCtrl ----------------------------------------------  End  ----------------------------------------------

}
