package cn.cnic.component.flow.vo;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.component.flow.entity.Stops;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * stop component table
 */
@Getter
@Setter
public class FlowStopsPublishingVo {

    private static final long serialVersionUID = 1L;

    private String id;
    private String publishingId;
    private String name;
    private String state;
    private StopsVo stopsVo;
}
