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
public class File extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileType;
    private String filePath;
    private Integer associateType; //关联类型 0-数据产品类型  1-数据产品 2-数据产品封面 3-流水线发布参数 4-流水线
    private String associateId;
}
