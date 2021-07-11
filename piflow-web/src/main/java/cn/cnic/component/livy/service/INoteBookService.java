package cn.cnic.component.livy.service;


import cn.cnic.controller.requestVo.NoteBookVoRequest;

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
    public String saveOrUpdateNoteBook(String username, boolean isAdmin, NoteBookVoRequest noteBookVo, boolean flag) throws Exception;


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

    /**
     * getNoteBookById
     *
     * @param username
     * @param isAdmin
     * @param id
     * @return String
     */
    public String getNoteBookById(String username, boolean isAdmin, String id);
    
    /**
     * startNoteBookSession
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    public String startNoteBookSession(String username, boolean isAdmin, String noteBookId);

    /**
     * delNoteBookSession
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    public String delNoteBookSession(String username, boolean isAdmin, String noteBookId);

    /**
     * getAllNoteBookRunning
     *
     * @param username
     * @param isAdmin
     * @return String
     */
    public String getAllNoteBookRunning(String username, boolean isAdmin);

}
