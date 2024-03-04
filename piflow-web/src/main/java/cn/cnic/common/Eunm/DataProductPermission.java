package cn.cnic.common.Eunm;

public enum DataProductPermission {
    OPEN(0),
    AUTHORIZATION(1);

    private final Integer value;

    //
    private DataProductPermission(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static DataProductPermission fromValue(Integer value){
        for (DataProductPermission productTypeAssociateType : DataProductPermission.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static DataProductPermission formName(String name){
        try {
            return DataProductPermission.valueOf(name);
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
