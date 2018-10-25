package com.nature.third.vo.flowLog;

import java.io.Serializable;

public class FlowLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppVo app;

	public AppVo getApp() {
		return app;
	}

	public void setApp(AppVo app) {
		this.app = app;
	}

}
