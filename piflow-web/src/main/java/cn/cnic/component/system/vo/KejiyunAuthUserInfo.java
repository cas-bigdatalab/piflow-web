package cn.cnic.component.system.vo;

import java.util.List;


public class KejiyunAuthUserInfo {
    private int umtId;
    private String truename;
    private String type;
    private String securityEmail;
    private String cstnetIdStatus;
    private String cstnetId;
    private String passwordType;
    private List<String> secondaryEmails;

    // Getter 和 Setter 方法
    public int getUmtId() {
        return umtId;
    }

    public void setUmtId(int umtId) {
        this.umtId = umtId;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecurityEmail() {
        return securityEmail;
    }

    public void setSecurityEmail(String securityEmail) {
        this.securityEmail = securityEmail;
    }

    public String getCstnetIdStatus() {
        return cstnetIdStatus;
    }

    public void setCstnetIdStatus(String cstnetIdStatus) {
        this.cstnetIdStatus = cstnetIdStatus;
    }

    public String getCstnetId() {
        return cstnetId;
    }

    public void setCstnetId(String cstnetId) {
        this.cstnetId = cstnetId;
    }

    public String getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(String passwordType) {
        this.passwordType = passwordType;
    }

    public List<String> getSecondaryEmails() {
        return secondaryEmails;
    }

    public void setSecondaryEmails(List<String> secondaryEmails) {
        this.secondaryEmails = secondaryEmails;
    }
}

