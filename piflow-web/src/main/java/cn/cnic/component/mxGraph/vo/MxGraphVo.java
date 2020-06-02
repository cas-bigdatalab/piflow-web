package cn.cnic.component.mxGraph.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MxGraphVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String loadId;

    private List<MxCellVo> mxCellVoList = new ArrayList<>();

}
