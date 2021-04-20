package cn.cnic.component.stopsComponent.vo;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Group name table
 */
@Setter
@Getter
public class StopsComponentGroupVo {

    private String id; // Group id
    private String groupName; // Group name

    private List<StopsComponentVo> stopsComponentVoList = new ArrayList<StopsComponentVo>();
}
