package cn.cnic.controller.api.livy;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.livy.service.ICodeSnippetService;
import cn.cnic.component.user.service.LogHelper;
import cn.cnic.controller.requestVo.CodeSnippetVoRequestAdd;
import cn.cnic.controller.requestVo.CodeSnippetVoRequestUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

    @Resource
    private LogHelper logHelper;

    /**
     * addCodeSnippet
     *
     * @param codeSnippetVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addCodeSnippet", method = RequestMethod.POST)
    @ResponseBody
    public String addCodeSnippet(CodeSnippetVoRequestAdd codeSnippetVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("addCodeSnippet" + codeSnippetVo.getNoteBookId() ,currentUsername);
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
    public String updateCodeSnippet(CodeSnippetVoRequestUpdate codeSnippetVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("updateCodeSnippet " + codeSnippetVo.getCodeSnippetSort(),currentUsername);
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
    @ApiImplicitParam(name="codeSnippetId", value="codeSnippet id", required = true)
    public String delCodeSnippet(String codeSnippetId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelper.logAuthSucceed("deleteCodeSnippet " + codeSnippetId,username);
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
    @ApiImplicitParam(name="noteBookId", value="noteBookId", required = true, paramType = "query")
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
    @ApiImplicitParam(name="codeSnippetId", value="codeSnippet id", required = true, paramType = "query")
    public String runStatements(String codeSnippetId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return codeSnippetServiceImpl.runCodeSnippet(currentUsername, codeSnippetId);
    }

    /**
     * getStatementsResult
     *
     * @param codeSnippetId
     * @return
     */
    @RequestMapping(value = "/getStatementsResult", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name="codeSnippetId", value="codeSnippet id", required = true, paramType = "query")
    public String getStatementsResult(String codeSnippetId) {
        return codeSnippetServiceImpl.getStatementsResult(codeSnippetId);
    }

}
