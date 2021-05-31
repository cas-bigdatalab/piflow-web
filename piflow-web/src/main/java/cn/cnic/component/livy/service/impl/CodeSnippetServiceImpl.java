package cn.cnic.component.livy.service.impl;

import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.livy.entity.CodeSnippet;
import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.CodeSnippetMapper;
import cn.cnic.component.livy.service.ICodeSnippetService;
import cn.cnic.component.livy.util.CodeSnippetUtils;
import cn.cnic.controller.requestVo.RequesCodeSnippetVo;
import cn.cnic.third.livy.service.ILivy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CodeSnippetServiceImpl implements ICodeSnippetService {

    @Autowired
    private CodeSnippetMapper codeSnippetMapper;

    @Autowired
    private ILivy livyImpl;

    @Override
    public String addCodeSnippet(String username, RequesCodeSnippetVo codeSnippetVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == codeSnippetVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        CodeSnippet codeSnippet = new CodeSnippet();
        BeanUtils.copyProperties(codeSnippetVo, codeSnippet);
        codeSnippet = CodeSnippetUtils.setCodeSnippetBasicInformation(codeSnippet, true, username);
        int affectedRows = codeSnippetMapper.addCodeSnippet(codeSnippet);
        if (affectedRows > 0) {
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("Save noteBook succeeded.");
            return ReturnMapUtils.appendValuesToJson(rtnMap, "noteBookId", codeSnippet.getId());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Save noteBook failed");
    }

    @Override
    public String updateCodeSnippet(String username, RequesCodeSnippetVo codeSnippetVo) {
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
        NoteBook noteBook = codeSnippet.getNoteBook();
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data Error");
        }
        String sessionsId = noteBook.getSessionsId();
        String codeContent = codeSnippet.getCodeContent();
        Map<String, Object> stringObjectMap = livyImpl.runStatements(sessionsId, codeContent);
        return ReturnMapUtils.mapToJson(stringObjectMap);
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
        if (StringUtils.isNotBlank(sessionsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data error, sessionsId is null");
        }
        String executeId = codeSnippet.getExecuteId();
        if (StringUtils.isNotBlank(executeId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data error, not execute");
        }
        Map<String, Object> statementsResult = livyImpl.getStatementsResult(noteBook.getSessionsId(), executeId);
        String data = statementsResult.get("data").toString();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", data);
    }
}
