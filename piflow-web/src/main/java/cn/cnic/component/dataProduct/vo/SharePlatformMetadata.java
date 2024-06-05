package cn.cnic.component.dataProduct.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
/**
 * SharePlatformMetadataDTO
 */
public class SharePlatformMetadata {
    private String id; // 主键id
    private String metadataFilePath; // 元数据文件路径

    private String iconPath; // 图标地址(本地)
    private String documentationPath; // 文档地址(相对)
    private int reviewStatus; // 状态 0-无效 1-已编辑,尚未提交 2-已提交,审核中 3-已提交,审核不通过,需修改 4-已发布
    private String productUrl; //发布的url,未发布时为空
    private Date crtDttm = new Date();
    private Date lastUpdatedDttm = new Date();


}
