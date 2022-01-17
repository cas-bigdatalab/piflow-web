package cn.cnic.component.livy.service.impl;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.livy.entity.CodeSnippet;
import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.CodeSnippetMapper;
import cn.cnic.component.livy.mapper.NoteBookMapper;
import cn.cnic.component.livy.service.ICodeSnippetService;
import cn.cnic.component.livy.util.CodeSnippetUtils;
import cn.cnic.controller.requestVo.CodeSnippetVoRequestAdd;
import cn.cnic.controller.requestVo.CodeSnippetVoRequestUpdate;
import cn.cnic.third.livy.service.ILivy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CodeSnippetServiceImpl implements ICodeSnippetService {

    private final CodeSnippetMapper codeSnippetMapper;
    private final NoteBookMapper noteBookMapper;
    private final ILivy livyImpl;

    @Autowired
    public CodeSnippetServiceImpl(CodeSnippetMapper codeSnippetMapper,
                                  NoteBookMapper noteBookMapper,
                                  ILivy livyImpl) {
        this.codeSnippetMapper = codeSnippetMapper;
        this.noteBookMapper = noteBookMapper;
        this.livyImpl = livyImpl;
    }

    @Override
    public String addCodeSnippet(String username, CodeSnippetVoRequestAdd codeSnippetVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == codeSnippetVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        String noteBookId = codeSnippetVo.getNoteBookId();
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId is null");
        }
        NoteBook noteBook = noteBookMapper.getNoteBookById(false, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId data is null");
        }
        CodeSnippet codeSnippet = new CodeSnippet();
        BeanUtils.copyProperties(codeSnippetVo, codeSnippet);
        codeSnippet = CodeSnippetUtils.setCodeSnippetBasicInformation(codeSnippet, true, username);
        codeSnippet.setNoteBook(noteBook);
        int affectedRows = codeSnippetMapper.addCodeSnippet(codeSnippet);
        if (affectedRows > 0) {
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("Save codeSnippet succeeded.");
            return ReturnMapUtils.appendValuesToJson(rtnMap, "codeSnippetId", codeSnippet.getId());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Save noteBook failed");
    }

    @Override
    public String updateCodeSnippet(String username, CodeSnippetVoRequestUpdate codeSnippetVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == codeSnippetVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(codeSnippetVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param id is empty");
        }
        CodeSnippet codeSnippet = codeSnippetMapper.getCodeSnippetById(codeSnippetVo.getId());
        if (null == codeSnippet) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Update failed, data is null");
        }
        BeanUtils.copyProperties(codeSnippetVo, codeSnippet);
        int affectedRows = codeSnippetMapper.updateCodeSnippet(codeSnippet);
        if (affectedRows > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Update noteBook succeeded.");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Update noteBook failed");
    }

    @Override
    public String delCodeSnippet(String username, boolean isAdmin, String codeSnippetId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(codeSnippetId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("codeSnippetId is empty");
        }
        int affectedRows = codeSnippetMapper.delCodeSnippetById(isAdmin, username, codeSnippetId);
        if (affectedRows > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Del noteBook succeeded.");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Del noteBook failed");
    }

    @Override
    public String getCodeSnippetList(String noteBookId) {
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBookId is empty");
        }
        List<CodeSnippet> codeSnippetList = codeSnippetMapper.getCodeSnippetListByNoteBookId(noteBookId);
        if (null == codeSnippetList) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("codeSnippetList", codeSnippetList);
    }

    @Override
    public String runCodeSnippet(String username, String codeSnippetId) {
        CodeSnippet codeSnippet = codeSnippetMapper.getCodeSnippetById(codeSnippetId);
        if (null == codeSnippet) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data");
        }
        if (null == codeSnippet.getNoteBook()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data Error");
        }
        String sessionsId = codeSnippet.getNoteBook().getSessionsId();
        String codeContent = codeSnippet.getCodeContent();
        Map<String, Object> rtnMap = livyImpl.runStatements(sessionsId, codeContent);
        if (null == rtnMap || (int)rtnMap.get("code") != 200) {
        	return ReturnMapUtils.mapToJson(rtnMap);
        }
        String statementsId = rtnMap.get("statementsId").toString();
        codeSnippet.setExecuteId(statementsId);
        codeSnippet.setLastUpdateDttm(new Date());
        codeSnippet.setLastUpdateUser(username);
     	int affectedRows = codeSnippetMapper.updateCodeSnippet(codeSnippet);
       	if (affectedRows > 0) {
    		return ReturnMapUtils.mapToJson(rtnMap);
    	}
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Interface call succeeded, save Failed.");
    }

    @Override
    public String getStatementsResult(String codeSnippetId) {
        if (StringUtils.isBlank(codeSnippetId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("codeSnippetId is null ");
        }
        CodeSnippet codeSnippet = codeSnippetMapper.getCodeSnippetById(codeSnippetId);
        if (null == codeSnippet) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data");
        }
        NoteBook noteBook = codeSnippet.getNoteBook();
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data error, noteBook is null");
        }
        String sessionsId = noteBook.getSessionsId();
        if (StringUtils.isBlank(sessionsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data error, sessionsId is null");
        }
        String executeId = codeSnippet.getExecuteId();
        if (StringUtils.isBlank(executeId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data error, not execute");
        }
        Map<String, Object> rtnMap = livyImpl.getStatementsResult(noteBook.getSessionsId(), executeId);
        return ReturnMapUtils.mapToJson(rtnMap);
    }
}
