package cn.cnic.controller.api.other;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.stopsComponent.service.IStopsComponentService;
import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "datasource api")
@Controller
@RequestMapping("/datasource")
public class DataSourceCtrl {

    private final IDataSource dataSourceImpl;
    private final IStopsService stopsServiceImpl;
    private final ILogHelperService logHelperServiceImpl;
    private final IStopsComponentService stopsComponentServiceImpl;

    @Autowired
    public DataSourceCtrl(IDataSource dataSourceImpl,
                          IStopsService stopsServiceImpl,
                          ILogHelperService logHelperServiceImpl,
                          IStopsComponentService stopsComponentServiceImpl) {
        this.dataSourceImpl = dataSourceImpl;
        this.stopsServiceImpl = stopsServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
        this.stopsComponentServiceImpl = stopsComponentServiceImpl;
    }

    @RequestMapping(value = "/getDatasourceList", method = RequestMethod.POST)
    @ResponseBody
    public String getDatasourceList() {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceVoList(currentUsername, isAdmin);
    }

    @RequestMapping(value = "/getDataSourceListPagination", method = RequestMethod.GET)
    @ResponseBody
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "page", value = "page", required = true, paramType = "query"),
		@ApiImplicitParam(name = "limit", value = "limit", required = true, paramType = "query"),
		@ApiImplicitParam(name = "param", value = "param", required = false, paramType = "query")
	})
    public String getDataSourceListPagination(Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceVoListPage(currentUsername, isAdmin, page, limit, param);
    }

    @RequestMapping(value = "/getDatasourceById", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")
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
        logHelperServiceImpl.logAuthSucceed("saveOrUpdate" + dataSourceVo.getDataSourceName(),currentUsername);
        return dataSourceImpl.saveOrUpdate(currentUsername, isAdmin, dataSourceVo, isSynchronize);
    }

    @RequestMapping(value = "/getDataSourceInputData", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "dataSourceId", value = "dataSourceId", required = true, paramType = "query")
    public String getDataSourceInputPageData(String dataSourceId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return dataSourceImpl.getDataSourceInputPageData(username, isAdmin, dataSourceId);
    }

    @RequestMapping(value = "/deleteDataSource", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "dataSourceId", value = "dataSourceId", required = true, paramType = "query")
    public String deleteDataSource(String dataSourceId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("deleteDataSource" + dataSourceId,currentUsername);
        return dataSourceImpl.deleteDataSourceById(currentUsername, isAdmin, dataSourceId);
    }

    @RequestMapping(value = "/fillDatasource", method = RequestMethod.POST)
    @ResponseBody
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "dataSourceId", value = "dataSourceId", required = true, paramType = "query"),
		@ApiImplicitParam(name = "stopId", value = "stopId", required = true, paramType = "query")
	})
    public String fillDatasource(String dataSourceId, String stopId) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsServiceImpl.fillDatasource(username, isAdmin, dataSourceId, stopId);
    }
    
    @RequestMapping(value = "/checkDatasourceLinked", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "dataSourceId", value = "dataSourceId", required = true, paramType = "query")
    public String checkDatasourceLinked(String dataSourceId) throws Exception {
        return stopsServiceImpl.checkDatasourceLinked(dataSourceId);
    }

    /**
     * stopsList for isDataSource is true
     * @return
     */
    @RequestMapping(value = "/getDataSourceStopList",method = RequestMethod.POST)
    @ResponseBody
    public String getDataSourceStopList(){
        return stopsComponentServiceImpl.getDataSourceStopList();
    }

    @RequestMapping(value = "/getDataSourceStopProperty",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stopsTemplateBundle",value = "stopsTemplateBundle",required = true,paramType = "query"),
    })
    public String getDataSourceStopProperty(String stopsTemplateBundle){
        stopsComponentServiceImpl.getStopsComponentPropertyByStopsId(stopsTemplateBundle);
        return null;
    }

}
