package cn.cnic.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ReturnPageCtrl {

    //BootPageCtrl
    @RequestMapping("/bootPage/index")
    public String index() {
        return "bootPage";
    }

    //DatasourceCtrl
    @RequestMapping("/datasource/getDatasourceListPage")
    public String getDatasourceListPage() {
        return "indexRight/flow/data_source_List";
    }

    @RequestMapping("/datasource/getDataSourceInputPage")
    public String getDataSourceInputPage() {
        return "dataSource/dataSourceInput";
    }

    //FlowCtrl
    @RequestMapping("/flow/getFlowListHtml")
    public String getFlowListHtml() {
        return "mxGraph/flow_List";
    }

}
