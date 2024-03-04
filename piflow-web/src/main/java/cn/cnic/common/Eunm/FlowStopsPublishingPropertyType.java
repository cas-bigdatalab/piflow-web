package cn.cnic.common.Eunm;

public enum FlowStopsPublishingPropertyType {
    FILE(0),
    COMMON(1),
    OUTPUT(2);

    private final Integer value;

    //
    private FlowStopsPublishingPropertyType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static FlowStopsPublishingPropertyType fromValue(Integer value){
        for (FlowStopsPublishingPropertyType productTypeAssociateType : FlowStopsPublishingPropertyType.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FlowStopsPublishingPropertyType formName(String name){
        try {
            return FlowStopsPublishingPropertyType.valueOf(name);
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
