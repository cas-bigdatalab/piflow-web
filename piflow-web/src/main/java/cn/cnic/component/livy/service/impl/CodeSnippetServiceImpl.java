package cn.cnic.component.livy.service.impl;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.livy.domain.CodeSnippetDomain;
import cn.cnic.component.livy.domain.NoteBookDomain;
import cn.cnic.component.livy.entity.CodeSnippet;
import cn.cnic.component.livy.entity.NoteBook;
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

    private final CodeSnippetDomain codeSnippetDomain;
    private final NoteBookDomain noteBookDomain;
    private final ILivy livyImpl;

    @Autowired
    public CodeSnippetServiceImpl(CodeSnippetDomain codeSnippetDomain,
                                  NoteBookDomain noteBookDomain,
                                  ILivy livyImpl) {
        this.codeSnippetDomain = codeSnippetDomain;
        this.noteBookDomain = noteBookDomain;
        this.livyImpl = livyImpl;
    }

    @Override
    public String addCodeSnippet(String username, CodeSnippetVoRequestAdd codeSnippetVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == codeSnippetVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("param"));
        }
        String noteBookId = codeSnippetVo.getNoteBookId();
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        NoteBook noteBook = noteBookDomain.getNoteBookById(false, username, noteBookId);
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(noteBookId));
        }
        CodeSnippet codeSnippet = new CodeSnippet();
        BeanUtils.copyProperties(codeSnippetVo, codeSnippet);
        codeSnippet = CodeSnippetUtils.setCodeSnippetBasicInformation(codeSnippet, true, username);
        codeSnippet.setNoteBook(noteBook);
        int affectedRows = codeSnippetDomain.addCodeSnippet(codeSnippet);
        if (affectedRows > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("codeSnippetId", codeSnippet.getId());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
    }

    @Override
    public String updateCodeSnippet(String username, CodeSnippetVoRequestUpdate codeSnippetVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == codeSnippetVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("param"));
        }
        if (StringUtils.isBlank(codeSnippetVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        CodeSnippet codeSnippet = codeSnippetDomain.getCodeSnippetById(codeSnippetVo.getId());
        if (null == codeSnippet) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
        BeanUtils.copyProperties(codeSnippetVo, codeSnippet);
        int affectedRows = codeSnippetDomain.updateCodeSnippet(codeSnippet);
        if (affectedRows > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
    }

    @Override
    public String delCodeSnippet(String username, boolean isAdmin, String codeSnippetId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(codeSnippetId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("codeSnippetId"));
        }
        int affectedRows = codeSnippetDomain.delCodeSnippetById(isAdmin, username, codeSnippetId);
        if (affectedRows > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
    }

    @Override
    public String getCodeSnippetList(String noteBookId) {
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("noteBookId"));
        }
        List<CodeSnippet> codeSnippetList = codeSnippetDomain.getCodeSnippetListByNoteBookId(noteBookId);
        if (null == codeSnippetList) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("codeSnippetList", codeSnippetList);
    }

    @Override
    public String runCodeSnippet(String username, String codeSnippetId) {
        CodeSnippet codeSnippet = codeSnippetDomain.getCodeSnippetById(codeSnippetId);
        if (null == codeSnippet) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        if (null == codeSnippet.getNoteBook()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        String sessionsId = codeSnippet.getNoteBook().getSessionsId();
        String codeContent = codeSnippet.getCodeContent();
        Map<String, Object> rtnMap = livyImpl.runStatements(sessionsId, codeContent);
        if (null == rtnMap || (int)rtnMap.get(ReturnMapUtils.KEY_CODE) != 200) {
        	return ReturnMapUtils.toJson(rtnMap);
        }
        String statementsId = rtnMap.get("statementsId").toString();
        codeSnippet.setExecuteId(statementsId);
        codeSnippet.setLastUpdateDttm(new Date());
        codeSnippet.setLastUpdateUser(username);
     	int affectedRows = codeSnippetDomain.updateCodeSnippet(codeSnippet);
       	if (affectedRows > 0) {
    		return ReturnMapUtils.toJson(rtnMap);
    	}
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INTERFACE_CALL_ERROR_MSG());
    }

    @Override
    public String getStatementsResult(String codeSnippetId) {
        if (StringUtils.isBlank(codeSnippetId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("codeSnippetId"));
        }
        CodeSnippet codeSnippet = codeSnippetDomain.getCodeSnippetById(codeSnippetId);
        if (null == codeSnippet) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        NoteBook noteBook = codeSnippet.getNoteBook();
        if (null == noteBook) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG("noteBook"));
        }
        String sessionsId = noteBook.getSessionsId();
        if (StringUtils.isBlank(sessionsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG("sessionsId"));
        }
        String executeId = codeSnippet.getExecuteId();
        if (StringUtils.isBlank(executeId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Map<String, Object> rtnMap = livyImpl.getStatementsResult(noteBook.getSessionsId(), executeId);
        return ReturnMapUtils.toJson(rtnMap);
    }
}
