package cn.cnic.common.Eunm;

/**
 * 数据源审核状态(资源共享服务平台)
 */
public enum DataProductMetaDataStatus {
    UNKNWON(0),
    EDITED(1),       //已编辑,尚未提交
    REVIEWED(2),     //已提交,待审核
    NEEDCHANGED(3),     //已提交,审核不通过,需修改
    POSTED(4);       //已发布

    private final Integer value;

    private DataProductMetaDataStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }


}
