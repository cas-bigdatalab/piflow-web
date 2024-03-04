package cn.cnic.common.Eunm;

public enum FileAssociateType {
    DATA_PRODUCT_TYPE_COVER(0),//数据产品类型封面 唯一
    DATA_PRODUCT(1),//数据产品 唯一
    DATA_PRODUCT_COVER(2),//数据产品封面 唯一
    FLOW_PUBLISHING_PROPERTY_TEMPLATE(3),//流水线发布参数的输入样例文件，唯一
    FLOW_PUBLISHING_PROPERTY(4),//流水线发布参数，供运行发布流水线时记录上传的输入文件，不唯一
    FLOW_PUBLISHING_COVER(5),//流水线发布封面 唯一
    FLOW_PUBLISHING_INSTRUCTION(6);//流水线发布说明书 唯一

    private final Integer value;

    //
    private FileAssociateType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static FileAssociateType fromValue(Integer value){
        for (FileAssociateType productTypeAssociateType : FileAssociateType.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FileAssociateType formName(String name){
        try {
            return FileAssociateType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
//    public static ProductTypeAssociateType selectGender(String name) {
//        for (ProductTypeAssociateType portType : ProductTypeAssociateType.values()) {
//            if (name.equalsIgnoreCase(portType.name())) {
//                return portType;
//            }
//        }
//        return null;
//    }
}
