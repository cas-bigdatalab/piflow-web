package cn.cnic.component.system.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassportUserLoginStatus {

    public PassportUserLoginStatus(boolean isAuthenticated, String userName) {
        this.isAuthenticated = isAuthenticated;
        this.userName = userName;
    }
    private boolean isAuthenticated = false;
    private String userName;

}
