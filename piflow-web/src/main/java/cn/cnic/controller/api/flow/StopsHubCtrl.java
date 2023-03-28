package cn.cnic.controller.api.flow;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "stops hub api", tags = "stops hub api")
@RestController
@RequestMapping("/stops")
public class StopsHubCtrl {

    private final ILogHelperService logHelperServiceImpl;
    private final IStopsHubService stopsHubServiceImpl;

    @Autowired
    public StopsHubCtrl(ILogHelperService logHelperServiceImpl,
                     IStopsHubService stopsHubServiceImpl) {

        this.logHelperServiceImpl = logHelperServiceImpl;
        this.stopsHubServiceImpl = stopsHubServiceImpl;
    }

    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/stopsHubListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="stopsHubListPage", notes="stopsHub list page")
    public String stopsHubListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.stopsHubListPage(username, isAdmin, page, limit, param);
    }


    /**
     * Upload stopsHub jar file and save stopsHub
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadStopsHubFile", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="uploadStopsHubFile", notes="upload StopsHub file")
    public String uploadStopsHubFile(@RequestParam("file") MultipartFile file,String type,String languageVersion) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("uploadStopsHubFile " + file.getName(),username);
        return stopsHubServiceImpl.uploadStopsHubFile(username, file,type,languageVersion);
    }

    /**
     * Mount stopsHub
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/mountStopsHub", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="mountStopsHub", notes="mount StopsHub")
    public String mountStopsHub(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.mountStopsHub(username, isAdmin, id);
    }

    /**
     * unmount stopsHub
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/unmountStopsHub", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="unmountStopsHub", notes="unmount StopsHub")
    public String unmountStopsHub(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.unmountStopsHub(username, isAdmin, id);
    }

    /**
     * unmount stopsHub
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delStopsHub", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delStopsHub", notes="delete StopsHub")
    public String delStopsHub(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.delStopsHub(username, isAdmin, id);
    }

    /**
     * stopsHub publishing
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/stopsHubPublishing", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="stopsHubPublishing", notes="stopsHub publishing")
    public String stopsHubPublishing(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.stopsHubPublishing(username, isAdmin, id);
    }

}
