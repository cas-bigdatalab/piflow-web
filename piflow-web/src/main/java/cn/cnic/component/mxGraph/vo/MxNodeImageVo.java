package cn.cnic.component.mxGraph.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class MxNodeImageVo implements Serializable {

    private static final long serialVersionUID = -5345043212647460732L;

    private String id;
    private String imageName;
    private String imagePath;
    private String imageUrl;

}
