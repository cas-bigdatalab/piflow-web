package cn.cnic.common.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class MessageConfig {

    public static String LANGUAGE = "EN";

    private static String ERROR_MSG;
    private static String ERROR_MSG_ZH;
    @Value("${msg.base.error}")
    private void setErrorMsg(String errorMsg) {
        ERROR_MSG = errorMsg;
    }
    @Value("${msg.base.error.zh}")
    private void setErrorMsgZh(String errorMsgZh) {
        ERROR_MSG_ZH = errorMsgZh;
    }
    public static String ERROR_MSG(String language){
        if("ZH".equals(language)){
            return ERROR_MSG_ZH;
        }
        return ERROR_MSG;
    }

    private static String SUCCEEDED_MSG;
    private static String SUCCEEDED_MSG_ZH;
    @Value("${msg.base.succeeded}")
    private void setSucceededMsg(String succeededMsg) {
        SUCCEEDED_MSG = succeededMsg;
    }
    @Value("${msg.base.succeeded.zh}")
    private void setSucceededMsgZh(String succeededMsgZh) {
        SUCCEEDED_MSG_ZH = succeededMsgZh;
    }
    public static String SUCCEEDED_MSG(String language){
        if("ZH".equals(language)){
            return SUCCEEDED_MSG_ZH;
        }
        return SUCCEEDED_MSG;
    }

    private static String ILLEGAL_OPERATION_MSG;
    private static String ILLEGAL_OPERATION_MSG_ZH;
    @Value("${msg.illegal_operation}")
    private void setIllegalOperationMsg(String illegalOperationMsg) {
        ILLEGAL_OPERATION_MSG = illegalOperationMsg;
    }
    @Value("${msg.illegal_operation.zh}")
    private void setIllegalOperationMsgZh(String illegalOperationMsgZh) {
        ILLEGAL_OPERATION_MSG_ZH = illegalOperationMsgZh;
    }
    public static String ILLEGAL_OPERATION_MSG(String language){
        if("ZH".equals(language)){
            return ILLEGAL_OPERATION_MSG_ZH;
        }
        return ILLEGAL_OPERATION_MSG;
    }

    private static String ILLEGAL_USER_MSG;
    private static String ILLEGAL_USER_MSG_ZH;
    @Value("${msg.illegal_user}")
    private void setIllegalUserMsg(String illegalUserMsg) {
        ILLEGAL_USER_MSG = illegalUserMsg;
    }
    @Value("${msg.illegal_user.zh}")
    private void setIllegalUserMsgZh(String illegalUserMsgZh) {
        ILLEGAL_USER_MSG_ZH = illegalUserMsgZh;
    }
    public static String ILLEGAL_USER_MSG(String language){
        if("ZH".equals(language)){
            return ILLEGAL_USER_MSG_ZH;
        }
        return ILLEGAL_USER_MSG;
    }

    private static String NO_DATA_BY_ID_XXX_MSG;
    private static String NO_DATA_BY_ID_XXX_MSG_ZH;
    @Value("${msg.no_data_by_id}")
    private void setNoDataByIdXxxMsg(String noDataByIdXxxMsg) {
        NO_DATA_BY_ID_XXX_MSG = noDataByIdXxxMsg;
    }
    @Value("${msg.no_data_by_id.zh}")
    private void setNoDataByIdXxxMsgZh(String noDataByIdXxxMsgZh) {
        NO_DATA_BY_ID_XXX_MSG_ZH = noDataByIdXxxMsgZh;
    }
    public static String NO_DATA_BY_ID_XXX_MSG(String language){
        if("ZH".equals(language)){
            return NO_DATA_BY_ID_XXX_MSG_ZH;
        }
        return NO_DATA_BY_ID_XXX_MSG;
    }

    private static String UPLOAD_FAILED_FILE_EMPTY_MSG;
    private static String UPLOAD_FAILED_FILE_EMPTY_MSG_ZH;
    @Value("${msg.upload_failed_file_empty}")
    private void setUploadFailedFileEmptyMsg(String uploadFailedFileEmptyMsg) {
        UPLOAD_FAILED_FILE_EMPTY_MSG = uploadFailedFileEmptyMsg;
    }
    @Value("${msg.upload_failed_file_empty.zh}")
    private void setUploadFailedFileEmptyMsgZh(String uploadFailedFileEmptyMsgZh) {
        UPLOAD_FAILED_FILE_EMPTY_MSG_ZH = uploadFailedFileEmptyMsgZh;
    }
    public static String UPLOAD_FAILED_FILE_EMPTY(String language){
        if("ZH".equals(language)){
            return UPLOAD_FAILED_FILE_EMPTY_MSG_ZH;
        }
        return UPLOAD_FAILED_FILE_EMPTY_MSG;
    }

    private static String PARAM_IS_NULL_MSG;
    private static String PARAM_IS_NULL_MSG_ZH;
    @Value("${msg.param_is_null}")
    private void setParamIsNullMsg(String paramIsNullMsg) {
        PARAM_IS_NULL_MSG = paramIsNullMsg;
    }
    @Value("${msg.param_is_null.zh}")
    private void setParamIsNullMsgZh(String paramIsNullMsgZh) {
        PARAM_IS_NULL_MSG_ZH = paramIsNullMsgZh;
    }
    public static String PARAM_IS_NULL_MSG(String language){
        if("ZH".equals(language)){
            return PARAM_IS_NULL_MSG_ZH;
        }
        return PARAM_IS_NULL_MSG;
    }

    private static String PARAM_ERROR_MSG;
    private static String PARAM_ERROR_MSG_ZH;
    @Value("${msg.param_error}")
    private void setParamErrorMsg(String paramErrorMsg) {
        PARAM_ERROR_MSG = paramErrorMsg;
    }
    @Value("${msg.param_error.zh}")
    private void setParamErrorMsgZh(String paramErrorMsg) {
        PARAM_ERROR_MSG_ZH = paramErrorMsg;
    }
    public static String PARAM_ERROR_MSG(String language){
        if("ZH".equals(language)){
            return PARAM_ERROR_MSG_ZH;
        }
        return PARAM_ERROR_MSG;
    }

    private static String NO_FLOW_NODE_MSG;
    private static String NO_FLOW_NODE_MSG_ZH;
    @Value("${msg.no_flow_node}")
    private void setNoFlowNodeMsg(String noFlowNodeMsg) {
        NO_FLOW_NODE_MSG = noFlowNodeMsg;
    }
    @Value("${msg.no_flow_node.zh}")
    private void setNoFlowNodeMsgZh(String noFlowNodeMsgZh) {
        NO_FLOW_NODE_MSG_ZH = noFlowNodeMsgZh;
    }
    public static String NO_FLOW_NODE_MSG(String language){
        if("ZH".equals(language)){
            return NO_FLOW_NODE_MSG_ZH;
        }
        return NO_FLOW_NODE_MSG;
    }

    private static String NO_FLOW_GROUP_NODE_MSG;
    private static String NO_FLOW_GROUP_NODE_MSG_ZH;
    @Value("${msg.no_flowGroup_node}")
    private void setNoFlowGroupNodeMsg(String noFlowGroupNodeMsg) {
        NO_FLOW_GROUP_NODE_MSG = noFlowGroupNodeMsg;
    }
    @Value("${msg.no_flowGroup_node.zh}")
    private void setNoFlowGroupNodeMsgZh(String noFlowGroupNodeMsgZh) {
        NO_FLOW_GROUP_NODE_MSG_ZH = noFlowGroupNodeMsgZh;
    }
    public static String NO_FLOW_GROUP_NODE_MSG(String language){
        if("ZH".equals(language)){
            return NO_FLOW_GROUP_NODE_MSG_ZH;
        }
        return NO_FLOW_GROUP_NODE_MSG;
    }

    private static String DUPLICATE_FLOW_NAME_MSG;
    private static String DUPLICATE_FLOW_NAME_MSG_ZH;
    @Value("${msg.duplicate_flow_name}")
    private void setDuplicateFlowNameMsg(String duplicateFlowNameMsg) {
        DUPLICATE_FLOW_NAME_MSG = duplicateFlowNameMsg;
    }
    @Value("${msg.duplicate_flow_name.zh}")
    private void setDuplicateFlowNameMsgZh(String duplicateFlowNameMsgZh) {
        DUPLICATE_FLOW_NAME_MSG_ZH = duplicateFlowNameMsgZh;
    }
    public static String DUPLICATE_FLOW_NAME_MSG(String language){
        if("ZH".equals(language)){
            return DUPLICATE_FLOW_NAME_MSG_ZH;
        }
        return DUPLICATE_FLOW_NAME_MSG;
    }

    private static String DUPLICATE_STOP_NAME_MSG;
    private static String DUPLICATE_STOP_NAME_MSG_ZH;
    @Value("${msg.duplicate_stop_name}")
    private void setDuplicateStopNameMsg(String duplicateStopNameMsg) {
        DUPLICATE_STOP_NAME_MSG = duplicateStopNameMsg;
    }
    @Value("${msg.duplicate_stop_name.zh}")
    private void setDuplicateStopNameMsgZh(String duplicateStopNameMsgZh) {
        DUPLICATE_STOP_NAME_MSG_ZH = duplicateStopNameMsgZh;
    }
    public static String DUPLICATE_STOP_NAME_MSG(String language){
        if("ZH".equals(language)){
            return DUPLICATE_STOP_NAME_MSG_ZH;
        }
        return DUPLICATE_STOP_NAME_MSG;
    }

    private static String CONVERSION_FAILED_MSG;
    private static String CONVERSION_FAILED_MSG_ZH;
    @Value("${msg.conversion_failed}")
    private void setConversionFailedMsg(String conversionFailedMsg) {
        CONVERSION_FAILED_MSG = conversionFailedMsg;
    }
    @Value("${msg.conversion_failed.zh}")
    private void setConversionFailedMsgZh(String conversionFailedMsgZh) {
        CONVERSION_FAILED_MSG_ZH = conversionFailedMsgZh;
    }
    public static String CONVERSION_FAILED_MSG(String language){
        if("ZH".equals(language)){
            return CONVERSION_FAILED_MSG_ZH;
        }
        return CONVERSION_FAILED_MSG;
    }

    private static String INTERFACE_RETURN_VALUE_IS_NULL_MSG;
    private static String INTERFACE_RETURN_VALUE_IS_NULL_MSG_ZH;
    @Value("${msg.interface_return_value_is_null}")
    private void setInterfaceReturnValueIsNullMsg(String interfaceReturnValueIsNullMsg) {
        INTERFACE_RETURN_VALUE_IS_NULL_MSG = interfaceReturnValueIsNullMsg;
    }
    @Value("${msg.interface_return_value_is_null.zh}")
    private void setInterfaceReturnValueIsNullMsgZh(String interfaceReturnValueIsNullMsgZh) {
        INTERFACE_RETURN_VALUE_IS_NULL_MSG_ZH = interfaceReturnValueIsNullMsgZh;
    }
    public static String INTERFACE_RETURN_VALUE_IS_NULL_MSG(String language){
        if("ZH".equals(language)){
            return INTERFACE_RETURN_VALUE_IS_NULL_MSG_ZH;
        }
        return INTERFACE_RETURN_VALUE_IS_NULL_MSG;
    }

    private static String INTERFACE_CALL_ERROR_MSG;
    private static String INTERFACE_CALL_ERROR_MSG_ZH;
    @Value("${msg.interface_call_error}")
    private void setInterfaceCallErrorMsg(String interfaceCallErrorMsg) {
        INTERFACE_CALL_ERROR_MSG = interfaceCallErrorMsg;
    }
    @Value("${msg.interface_call_error.zh}")
    private void setInterfaceCallErrorMsgZh(String interfaceCallErrorMsgZh) {
        INTERFACE_CALL_ERROR_MSG_ZH = interfaceCallErrorMsgZh;
    }
    public static String INTERFACE_CALL_ERROR_MSG(String language){
        if("ZH".equals(language)){
            return INTERFACE_CALL_ERROR_MSG_ZH;
        }
        return INTERFACE_CALL_ERROR_MSG;
    }

    private static String INIT_COMPONENTS_ERROR_MSG;
    private static String INIT_COMPONENTS_ERROR_MSG_ZH;
    @Value("${msg.init_components_error}")
    private void setInitComponentsErrorMsg(String initComponentsErrorMsg) {
        INIT_COMPONENTS_ERROR_MSG = initComponentsErrorMsg;
    }
    @Value("${msg.init_components_error.zh}")
    private void setInitComponentsErrorMsgZh(String initComponentsErrorMsgZh) {
        INIT_COMPONENTS_ERROR_MSG_ZH = initComponentsErrorMsgZh;
    }
    public static String INIT_COMPONENTS_ERROR_MSG(String language){
        if("ZH".equals(language)){
            return INIT_COMPONENTS_ERROR_MSG_ZH;
        }
        return INIT_COMPONENTS_ERROR_MSG;
    }

    private static String INIT_COMPONENTS_COMPLETED_MSG;
    private static String INIT_COMPONENTS_COMPLETED_MSG_ZH;
    @Value("${msg.init_components_completed}")
    private void setInitComponentsCompletedMsg(String initComponentsCompletedMsg) {
        INIT_COMPONENTS_COMPLETED_MSG = initComponentsCompletedMsg;
    }
    @Value("${msg.init_components_completed.zh}")
    private void setInitComponentsCompletedMsgZh(String initComponentsCompletedMsgZh) {
        INIT_COMPONENTS_COMPLETED_MSG_ZH = initComponentsCompletedMsgZh;
    }
    public static String INIT_COMPONENTS_COMPLETED_MSG(String language){
        if("ZH".equals(language)){
            return INIT_COMPONENTS_COMPLETED_MSG_ZH;
        }
        return INIT_COMPONENTS_COMPLETED_MSG;
    }




}