package cn.cnic.component.livy.service;


import cn.cnic.component.livy.vo.CodeSnippetVo;
import cn.cnic.controller.requestVo.RequesCodeSnippetVo;

public interface ICodeSnippetService {

    public String addCodeSnippet(String username, RequesCodeSnippetVo codeSnippetVo);

    public String updateCodeSnippet(String username, RequesCodeSnippetVo codeSnippetVo);

    public String delCodeSnippet(String username, boolean isAdmin, String codeSnippetId);

    public String getCodeSnippetList(String noteBookId);

    public String runCodeSnippet(String username, String codeSnippetId);

    public String getStatementsResult(String executeId);

}
