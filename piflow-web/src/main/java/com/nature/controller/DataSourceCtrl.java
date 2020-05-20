package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.component.dataSource.service.IDataSource;
import com.nature.component.dataSource.vo.DataSourceVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/datasource")
public class DataSourceCtrl {
    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IDataSource dataSourceImpl;

    @RequestMapping("/getDatasourceListPage")
    public String getDatasourceListPage() {
        return "indexRight/flow/data_source_List";
    }

    @RequestMapping("/getDatasourceList")
    @ResponseBody
    public String getDatasourceList() {
        return dataSourceImpl.getDataSourceVoList(false);
    }

    @RequestMapping("/getDataSourceListPagination")
    @ResponseBody
    public String getDataSourceListPagination(Integer page, Integer limit, String param) {
        return dataSourceImpl.getDataSourceVoListPage(page, limit, param);
    }

    @RequestMapping("/getDatasourceById")
    @ResponseBody
    public String getDatasourceById(String id) {
        return dataSourceImpl.getDataSourceVoById(id);
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(DataSourceVo dataSourceVo) {
        return dataSourceImpl.saveOrUpdate(dataSourceVo);
    }

    @RequestMapping("/getDataSourceInputPage")
    public String getDataSourceInputPage(Model model, String dataSourceId) {
        DataSourceVo dataSourceVo = dataSourceImpl.dataSourceVoById(dataSourceId);
        if (null != dataSourceVo) {
            model.addAttribute("dataSourceVo", dataSourceVo);
        }
        List<DataSourceVo> dataSourceTemplateList = dataSourceImpl.getDataSourceTemplateList();
        model.addAttribute("templateList", dataSourceTemplateList);
        return "dataSource/dataSourceInput";
    }

    @RequestMapping("/deleteDataSource")
    @ResponseBody
    public String deleteDataSource(String dataSourceId) {
        return dataSourceImpl.deleteDataSourceById(dataSourceId);
    }

    @RequestMapping("/fillDatasource")
    @ResponseBody
    public String fillDatasource(String dataSourceId, String stopId) {
        return dataSourceImpl.fillDatasource(dataSourceId, stopId);
    }


}
