package cn.cnic.component.livy.domain;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.livy.entity.CodeSnippet;
import cn.cnic.component.livy.mapper.CodeSnippetMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class CodeSnippetDomain {

    private final CodeSnippetMapper codeSnippetMapper;

    public CodeSnippetDomain(CodeSnippetMapper codeSnippetMapper) {
        this.codeSnippetMapper = codeSnippetMapper;
    }

    public int addCodeSnippet(CodeSnippet codeSnippet) throws Exception {
        if (null == codeSnippet) {
            throw new Exception("save failed");
        }
        String id = codeSnippet.getId();
        if (StringUtils.isBlank(id)) {
            codeSnippet.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = codeSnippetMapper.addCodeSnippet(codeSnippet);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int updateCodeSnippet(CodeSnippet codeSnippet) {
        if (null == codeSnippet) {
            return 0;
        }
        return codeSnippetMapper.updateCodeSnippet(codeSnippet);
    }

    public Integer delCodeSnippetById(boolean isAdmin, String username, String id) {
        return codeSnippetMapper.delCodeSnippetById(isAdmin, username, id);
    }

    public CodeSnippet getCodeSnippetById(String id) {
        return codeSnippetMapper.getCodeSnippetById(id);
    }

    public List<CodeSnippet> getCodeSnippetListByNoteBookId(String noteBookId) {
        return codeSnippetMapper.getCodeSnippetListByNoteBookId(noteBookId);
    }
}
