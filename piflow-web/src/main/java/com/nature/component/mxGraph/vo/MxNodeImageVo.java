package com.nature.component.mxGraph.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class MxNodeImageVo implements Serializable {

    private String id;
    private String imageName;
    private String imagePath;
    private String imageUrl;

}
