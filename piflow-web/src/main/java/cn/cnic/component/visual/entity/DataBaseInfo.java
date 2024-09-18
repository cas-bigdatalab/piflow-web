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
 * date:2023-11-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value="vis_dataBase_info")
public class DataBaseInfo {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    /** 数据库名称 */
    private String dbName ;
    /** 数据库描述 */
    private String description ;
    /** 数据库驱动 */
    private String driverClass ;
    /** 数据库地址 */
    private String url ;
    /** 用户名 */
    private String userName ;
    /** 密码 */
    private String password ;
    /** 创建时间 */
    private String createTime ;
    /** 更新时间 */
    private String updateTime ;

}
