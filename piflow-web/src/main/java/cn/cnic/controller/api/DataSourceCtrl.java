package cn.cnic.controller.api;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.flow.service.IStopsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/datasource")
public class DataSourceCtrl {
    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IDataSource dataSourceImpl;

    @Autowired
    private IStopsService stopsServiceImpl;

    @RequestMapping(value = "/getDatasourceList", method = RequestMethod.POST)
    @ResponseBody
    public String getDatasourceList() {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceVoList(currentUsername, isAdmin);
    }

    @RequestMapping(value = "/getDataSourceListPagination", method = RequestMethod.GET)
    @ResponseBody
    public String getDataSourceListPagination(Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceVoListPage(currentUsername, isAdmin, page, limit, param);
    }

    @RequestMapping(value = "/getDatasourceById", method = RequestMethod.POST)
    @ResponseBody
    public String getDatasourceById(String id) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceVoById(currentUsername, isAdmin, id);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdate(DataSourceVo dataSourceVo, boolean isSynchronize) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.saveOrUpdate(currentUsername, isAdmin, dataSourceVo, isSynchronize);
    }

    @RequestMapping(value = "/getDataSourceInputData", method = RequestMethod.POST)
    @ResponseBody
    public String getDataSourceInputPageData(String dataSourceId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceInputPageData(username, isAdmin, dataSourceId);
    }

    @RequestMapping(value = "/deleteDataSource", method = RequestMethod.POST)
    @ResponseBody
    public String deleteDataSource(String dataSourceId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.deleteDataSourceById(currentUsername, isAdmin, dataSourceId);
    }

    @RequestMapping(value = "/fillDatasource", method = RequestMethod.POST)
    @ResponseBody
    public String fillDatasource(String dataSourceId, String stopId) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        return stopsServiceImpl.fillDatasource(username, dataSourceId, stopId);
    }
    
    @RequestMapping(value = "/checkDatasourceLinked", method = RequestMethod.POST)
    @ResponseBody
    public String checkDatasourceLinked(String dataSourceId) throws Exception {
        return stopsServiceImpl.checkDatasourceLinked(dataSourceId);
    }


}
