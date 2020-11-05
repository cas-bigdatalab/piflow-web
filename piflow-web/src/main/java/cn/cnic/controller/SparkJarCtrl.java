package cn.cnic.controller;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.sparkJar.service.ISparkJarService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sparkJar")
public class SparkJarCtrl {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ISparkJarService sparkJarServiceImpl;


    /**
     * Query and enter the spark jar list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/sparkJarListPage")
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
    public String mountSparkJar(HttpServletRequest request, String id) {
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
    public String unmountSparkJar(HttpServletRequest request, String id) {
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
    public String delSparkJar(HttpServletRequest request, String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return sparkJarServiceImpl.delSparkJar(username, isAdmin, id);
    }
}
