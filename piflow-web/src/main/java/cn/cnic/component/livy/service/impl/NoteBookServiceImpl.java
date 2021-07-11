package cn.cnic.component.livy.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.NoteBookMapper;
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

    @Autowired
    private NoteBookMapper noteBookMapper;

    @Autowired
    private ILivy livyImpl;

    @Override
    public String saveOrUpdateNoteBook(String username, boolean isAdmin, NoteBookVoRequest noteBookVo, boolean flag) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == noteBookVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(noteBookVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        NoteBook noteBook = null;
        String noteBookVoId = noteBookVo.getId();
        if (StringUtils.isNotBlank(noteBookVoId)) {
            noteBook = noteBookMapper.getNoteBookById(isAdmin, username, noteBookVoId);
        }

        if (null == noteBook) {
            noteBook = NoteBookUtils.setNoteBookBasicInformation(null, false, username);
        }
        BeanUtils.copyProperties(noteBookVo, noteBook);
        Integer returnValue = 0;
        if (StringUtils.isBlank(noteBook.getId())) {
            noteBook.setId(UUIDUtils.getUUID32());
            returnValue = noteBookMapper.addNoteBook(noteBook);
        } else {
            returnValue = noteBookMapper.updateNoteBook(noteBook);
        }
        if (returnValue > 0) {
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("Save noteBook succeeded.");
            return ReturnMapUtils.appendValuesToJson(rtnMap, "noteBookId", noteBook.getId());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Save noteBook failed");
    }

    @Override
    public String checkNoteBookName(String username, boolean isAdmin, String noteBookName) {
        int noteBookNameCount = noteBookMapper.checkNoteBookByName(isAdmin, username, noteBookName);
        if (StringUtils.isBlank(noteBookName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBook name can not be empty");
        }
        if (noteBookNameCount > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBook is already taken");
        } else {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("noteBook is available");
        }

    }

    @Override
    public String deleteNoteBook(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId is null");
        }
        int i = noteBookMapper.deleteNoteBookById(isAdmin, username, noteBookId);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("delete success");
    }

    @Override
    public String getNoteBookListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        Page<NoteBook> page = PageHelper.startPage(offset, limit);
        noteBookMapper.getNoteBookList(isAdmin, username, param);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId is null");
        }
        NoteBook noteBook = noteBookMapper.getNoteBookById(isAdmin, username, id);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
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
    public String startNoteBookSession(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId is null");
        }
        NoteBook noteBook = noteBookMapper.getNoteBookById(isAdmin, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }
        Map<String, Object> rtnMap = livyImpl.startSessions();
        if (null == rtnMap || (int)rtnMap.get("code") != 200) {
        	return ReturnMapUtils.mapToJson(rtnMap);
        }
        String sessionsId = rtnMap.get("sessionsId").toString();
    	noteBook.setSessionsId(sessionsId);
    	noteBook.setLastUpdateDttm(new Date());
    	noteBook.setLastUpdateUser(username);
    	int affectedRows = noteBookMapper.updateNoteBook(noteBook);
    	if (affectedRows > 0) {
    		return ReturnMapUtils.mapToJson(rtnMap);
    	}
    	livyImpl.stopSessions(sessionsId);
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Interface call succeeded, save Failed. sessions close");
    }

    /**
     * delNoteBookSession
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    public String delNoteBookSession(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId is null");
        }
        NoteBook noteBook = noteBookMapper.getNoteBookById(isAdmin, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }
        String sessionsId = noteBook.getSessionsId();
        if (StringUtils.isBlank(sessionsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data error, sessionsId is null");
        }
        Map<String, Object> rtnMap = livyImpl.stopSessions(sessionsId);
        if (null == rtnMap || (int)rtnMap.get("code") != 200) {
        	return ReturnMapUtils.mapToJson(rtnMap);
        }
        noteBook.setSessionsId(null);
    	noteBook.setLastUpdateDttm(new Date());
    	noteBook.setLastUpdateUser(username);
    	int affectedRows = noteBookMapper.updateNoteBook(noteBook);
    	if (affectedRows > 0) {
    		return ReturnMapUtils.mapToJson(rtnMap);
    	}
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Interface call succeeded, save Failed. sessions close");
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
        return ReturnMapUtils.mapToJson(rtnMap);
    }
}
