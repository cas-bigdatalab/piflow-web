package cn.cnic.third.vo.flowLog;

import java.io.Serializable;

public class ThirdFlowLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private ThirdAppVo app;

    public ThirdAppVo getApp() {
        return app;
    }

    public void setApp(ThirdAppVo app) {
        this.app = app;
    }

}
