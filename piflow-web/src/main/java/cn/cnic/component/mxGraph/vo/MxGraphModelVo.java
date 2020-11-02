package cn.cnic.component.mxGraph.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MxGraphModelVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String dx;

    private String dy;

    private String grid;

    private String gridSize;

    private String guides;

    private String tooltips;

    private String connect;

    private String arrows;

    private String fold;

    private String page;

    private String pageScale;

    private String pageWidth;

    private String pageHeight;

    private String background;

    private List<MxCellVo> rootVo = new ArrayList<MxCellVo>();

}
