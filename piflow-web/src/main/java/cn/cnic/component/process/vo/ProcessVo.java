package cn.cnic.component.process.vo;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.vo.BasePageVo;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.serializer.ToLongDeserializer;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ProcessVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Date crtDttm;
    private Date lastUpdateDttm;
    private String name;
    private String driverMemory;
    private String executorNumber;
    private String executorMemory;
    private String executorCores;
    private String description;
    private String flowId;
    private String appId;
    private String parentProcessId;
    private String processId;
    private ProcessState state;
    private Date startTime;
    private Date endTime;
    private String progress;
    private RunModeType runModeType;
    private String pageId;
    private String viewXml;
    private ProcessGroupVo processGroupVo;
    private MxGraphModelVo mxGraphModelVo;
    private List<ProcessStopVo> processStopVoList = new ArrayList<ProcessStopVo>();
    private List<ProcessPathVo> processPathVoList = new ArrayList<ProcessPathVo>();

    private FlowPublishingVo flowPublishingVo;//详情信息
    private List<DataProductVo> dataProductList;//关联的数据产品

    private String keyword;//用于接收关键词查询的关键词
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String publishingId;//用于接收根据发布流水线ID查询

    public String getCrtDttmStr() {
        return DateUtils.dateTimesToStr(this.crtDttm);
    }

    public String getStartTimeStr() {
        return DateUtils.dateTimesToStr(this.startTime);
    }

    public String getEndTimeStr() {
        return DateUtils.dateTimesToStr(this.endTime);
    }
}
