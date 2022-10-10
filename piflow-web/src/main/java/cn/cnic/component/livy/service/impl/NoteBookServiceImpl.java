package cn.cnic.component.livy.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.livy.domain.NoteBookDomain;
import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.service.INoteBookService;
import cn.cnic.component.livy.util.NoteBookUtils;
import cn.cnic.controller.requestVo.NoteBookVoRequest;
import cn.cnic.third.livy.service.ILivy;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class NoteBookServiceImpl implements INoteBookService {

    private final NoteBookDomain noteBookDomain;
    private final ILivy livyImpl;

    @Autowired
    public NoteBookServiceImpl(NoteBookDomain noteBookDomain, ILivy livyImpl) {
        this.noteBookDomain = noteBookDomain;
        this.livyImpl = livyImpl;
    }

    @Override
    public String saveOrUpdateNoteBook(String username, boolean isAdmin, NoteBookVoRequest noteBookVo, boolean flag) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == noteBookVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StringUtils.isBlank(noteBookVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        NoteBook noteBook = null;
        String noteBookVoId = noteBookVo.getId();
        if (StringUtils.isNotBlank(noteBookVoId)) {
            noteBook = noteBookDomain.getNoteBookById(isAdmin, username, noteBookVoId);
        }

        if (null == noteBook) {
            noteBook = NoteBookUtils.setNoteBookBasicInformation(null, false, username);
        }
        BeanUtils.copyProperties(noteBookVo, noteBook);
        Integer returnValue = 0;
        if (StringUtils.isBlank(noteBook.getId())) {
            noteBook.setId(UUIDUtils.getUUID32());
            returnValue = noteBookDomain.addNoteBook(noteBook);
        } else {
            returnValue = noteBookDomain.updateNoteBook(noteBook);
        }
        if (returnValue > 0) {
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
            return ReturnMapUtils.appendValuesToJson(rtnMap, "noteBookId", noteBook.getId());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
    }

    @Override
    public String checkNoteBookName(String username, boolean isAdmin, String noteBookName) {
        if (StringUtils.isBlank(noteBookName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookName"));
        }
        int noteBookNameCount = noteBookDomain.checkNoteBookByName(isAdmin, username, noteBookName);
        if (noteBookNameCount > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.XXX_ALREADY_TAKEN_MSG("noteBook"));
        } else {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.XXX_AVAILABLE_MSG("noteBook"));
        }

    }

    @Override
    public String deleteNoteBook(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        int i = noteBookDomain.deleteNoteBookById(isAdmin, username, noteBookId);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String getNoteBookListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Page<NoteBook> page = PageHelper.startPage(offset, limit);
        noteBookDomain.getNoteBookList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }
    
    /**
     * getNoteBookById
     *
     * @param username
     * @param isAdmin
     * @param id
     * @return String
     */
    @Override
    public String getNoteBookById(String username, boolean isAdmin, String id) {
    	if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        NoteBook noteBook = noteBookDomain.getNoteBookById(isAdmin, username, id);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
    	return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("noteBook", noteBook);
    }

    /**
     * startNoteBookSession
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    @Override
    public String startNoteBookSession(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        NoteBook noteBook = noteBookDomain.getNoteBookById(isAdmin, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Map<String, Object> rtnMap = livyImpl.startSessions();
        if (null == rtnMap || (int)rtnMap.get(ReturnMapUtils.KEY_CODE) != 200) {
        	return ReturnMapUtils.toJson(rtnMap);
        }
        String sessionsId = rtnMap.get("sessionsId").toString();
    	noteBook.setSessionsId(sessionsId);
    	noteBook.setLastUpdateDttm(new Date());
    	noteBook.setLastUpdateUser(username);
    	int affectedRows = noteBookDomain.updateNoteBook(noteBook);
    	if (affectedRows > 0) {
    		return ReturnMapUtils.toJson(rtnMap);
    	}
    	livyImpl.stopSessions(sessionsId);
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG());
    }

    /**
     * getNoteBookSessionState
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    @Override
    public String getNoteBookSessionState(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        NoteBook noteBook = noteBookDomain.getNoteBookById(isAdmin, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        String sessionsId = noteBook.getSessionsId();
        if (StringUtils.isBlank(sessionsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_XXX_IS_NULL_MSG("sessionsId"));
        }
        Map<String, Object> rtnMap = livyImpl.getSessionsState(sessionsId);
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * delNoteBookSession
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    @Override
    public String delNoteBookSession(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        NoteBook noteBook = noteBookDomain.getNoteBookById(isAdmin, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        String sessionsId = noteBook.getSessionsId();
        if (StringUtils.isBlank(sessionsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_XXX_IS_NULL_MSG("sessionsId"));
        }
        Map<String, Object> rtnMap = livyImpl.stopSessions(sessionsId);
        if (null == rtnMap || (int)rtnMap.get(ReturnMapUtils.KEY_CODE) != 200) {
        	return ReturnMapUtils.toJson(rtnMap);
        }
        noteBook.setSessionsId(null);
    	noteBook.setLastUpdateDttm(new Date());
    	noteBook.setLastUpdateUser(username);
    	int affectedRows = noteBookDomain.updateNoteBook(noteBook);
    	if (affectedRows > 0) {
    		return ReturnMapUtils.toJson(rtnMap);
    	}
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG());
    }

    /**
     * getAllNoteBookRunning
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    @Override
    public String getAllNoteBookRunning(String username, boolean isAdmin) {
        Map<String, Object> rtnMap = livyImpl.getAllSessions();
        return ReturnMapUtils.toJson(rtnMap);
    }
}
