package cn.cnic.component.livy.service;


import cn.cnic.component.livy.vo.CodeSnippetVo;

public interface ICodeSnippetService {

    public String addCodeSnippet(String username, CodeSnippetVo codeSnippetVo);

    public String updateCodeSnippet(String username, CodeSnippetVo codeSnippetVo);

    public String delCodeSnippet(String username, boolean isAdmin, String codeSnippetId);

    public String getCodeSnippetList(String noteBookId);

    public String runCodeSnippet(String username, String codeSnippetId);

}
