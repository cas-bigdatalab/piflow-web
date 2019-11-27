package com.nature.component.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DebugDataResponse implements Serializable {

    int lastReadLine;                            // The last number of rows read
    String lastFileName;                         // Last read file
    boolean isEnd;                               // Whether to reach the end of the file
    List<String> schema = new ArrayList<>();
    List<String> data = new ArrayList<>();

}
