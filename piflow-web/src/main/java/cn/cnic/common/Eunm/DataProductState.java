package cn.cnic.common.Eunm;

public enum DataProductState {
    DELETED(0,"已删除"),
    CREATING(1,"生成中"),
    CREATE_FAILED(2,"生成失败"),
    TO_PUBLISH(3,"待发布"),
    TO_CHECK(4,"待审核"),
    PUBLISHED(5,"已发布"),
    REJECT_PUBLISH(6,"拒绝发布"),
    DOWN(7,"已下架");

    private final Integer value;
    private final String description;

    //
    private DataProductState(Integer value,String description ) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static DataProductState fromValue(Integer value){
        for (DataProductState productTypeAssociateType : DataProductState.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static DataProductState formName(String name){
        try {
            return DataProductState.valueOf(name);
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
