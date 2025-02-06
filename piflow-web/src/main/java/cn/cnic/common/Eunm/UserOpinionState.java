package cn.cnic.common.Eunm;

public enum UserOpinionState {
    TO_FINISH(0,"待解决"),
    FINISH(1,"已解决");

    private final Integer value;
    private final String description;

    //
    private UserOpinionState(Integer value, String description ) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public static UserOpinionState fromValue(Integer value){
        for (UserOpinionState productTypeAssociateType : UserOpinionState.values()) {
            if(productTypeAssociateType.getValue() == value){
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static UserOpinionState formName(String name){
        try {
            return UserOpinionState.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
