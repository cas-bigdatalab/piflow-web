package cn.cnic.component.mxGraph.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class MxGraphComponentVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String component_prefix;

    private String addImagePaletteId;

    private String component_name;

    private List<COMPONENT_GROUP> component_group;

    @Getter
    @Setter
    public static class COMPONENT_GROUP implements Serializable {

        private static final long serialVersionUID = 1L;

        private String img_name;
        private String name;
        private String img_type;
        private String description;
    }

}
