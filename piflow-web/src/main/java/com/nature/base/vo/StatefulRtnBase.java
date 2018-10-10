package com.nature.base.vo;

import java.io.Serializable;

/**
 * 有状态的返回值
 * 
 * @author Nature
 *
 */
public class StatefulRtnBase implements Serializable {

	public final String ERRCODE_SUCCESS = "1";
	public final String ERRCODE_FAIL = "0";

	public final String ERRMSG_SUCCESS = "OK";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 请求处理响应默认成功
	 */
	private boolean reqRtnStatus = true;

	/**
	 * 请求处理失败错误码
	 */
	private String errorCode = ERRCODE_SUCCESS;

	/**
	 * 请求处理失败错误信息
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
