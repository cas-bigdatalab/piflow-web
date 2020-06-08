package cn.cnic.controller.modify.admin;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.flow.service.IStopsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/datasource")
public class AdminDataSourceCtrl {

    @Resource
    private IDataSource dataSourceImpl;

    @Resource
    private IStopsService stopsServiceImpl;

    @RequestMapping("/getDatasourceList")
    @ResponseBody
    public String getDatasourceList(HttpServletRequest request) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceVoList(currentUsername, true);
    }

    @RequestMapping("/getDataSourceListPagination")
    @ResponseBody
    public String getDataSourceListPagination(HttpServletRequest request, Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceVoListPage(currentUsername, true, page, limit, param);
    }

    @RequestMapping("/getDatasourceById")
    @ResponseBody
    public String getDatasourceById(HttpServletRequest request, String id) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceVoById(currentUsername, true, id);
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(HttpServletRequest request, DataSourceVo dataSourceVo) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.saveOrUpdate(currentUsername, true, dataSourceVo);
    }

    @RequestMapping("/getDataSourceInputData")
    @ResponseBody
    public String getDataSourceInputPageData(HttpServletRequest request, String dataSourceId) {
        String username = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceInputPageData(username, true, dataSourceId);
    }

    @RequestMapping("/deleteDataSource")
    @ResponseBody
    public String deleteDataSource(HttpServletRequest request, String dataSourceId) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.deleteDataSourceById(currentUsername, true, dataSourceId);
    }

    @RequestMapping("/fillDatasource")
    @ResponseBody
    public String fillDatasource(HttpServletRequest request, String dataSourceId, String stopId) {
        String username = SessionUserUtil.getUsername(request);
        return stopsServiceImpl.fillDatasource(username, dataSourceId, stopId);
    }


}
