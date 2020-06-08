package cn.cnic.controller.modify.user;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.flow.service.IStopsService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/datasource")
public class UserDataSourceCtrl {
    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IDataSource dataSourceImpl;

    @Resource
    private IStopsService stopsServiceImpl;

    @RequestMapping("/getDatasourceList")
    @ResponseBody
    public String getDatasourceList(HttpServletRequest request) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceVoList(currentUsername, false);
    }

    @RequestMapping("/getDataSourceListPagination")
    @ResponseBody
    public String getDataSourceListPagination(HttpServletRequest request, Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceVoListPage(currentUsername, false, page, limit, param);
    }

    @RequestMapping("/getDatasourceById")
    @ResponseBody
    public String getDatasourceById(HttpServletRequest request, String id) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceVoById(currentUsername, false, id);
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(HttpServletRequest request, DataSourceVo dataSourceVo) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.saveOrUpdate(currentUsername, false, dataSourceVo);
    }

    @RequestMapping("/getDataSourceInputData")
    @ResponseBody
    public String getDataSourceInputPageData(HttpServletRequest request, String dataSourceId) {
        String username = SessionUserUtil.getUsername(request);
        return dataSourceImpl.getDataSourceInputPageData(username, false, dataSourceId);
    }

    @RequestMapping("/deleteDataSource")
    @ResponseBody
    public String deleteDataSource(HttpServletRequest request, String dataSourceId) {
        String currentUsername = SessionUserUtil.getUsername(request);
        return dataSourceImpl.deleteDataSourceById(currentUsername, false, dataSourceId);
    }

    @RequestMapping("/fillDatasource")
    @ResponseBody
    public String fillDatasource(HttpServletRequest request, String dataSourceId, String stopId) {
        String username = SessionUserUtil.getUsername(request);
        return stopsServiceImpl.fillDatasource(username, dataSourceId, stopId);
    }


}
