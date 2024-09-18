package cn.cnic.common.Eunm;

public enum DatasetType {
    EXCEL(0),
    DATABASE(1);

    private final Integer value;

    //
    private DatasetType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static DatasetType fromValue(Integer value){
        for (DatasetType productTypeAssociateType : DatasetType.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static DatasetType formName(String name){
        try {
            return DatasetType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
