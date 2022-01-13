package cn.cnic.component.system.vo;



import java.io.Serializable;
import java.util.Date;

public class SysLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String lastLoginIp;


    private String action;

    private Boolean status;

    private String result;

    private String comment;

    private Date ctrDttm;

    private Date lastUpdateDttm;

    private Boolean enableFlag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCtrDttm() {
        return ctrDttm;
    }

    public void setCtrDttm(Date ctrDttm) {
        this.ctrDttm = ctrDttm;
    }

    public Date getLastUpdateDttm() {
        return lastUpdateDttm;
    }

    public void setLastUpdateDttm(Date lastUpdateDttm) {
        this.lastUpdateDttm = lastUpdateDttm;
    }

    public Boolean getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }
}
