package cn.cnic.component.visual.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProductVisualView {
    /**
     *  vis_product_template_graph_asso表的id
     */
    private Long productVisualId;

    /**
     *  vis_graph_conf表的id
     */
    private Integer graphConfigId;

    /**
     * 数据产品id
     */
    private Long productId;

    /**
     * 数据产品名称
     */
    private String productName;
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
