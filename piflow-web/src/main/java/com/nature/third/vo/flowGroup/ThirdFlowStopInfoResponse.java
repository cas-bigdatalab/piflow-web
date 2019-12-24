package com.nature.third.vo.flowGroup;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ThirdFlowStopInfoResponse implements Serializable {

    private String name;
    private String state;
    private String startTime;
    private String endTime;
}
