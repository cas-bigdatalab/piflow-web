package cn.cnic.common.Eunm;

public enum ProductTypeAssociateState {
    DELETED(0),
    LIKE(1),
    NOT_LIKE(2),
    USABLE(3);

    private final Integer value;

    //
    private ProductTypeAssociateState(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static ProductTypeAssociateState fromValue(Integer value){
        for (ProductTypeAssociateState productTypeAssociateState : ProductTypeAssociateState.values()) {
            if(productTypeAssociateState.getValue() == value){
                return productTypeAssociateState;
            }
        }
        return null;
    }

    public static ProductTypeAssociateState formName(String name){
        try {
            return ProductTypeAssociateState.valueOf(name);
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
