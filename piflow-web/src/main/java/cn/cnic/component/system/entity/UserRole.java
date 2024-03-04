package cn.cnic.component.system.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.flow.entity.FlowGlobalParams;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserRole extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;
}
