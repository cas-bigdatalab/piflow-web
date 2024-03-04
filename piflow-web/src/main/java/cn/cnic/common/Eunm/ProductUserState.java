package cn.cnic.common.Eunm;

public enum ProductUserState {
    INVALID(0,"失效"),
    TO_CHECK(1,"待审核"),
    CHECK_SUCCESS(2,"审核通过"),
    CHECK_REJECT(3,"审核拒绝");

    private final Integer value;
    private final String description;

    //
    private ProductUserState(Integer value, String description ) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static ProductUserState fromValue(Integer value){
        for (ProductUserState productTypeAssociateType : ProductUserState.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static ProductUserState formName(String name){
        try {
            return ProductUserState.valueOf(name);
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
