package cn.cnic.controller.api.admin;

import cn.cnic.component.user.service.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.sparkJar.service.ISparkJarService;
import io.swagger.annotations.Api;

@Api(value = "sparkJar api")
@RestController
@RequestMapping("/sparkJar")
public class SparkJarCtrl {

    @Autowired
    private ISparkJarService sparkJarServiceImpl;

    @Autowired
    private LogHelper logHelper;


    /**
     * Query and enter the spark jar list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/sparkJarListPage", method = RequestMethod.GET)
    @ResponseBody
    public String sparkJarListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sparkJarServiceImpl.sparkJarListPage(username, isAdmin, page, limit, param);
    }


    /**
     * Upload spark jar file and save spark jar
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadSparkJarFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadSparkJarFile(@RequestParam("file") MultipartFile file) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelper.logAuthSucceed("uploadSparkJarFile " + file.getName(),username);
        return sparkJarServiceImpl.uploadSparkJarFile(username, file);
    }

    /**
     * Mount spark jar
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/mountSparkJar", method = RequestMethod.POST)
    @ResponseBody
    public String mountSparkJar(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return sparkJarServiceImpl.mountSparkJar(username, isAdmin, id);
    }

    /**
     * unmount spark jar
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/unmountSparkJar", method = RequestMethod.POST)
    @ResponseBody
    public String unmountSparkJar(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return sparkJarServiceImpl.unmountSparkJar(username, isAdmin, id);
    }

    /**
     * del spark jar
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delSparkJar", method = RequestMethod.POST)
    @ResponseBody
    public String delSparkJar(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("del SparkJar " + id,username);
        return sparkJarServiceImpl.delSparkJar(username, isAdmin, id);
    }
}
