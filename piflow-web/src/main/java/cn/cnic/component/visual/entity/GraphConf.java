package cn.cnic.component.visual.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * author:hmh
 * date:2023-09-25
 */
@Data
@NoArgsConstructor
@TableName(value="vis_graph_conf")
public class GraphConf{
    /** id号 */
    @TableId(type = IdType.AUTO)
    private Integer id ;
    /** 图表配置名称 */
    private String name ;
    /** 图表配置描述 */
    private String description ;
    /** 图表模板id */
    private Integer graphTemplateId ;
    /** 具体配置信息 */
    private String configInfo ;
    /** 创建时间 */
    private String createTime ;
    /** 更新时间 */
    private String updateTime ;
    //判断是新增还是修改标记（add为新增，update为修改）
    @TableField(exist = false)
    private String addFlag ;
}
