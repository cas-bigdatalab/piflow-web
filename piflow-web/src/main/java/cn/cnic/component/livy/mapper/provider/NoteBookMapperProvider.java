package cn.cnic.component.livy.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.livy.entity.NoteBook;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import java.util.Date;


public class NoteBookMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private int enableFlag;
    private String name;
    private String description;
    private String codeType;
    private String sessionsId;

    private boolean preventSQLInjectionNoteBook(NoteBook noteBook) {
        if (null == noteBook || StringUtils.isBlank(noteBook.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateDttm = DateUtils
                .dateTimesToStr(null != noteBook.getLastUpdateDttm() ? noteBook.getLastUpdateDttm() : new Date());
        this.id = SqlUtils.preventSQLInjection(noteBook.getId());
        this.enableFlag = ((null != noteBook.getEnableFlag() && noteBook.getEnableFlag()) ? 1 : 0);
        this.version = (null != noteBook.getVersion() ? noteBook.getVersion() : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(noteBook.getLastUpdateUser());

        // Selection field
        this.name = SqlUtils.preventSQLInjection(noteBook.getName());
        this.description = SqlUtils.preventSQLInjection(noteBook.getDescription());
        this.codeType = SqlUtils.preventSQLInjection(noteBook.getCodeType());
        this.sessionsId = SqlUtils.preventSQLInjection(noteBook.getSessionsId());

        return true;
    }

    private void resetNoteBook() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.enableFlag = 1;
        this.name = null;
        this.description = null;
        this.codeType = null;
        this.sessionsId = null;
    }

    /**
     * add NoteBook
     *
     * @param noteBook
     * @return Integer
     */
    public String addNoteBook(NoteBook noteBook) {
        String sql = "SELECT 0";
        if (preventSQLInjectionNoteBook(noteBook)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO note_book ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("name, ");
            strBuf.append("description, ");
            strBuf.append("code_type, ");
            strBuf.append("sessions_id ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(noteBook) + ", ");
            strBuf.append(this.name + ", ");
            strBuf.append(this.description + ", ");
            strBuf.append(this.codeType + ", ");
            strBuf.append(this.sessionsId + " ");
            strBuf.append(")");
            sql = strBuf.toString();
        }
        this.resetNoteBook();
        return sql;
    }

    /**
     * update NoteBook
     *
     * @param noteBook
     * @return Integer
     */
    public String updateNoteBook(NoteBook noteBook) {
        String sqlStr = "SELECT 0";
        boolean flag = preventSQLInjectionNoteBook(noteBook);
        if (flag && StringUtils.isNotBlank(this.id)) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.UPDATE("note_book");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + this.enableFlag);
            sql.SET("name = " + this.name);
            sql.SET("description = " + this.description);
            sql.SET("code_type = " + this.codeType);
            sql.SET("sessions_id = " + this.sessionsId);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            sqlStr = sql.toString();
        }
        this.resetNoteBook();
        return sqlStr;
    }

    /**
     * update NoteBook enable_flag
     *
     * @param isAdmin
     * @param username
     * @param id
     * @return Integer
     */
    public String deleteNoteBookById(boolean isAdmin, String username, String id){
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        String condition = "";
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            condition = " AND crt_user=" + SqlUtils.preventSQLInjection(username);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("UPDATE note_book SET enable_flag=0 WHERE ");
        stringBuffer.append("id=" + SqlUtils.preventSQLInjection(id));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }

    /**
     * get NoteBook by name
     *
     * @param isAdmin
     * @param username
     * @param name
     * @return String
     */
    public String checkNoteBookByName(boolean isAdmin, String username, String name){
        if (StringUtils.isBlank(name)) {
            return "SELECT 0";
        }
        String condition = "";
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            condition = " AND crt_user=" + SqlUtils.preventSQLInjection(username);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT count(name) FROM note_book WHERE ");
        stringBuffer.append("enable_flag=1 and name=" + SqlUtils.preventSQLInjection(name));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }

    /**
     * get NoteBook by id
     *
     * @param isAdmin
     * @param username
     * @param id
     * @return String
     */
    public String getNoteBookById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("id") String id){
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        String condition = "";
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            condition = " AND crt_user=" + SqlUtils.preventSQLInjection(username);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM note_book WHERE ");
        stringBuffer.append("enable_flag=1 and id=" + SqlUtils.preventSQLInjection(id));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }


    /**
     * search NoteBook List
     *
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    public String adminGetNoteBookById(String id) {
    	if (StringUtils.isBlank(id)) {
    		return "SELECT 0";
        }
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("SELECT * FROM note_book WHERE enable_flag=1 ");
        stringBuf.append("AND id=" + SqlUtils.preventSQLInjection(id) + " ");
        return stringBuf.toString();
    }
    


    /**
     * search NoteBook List
     *
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    public String getNoteBookList(boolean isAdmin, String username, String param) {
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("SELECT * FROM note_book WHERE enable_flag=1 ");
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            stringBuf.append("AND crt_user=" + SqlUtils.preventSQLInjection(username) + " ");
        }
        if (StringUtils.isNotBlank(param)) {
            stringBuf.append("crt_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            stringBuf.append("last_update_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            stringBuf.append("name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            stringBuf.append("description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
        }
        stringBuf.append("ORDER BY  last_update_dttm DESC ");
        return stringBuf.toString();
    }
}
