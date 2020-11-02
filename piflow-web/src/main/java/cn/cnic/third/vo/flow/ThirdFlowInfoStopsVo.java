package cn.cnic.third.vo.flow;

import java.io.Serializable;

public class ThirdFlowInfoStopsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private ThirdFlowInfoStopVo stop;

    public ThirdFlowInfoStopVo getStop() {
        return stop;
    }

    public void setStop(ThirdFlowInfoStopVo stop) {
        this.stop = stop;
    }

}
