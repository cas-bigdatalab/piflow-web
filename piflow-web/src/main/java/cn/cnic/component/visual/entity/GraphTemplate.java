package cn.cnic.component.visual.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * author:hmh
 * date:2024-02-02
 */
@Data
@NoArgsConstructor
@TableName(value="vis_graph_template")
public class GraphTemplate{
    /** id号 */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 数据库id
     */
    private Integer dataBaseId;
//    /** 数据类型（mysql、excel） */
    private String type ;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 图表名称
     */
    private String tableName;
    /** excel表名关联id */
    private Integer excelAssoId ;
    /**
     * 模板描述
     */
    private String description;
    /** sheet页名称 */
    private String sheetName ;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
}