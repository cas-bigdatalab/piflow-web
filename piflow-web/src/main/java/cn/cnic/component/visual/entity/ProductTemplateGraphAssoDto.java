package cn.cnic.component.visual.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName(value = "vis_product_template_graph_asso")
public class ProductTemplateGraphAssoDto {
    /**
     * id号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据产品id
     */
    private String productId;

    /**
     * 所有者,用户名
     */
    private String owner;

    /**
     * 数据类型（mysql-1、excel-0）
     */
    private Integer type;

    /**
     * excel对应的全路径+文件名
     */
    private String path;

    /**
     * vis_graph_templete表id
     */
    private Integer graphTemplateId;

    /**
     * vis_graph_conf表id
     */
    private Integer graphConfId;
    private String createTime;
}
