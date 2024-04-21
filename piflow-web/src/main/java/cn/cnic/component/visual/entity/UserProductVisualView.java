package cn.cnic.component.visual.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProductVisualView {
    /**
     * id, vis_product_template_graph_asso表的id
     */
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
     * 名称
     */
    private  String name;

    /**
     * 描述信息
     */
    private String description;

    private String createTime;
}
