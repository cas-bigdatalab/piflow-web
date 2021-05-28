package cn.cnic.component.livy.mapper;

import cn.cnic.component.livy.entity.CodeSnippet;
import cn.cnic.component.livy.mapper.provider.CodeSnippetMapperProvider;
import cn.cnic.component.livy.vo.CodeSnippetVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface CodeSnippetMapper {


    /**
     * add CodeSnippet
     *
     * @param codeSnippet
     * @return Integer
     */
    @InsertProvider(type = CodeSnippetMapperProvider.class, method = "addCodeSnippet")
    public Integer addCodeSnippet(CodeSnippet codeSnippet);

    /**
     * update CodeSnippet
     *
     * @param codeSnippet
     * @return Integer
     */
    @UpdateProvider(type = CodeSnippetMapperProvider.class, method = "updateCodeSnippet")
    public Integer updateCodeSnippet(CodeSnippet codeSnippet);

    /**
     * update CodeSnippet enable_flag
     *
     * @param isAdmin
     * @param username
     * @param id
     * @return Integer
     */
    @UpdateProvider(type = CodeSnippetMapperProvider.class,method = "delCodeSnippetById")
    public Integer delCodeSnippetById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("id") String id);

    /**
     * get CodeSnippet by id
     *
     * @param id
     * @return CodeSnippet
     */
    @Select("select * from code_snippet where enable_flag=1 and id=#{id} ")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public CodeSnippet getCodeSnippetById(String id);

    /**
     * get CodeSnippet by id, Do not perform related queries
     *
     * @param id
     * @return CodeSnippet
     */
    @Select("select * from code_snippet where enable_flag=1 and id=#{id} ")
    public CodeSnippet getCodeSnippetByIdOnly(String id);

    /**
     * get CodeSnippetVo by id
     *
     * @param id
     * @return CodeSnippetVo
     */
    @Select("select * from code_snippet where enable_flag=1 and id=#{id} ")
    public CodeSnippetVo getCodeSnippetVoById(String id);

    /**
     * search CodeSnippet List
     *
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    @SelectProvider(type = CodeSnippetMapperProvider.class,method = "getCodeSnippetList")
    public List<CodeSnippet> getCodeSnippetList(boolean isAdmin, String username, String param);

    /**
     * search CodeSnippetVo List
     *
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    @SelectProvider(type = CodeSnippetMapperProvider.class,method = "getCodeSnippetList")
    public List<CodeSnippetVo> getCodeSnippetVoList(boolean isAdmin, String username, String param);

    /**
     * search CodeSnippetVo List
     *
     * @param noteBookId
     * @return
     */
    @SelectProvider(type = CodeSnippetMapperProvider.class,method = "getCodeSnippetList")
    public List<CodeSnippet> getCodeSnippetListByNoteBookId(String noteBookId);

}
