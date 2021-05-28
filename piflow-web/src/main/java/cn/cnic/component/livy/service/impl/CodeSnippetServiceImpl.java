package cn.cnic.component.livy.service.impl;

import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.livy.entity.CodeSnippet;
import cn.cnic.component.livy.mapper.CodeSnippetMapper;
import cn.cnic.component.livy.service.ICodeSnippetService;
import cn.cnic.component.livy.util.CodeSnippetUtils;
import cn.cnic.component.livy.vo.CodeSnippetVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CodeSnippetServiceImpl implements ICodeSnippetService {

    @Autowired
    private CodeSnippetMapper codeSnippetMapper;

    @Override
    public String addCodeSnippet(String username, CodeSnippetVo codeSnippetVo) {
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
        if (affectedRows > 0){
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Save noteBook succeeded.");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Save noteBook failed");
    }

    @Override
    public String updateCodeSnippet(String username, CodeSnippetVo codeSnippetVo) {
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
        if (affectedRows > 0){
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param id is empty");
        }
        int affectedRows = codeSnippetMapper.delCodeSnippetById(isAdmin,username,codeSnippetId);
        if (affectedRows > 0){
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
        return null;
    }
}
