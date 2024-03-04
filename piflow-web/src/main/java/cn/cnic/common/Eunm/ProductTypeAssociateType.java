package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public enum ProductTypeAssociateType {
    FLOW(0),
    DATA_PRODUCT(1),
    USER(2);

    private final Integer value;

    //
    private ProductTypeAssociateType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static ProductTypeAssociateType fromValue(Integer value){
        for (ProductTypeAssociateType productTypeAssociateType : ProductTypeAssociateType.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static ProductTypeAssociateType formName(String name){
        try {
            return ProductTypeAssociateType.valueOf(name);
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
