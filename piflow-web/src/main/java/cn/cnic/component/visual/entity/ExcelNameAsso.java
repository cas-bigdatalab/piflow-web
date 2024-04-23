package cn.cnic.component.visual.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * author:hmh
 * date:2024-03-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value="vis_excel_name_asso")
public class ExcelNameAsso {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    /** excel文件名 */
    private String excelName ;
    /** 关联表名 */
    private String assoName ;
    /** 创建时间 */
    private String createTime ;
    /** 更新时间 */
    private String updateTime ;
    /** 备用字段1 */
    private String reserve1 ;
    /** 备用字段1 */
    private String reserve2 ;
}
