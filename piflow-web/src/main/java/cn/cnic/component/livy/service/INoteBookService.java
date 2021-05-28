package cn.cnic.component.livy.service;


import cn.cnic.controller.requestVo.RequesNoteBookVo;

public interface INoteBookService {

    /**
     * saveOrUpdateNoteBook
     *
     * @param username
     * @param isAdmin
     * @param noteBookVo
     * @return String
     * @throws Exception
     */
    public String saveOrUpdateNoteBook(String username, boolean isAdmin, RequesNoteBookVo noteBookVo, boolean flag) throws Exception;


    /**
     * checkNoteBookName
     *
     * @param username
     * @param isAdmin
     * @param noteBookName
     * @return String
     */

    public String checkNoteBookName(String username, boolean isAdmin, String noteBookName);
    /**
     * deleteNoteBook
     *
     * @param username
     * @param isAdmin
     * @param noteBookId
     * @return String
     */
    public String deleteNoteBook(String username, boolean isAdmin, String noteBookId);

    /**
     * getNoteBookList
     *
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @return String
     */
    public String getNoteBookListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

}
