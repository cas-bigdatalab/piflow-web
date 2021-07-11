package cn.cnic.component.livy.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.livy.entity.CodeSnippet;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class CodeSnippetMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private int enableFlag;
    private String codeContent;
    private String executeId;
    private String noteBookId;
    private int codeSnippetSort;


    private boolean preventSQLInjectionCodeSnippet(CodeSnippet codeSnippet) {
        if (null == codeSnippet || StringUtils.isBlank(codeSnippet.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateDttm = DateUtils
                .dateTimesToStr(null != codeSnippet.getLastUpdateDttm() ? codeSnippet.getLastUpdateDttm() : new Date());
        this.id = SqlUtils.preventSQLInjection(codeSnippet.getId());
        this.enableFlag = ((null != codeSnippet.getEnableFlag() && codeSnippet.getEnableFlag()) ? 1 : 0);
        this.version = (null != codeSnippet.getVersion() ? codeSnippet.getVersion() : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(codeSnippet.getLastUpdateUser());

        // Selection field
        this.executeId = SqlUtils.preventSQLInjection(codeSnippet.getExecuteId());
        this.codeContent = SqlUtils.preventSQLInjection(codeSnippet.getCodeContent());
        String noteBookIdStr = (null != codeSnippet.getNoteBook() ? codeSnippet.getNoteBook().getId() : null);        
        this.noteBookId = SqlUtils.preventSQLInjection(noteBookIdStr);
        this.codeSnippetSort = codeSnippet.getCodeSnippetSort();

        return true;
    }

    private void resetCodeSnippet() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.enableFlag = 1;
        this.executeId = null;
        this.codeContent = null;
        this.noteBookId = null;
        this.codeSnippetSort = 0;
    }

    /**
     * add CodeSnippet
     * 
     * @param codeSnippet
     * @return String
     */
    public String addCodeSnippet(CodeSnippet codeSnippet) {
        String sql = "SELECT 0";
        if (preventSQLInjectionCodeSnippet(codeSnippet)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO code_snippet ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("execute_id, ");
            strBuf.append("code_content, ");
            strBuf.append("fk_note_book_id, ");
            strBuf.append("code_snippet_sort ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(codeSnippet) + ", ");
            strBuf.append(this.executeId + ", ");
            strBuf.append(this.codeContent + ", ");
            strBuf.append(this.noteBookId + ", ");
            strBuf.append(this.codeSnippetSort + " ");
            strBuf.append(")");
            sql = strBuf.toString();
        }
        this.resetCodeSnippet();
        return sql;
    }

    /**
     * update CodeSnippet
     * 
     * @param codeSnippet
     * @return String
     */
    public String updateCodeSnippet(CodeSnippet codeSnippet) {
        String sqlStr = "SELECT 0";
        boolean flag = preventSQLInjectionCodeSnippet(codeSnippet);
        if (flag && StringUtils.isNotBlank(this.id)) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.UPDATE("code_snippet");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + this.enableFlag);
            sql.SET("execute_id = " + this.executeId);
            sql.SET("code_content = " + this.codeContent);
            sql.SET("code_snippet_sort = " + this.codeSnippetSort);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            sqlStr = sql.toString();
        }
        this.resetCodeSnippet();
        return sqlStr;
    }

    /**
     * update CodeSnippet enable_flag
     * 
     * @param isAdmin
     * @param username
     * @param id
     * @return String
     */
    public String delCodeSnippetById(boolean isAdmin, String username, String id){
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
        stringBuffer.append("UPDATE code_snippet SET enable_flag=0 WHERE ");
        stringBuffer.append("id=" + SqlUtils.preventSQLInjection(id));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }

    /**
     * search CodeSnippet List
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @return String
     */
    public String getCodeSnippetList(boolean isAdmin, String username, String param) {
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("SELECT * FROM code_snippet WHERE enable_flag=1 ");
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            stringBuf.append("AND crt_user=" + SqlUtils.preventSQLInjection(username) + " ");
        }
        if (StringUtils.isNotBlank(param)) {
            stringBuf.append("crt_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("last_update_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
        }
        stringBuf.append("ORDER BY  code_snippet_sort ASC ");
        return stringBuf.toString();
    }

    /**
     * search CodeSnippet List
     *
     * @param noteBookId
     * @return String
     */
    public String getCodeSnippetListByNoteBookId(String noteBookId) {
        if (StringUtils.isBlank(noteBookId)) {
            return "SELECT 0";
        }
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("SELECT * FROM code_snippet WHERE enable_flag=1 ");
        stringBuf.append("AND fk_note_book_id=" + SqlUtils.preventSQLInjection(noteBookId) + " ");
        stringBuf.append("ORDER BY  code_snippet_sort ASC ");
        return stringBuf.toString();
    }


}
