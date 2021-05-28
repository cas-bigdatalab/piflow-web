package cn.cnic.component.livy.mapper;

import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.provider.NoteBookMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteBookMapper {

    /**
     * add NoteBook
     *
     * @param noteBook
     * @return Integer
     */
    @InsertProvider(type = NoteBookMapperProvider.class, method = "addNoteBook")
    public Integer addNoteBook(NoteBook noteBook);

    /**
     * update NoteBook
     *
     * @param noteBook
     * @return Integer
     */
    @UpdateProvider(type = NoteBookMapperProvider.class, method = "updateNoteBook")
    public Integer updateNoteBook(NoteBook noteBook);

    /**
     * update NoteBook enable_flag
     *
     * @param isAdmin
     * @param username
     * @param id
     * @return Integer
     */
    @UpdateProvider(type = NoteBookMapperProvider.class,method = "deleteNoteBookById")
    public Integer deleteNoteBookById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("id") String id);

    /**
     * get NoteBook by name
     *
     * @param isAdmin
     * @param username
     * @param name
     * @return Integer
     */
    @SelectProvider(type = NoteBookMapperProvider.class,method = "checkNoteBookByName")
    public List<NoteBook> checkNoteBookByName(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("name") String name);


    /**
     * get NoteBook by id
     *
     * @param isAdmin
     * @param username
     * @param id
     * @return Integer
     */
    @SelectProvider(type = NoteBookMapperProvider.class,method = "getNoteBookById")
    public NoteBook getNoteBookById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("id") String id);


    /**
     * search NoteBook List
     *
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    @SelectProvider(type = NoteBookMapperProvider.class,method = "getNoteBookList")
    public List<NoteBook> getNoteBookList(boolean isAdmin, String username, String param);

}
