package cn.cnic.base.vo;

import java.io.Serializable;

/**
 * Stateful return value
 *
 */
public class StatefulRtnBase implements Serializable {

    public final String ERRCODE_SUCCESS = "1";
    public final String ERRCODE_FAIL = "0";

    public final String ERRMSG_SUCCESS = "OK";

    private static final long serialVersionUID = 1L;

    /**
     * Request processing response default success
     */
    private boolean reqRtnStatus = true;

    /**
     * Request processing failure error code
     */
    private String errorCode = ERRCODE_SUCCESS;

    /**
     * Request processing failure error message
     */
    private String errorMsg = ERRMSG_SUCCESS;

    public boolean isReqRtnStatus() {
        return reqRtnStatus;
    }

    public void setReqRtnStatus(boolean reqRtnStatus) {
        this.reqRtnStatus = reqRtnStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
