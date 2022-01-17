package cn.cnic.controller.api.livy;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.livy.service.INoteBookService;
import cn.cnic.component.system.service.ILogHelperService;
import cn.cnic.controller.requestVo.NoteBookVoRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "noteBoot api")
@RestController
@RequestMapping(value = "/noteBoot")
public class NoteBookCtrl {

    private final INoteBookService noteBookServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public NoteBookCtrl(INoteBookService noteBookServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.noteBookServiceImpl = noteBookServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    /**
     * saveOrUpdateNoteBook
     *
     * @param noteBookVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrUpdateNoteBook", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdateNoteBook(NoteBookVoRequest noteBookVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("saveOrUpdateNoteBook " + noteBookVo.getName(),currentUsername);
        return noteBookServiceImpl.saveOrUpdateNoteBook(currentUsername, isAdmin, noteBookVo, false);
    }

    /**
     * getNoteBookById
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getNoteBookById", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")
    public String getNoteBookById(String id) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return noteBookServiceImpl.getNoteBookById(currentUsername, isAdmin, id);
    }

    /**
     * checkNoteBookName
     *
     * @param noteBookName
     * @return
     */
    @RequestMapping(value = "/checkNoteBookName", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "noteBookName", value = "noteBookName", required = true, paramType = "query")
    public String checkNoteBookName(String noteBookName) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return noteBookServiceImpl.checkNoteBookName(currentUsername, isAdmin, noteBookName);
    }

    /**
     * deleteNoteBook
     *
     * @param noteBookId
     * @return
     */
    @RequestMapping(value = "/deleteNoteBook", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "noteBookId", value = "noteBookId", required = true, paramType = "query")
    public String deleteNoteBook(String noteBookId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("deleteNoteBook " + noteBookId,currentUsername);
        return noteBookServiceImpl.deleteNoteBook(currentUsername, isAdmin, noteBookId);
    }

    /**
     * "noteBook" list Pagination
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/noteBookListPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({ 
        @ApiImplicitParam(name = "page", value = "page", required = true, paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "limit", required = true, paramType = "query"),
        @ApiImplicitParam(name = "param", value = "param", required = false, paramType = "query")
    })
    public String noteBookListPage(Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return noteBookServiceImpl.getNoteBookListPage(currentUsername, isAdmin, page, limit, param);
    }

    /**
     * startNoteBookSession
     *
     * @param noteBookId
     * @return
     */
    @RequestMapping(value = "/startNoteBookSession", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "noteBookId", value = "noteBookId", required = true, paramType = "query")
    public String startNoteBookSession(String noteBookId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("startNoteBookSession " + noteBookId,currentUsername);
        return noteBookServiceImpl.startNoteBookSession(currentUsername, isAdmin, noteBookId);
    }
    
    /**
     * startNoteBookSession
     *
     * @param noteBookId
     * @return
     */
    @RequestMapping(value = "/getNoteBookSessionState", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "noteBookId", value = "noteBookId", required = true, paramType = "query")
    public String getNoteBookSessionState(String noteBookId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return noteBookServiceImpl.getNoteBookSessionState(currentUsername, isAdmin, noteBookId);
    }

    /**
     * deleteNoteBookSession
     *
     * @param noteBookId
     * @return
     */
    @RequestMapping(value = "/deleteNoteBookSession", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "noteBookId", value = "noteBookId", required = true, paramType = "query")
    public String deleteNoteBookSession(String noteBookId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("deleteNoteBookSession " + noteBookId,currentUsername);
        return noteBookServiceImpl.delNoteBookSession(currentUsername, isAdmin, noteBookId);
    }

}
