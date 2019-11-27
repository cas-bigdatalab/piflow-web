package com.nature.component.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DebugDataRequest implements Serializable {

    public DebugDataRequest(String appID, String stopName, String portName, String startFileName, int startLine) {
        this.appID = appID;
        this.stopName = stopName;
        this.portName = portName;
        this.startFileName = startFileName;
        this.startLine = startLine;
    }

    private String appID;
    private String stopName;
    private String portName;
    private String startFileName;
    private int startLine;

}
