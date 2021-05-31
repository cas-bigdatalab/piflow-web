package cn.cnic.controller.api;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.livy.service.ICodeSnippetService;
import cn.cnic.controller.requestVo.RequesCodeSnippetVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "noteBoot api")
@RestController
@RequestMapping(value = "/codeSnippet")
public class CodeSnippetCtrl {

    @Resource
    private ICodeSnippetService codeSnippetServiceImpl;

    /**
     * addCodeSnippet
     *
     * @param codeSnippetVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addCodeSnippet", method = RequestMethod.POST)
    @ResponseBody
    public String addCodeSnippet(@ApiParam(value = "codeSnippetVo", required = true) RequesCodeSnippetVo codeSnippetVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        return codeSnippetServiceImpl.addCodeSnippet(currentUsername, codeSnippetVo);
    }

    /**
     * updateCodeSnippet
     *
     * @param codeSnippetVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateCodeSnippet", method = RequestMethod.POST)
    @ResponseBody
    public String updateCodeSnippet(@ApiParam(value = "codeSnippetVo", required = true) RequesCodeSnippetVo codeSnippetVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        return codeSnippetServiceImpl.updateCodeSnippet(currentUsername, codeSnippetVo);
    }

    /**
     * delCodeSnippet
     *
     * @param codeSnippetId
     * @return
     */
    @RequestMapping(value = "/deleteCodeSnippet", method = RequestMethod.POST)
    @ResponseBody
    public String delCodeSnippet(String codeSnippetId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return codeSnippetServiceImpl.delCodeSnippet(currentUsername, isAdmin, codeSnippetId);
    }

    /**
     * "codeSnippet" list
     *
     * @param noteBookId
     * @return
     */
    @RequestMapping(value = "/codeSnippetList", method = RequestMethod.POST)
    @ResponseBody
    public String codeSnippetList(String noteBookId) {
        return codeSnippetServiceImpl.getCodeSnippetList(noteBookId);
    }

    /**
     * getStatementsResult
     *
     * @param codeSnippetId
     * @return
     */
    @RequestMapping(value = "/runStatements", method = RequestMethod.POST)
    @ResponseBody
    public String runStatements(String codeSnippetId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return codeSnippetServiceImpl.runCodeSnippet(currentUsername, codeSnippetId);
    }

    /**
     * getStatementsResult
     *
     * @param executeId
     * @return
     */
    @RequestMapping(value = "/getStatementsResult", method = RequestMethod.POST)
    @ResponseBody
    public String getStatementsResult(String codeSnippetId) {
        return codeSnippetServiceImpl.getStatementsResult(codeSnippetId);
    }

}
