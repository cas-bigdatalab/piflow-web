package cn.cnic.common.Eunm;

public enum EcosystemTypeAssociateType {
    FLOW(0),
    DATA_PRODUCT(1),
    USER(2);

    private final Integer value;

    //
    private EcosystemTypeAssociateType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static EcosystemTypeAssociateType fromValue(Integer value){
        for (EcosystemTypeAssociateType productTypeAssociateType : EcosystemTypeAssociateType.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static EcosystemTypeAssociateType formName(String name){
        try {
            return EcosystemTypeAssociateType.valueOf(name);
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
