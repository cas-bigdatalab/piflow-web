package cn.cnic.component.livy.service;


import cn.cnic.controller.requestVo.CodeSnippetVoRequestAdd;
import cn.cnic.controller.requestVo.CodeSnippetVoRequestUpdate;

public interface ICodeSnippetService {

    public String addCodeSnippet(String username, CodeSnippetVoRequestAdd codeSnippetVo);

    public String updateCodeSnippet(String username, CodeSnippetVoRequestUpdate codeSnippetVo);

    public String delCodeSnippet(String username, boolean isAdmin, String codeSnippetId);

    public String getCodeSnippetList(String noteBookId);

    public String runCodeSnippet(String username, String codeSnippetId);

    public String getStatementsResult(String codeSnippetId);

}
