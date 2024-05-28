package cn.cnic.component.dataProduct.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataProductMetaDataExcelVo {
    private String identifier; // 管理标识符,即数据产品id
    private String doiIdentifier; // DOI标识符
    private String pidIdentifier; // PID标识符
    private String cstrIdentifier; // CSTR标识符
    private String title; // 中文标题
    private String englishTitle; // 英文标题
    private String creationDate; // 生成日期
    private String publishDate; // 最新发布日期
    private String version; // 版本号
    private int fileSize; // 资源大小(MB)
    private int numberOfEntities; // 资源记录数
    private String storageType; // 资源存储类型
    private String storageFormat;  // 资源存储格式
    private String spatialCoverage; // 空间范围
    private String temporalCoverage; // 时间范围
    private String temporalResolution; // 时间分辨率
    private String spatialResolution; // 空间分辨率
    private String subject; // 学科分类
    private String subjectTag; // 分类标签
    private String keywords; // 关键词
    private String abstracts; // 摘要描述
    private String creator; // 生产者
    private String contributor; // 资助项目
    private String financialSupport; // 资助项目
    private String phone; // 联系电话
    private String email; // 联系邮箱
    private String sharePolicy; // 共享方式
    private String protectDuration; // 保护期
    private String citation; // 引用文献
    private String relatedPaperTitle; // 关联论文标题
    private String relatedPaperDOI; // 关联论文DOI
    private String relatedPaperJournal; // 关联论文期刊
    private String relatedPaperAddress; // 关联论文访问地址


    private String documentationAddress; // 数据说明文档地址(相对)

    private String dataSetFile = ""; // 数据集文件地址(相对)
    private String iconAddress; // 图标地址(本地)

}
