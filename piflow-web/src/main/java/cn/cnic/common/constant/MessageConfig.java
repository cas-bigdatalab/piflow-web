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
    @Value("${msg.base_error}")
    private void setErrorMsg(String errorMsg) {
        ERROR_MSG = errorMsg;
    }
    @Value("${msg.base_error.zh}")
    private void setErrorMsgZh(String errorMsgZh) {
        ERROR_MSG_ZH = errorMsgZh;
    }
    public static String ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return ERROR_MSG_ZH;
        }
        return ERROR_MSG;
    }

    private static String SUCCEEDED_MSG;
    private static String SUCCEEDED_MSG_ZH;
    @Value("${msg.base_succeeded}")
    private void setSucceededMsg(String succeededMsg) {
        SUCCEEDED_MSG = succeededMsg;
    }
    @Value("${msg.base_succeeded.zh}")
    private void setSucceededMsgZh(String succeededMsgZh) {
        SUCCEEDED_MSG_ZH = succeededMsgZh;
    }
    public static String SUCCEEDED_MSG(){
        if("ZH".equals(LANGUAGE)){
            return SUCCEEDED_MSG_ZH;
        }
        return SUCCEEDED_MSG;
    }

    private static String ADD_ERROR_MSG;
    private static String ADD_ERROR_MSG_ZH;
    @Value("${msg.add_error}")
    private void setAddErrorMsg(String addErrorMsg) {
        ADD_ERROR_MSG = addErrorMsg;
    }
    @Value("${msg.add_error.zh}")
    private void setAddErrorMsgZh(String addErrorMsgZh) {
        ADD_ERROR_MSG_ZH = addErrorMsgZh;
    }
    public static String ADD_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return ADD_ERROR_MSG_ZH;
        }
        return ADD_ERROR_MSG;
    }

    private static String ADD_SUCCEEDED_MSG;
    private static String ADD_SUCCEEDED_MSG_ZH;
    @Value("${msg.add_succeeded}")
    private void setAddSucceededMsg(String addSucceededMsg) {
        ADD_SUCCEEDED_MSG = addSucceededMsg;
    }
    @Value("${msg.add_succeeded.zh}")
    private void setAddSucceededMsgZh(String addSucceededMsgZh) {
        ADD_SUCCEEDED_MSG_ZH = addSucceededMsgZh;
    }
    public static String ADD_SUCCEEDED_MSG(){
        if("ZH".equals(LANGUAGE)){
            return ADD_SUCCEEDED_MSG_ZH;
        }
        return ADD_SUCCEEDED_MSG;
    }

    private static String UPDATE_ERROR_MSG;
    private static String UPDATE_ERROR_MSG_ZH;
    @Value("${msg.update_error}")
    private void setUpdateErrorMsg(String updateErrorMsg) {
        UPDATE_ERROR_MSG = updateErrorMsg;
    }
    @Value("${msg.update_error.zh}")
    private void setUpdateErrorMsgZh(String updateErrorMsgZh) {
        UPDATE_ERROR_MSG_ZH = updateErrorMsgZh;
    }
    public static String UPDATE_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return UPDATE_ERROR_MSG_ZH;
        }
        return UPDATE_ERROR_MSG;
    }

    private static String UPDATE_SUCCEEDED_MSG;
    private static String UPDATE_SUCCEEDED_MSG_ZH;
    @Value("${msg.update_succeeded}")
    private void setUpdateSucceededMsg(String updateSucceededMsg) {
        UPDATE_SUCCEEDED_MSG = updateSucceededMsg;
    }
    @Value("${msg.update_succeeded.zh}")
    private void setUpdateSucceededMsgZh(String updateSucceededMsgZh) {
        UPDATE_SUCCEEDED_MSG_ZH = updateSucceededMsgZh;
    }
    public static String UPDATE_SUCCEEDED_MSG(){
        if("ZH".equals(LANGUAGE)){
            return UPDATE_SUCCEEDED_MSG_ZH;
        }
        return UPDATE_SUCCEEDED_MSG;
    }

    private static String DELETE_ERROR_MSG;
    private static String DELETE_ERROR_MSG_ZH;
    @Value("${msg.delete_error}")
    private void setDeleteErrorMsg(String deleteErrorMsg) {
        DELETE_ERROR_MSG = deleteErrorMsg;
    }
    @Value("${msg.delete_error.zh}")
    private void setDeleteErrorMsgZh(String deleteErrorMsgZh) {
        DELETE_ERROR_MSG_ZH = deleteErrorMsgZh;
    }
    public static String DELETE_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return DELETE_ERROR_MSG_ZH;
        }
        return DELETE_ERROR_MSG;
    }

    private static String DELETE_LINK_SCHEDULED_ERROR_MSG;
    private static String DELETE_LINK_SCHEDULED_ERROR_MSG_ZH;
    @Value("${msg.delete_link_scheduled_error}")
    private void setDeleteLinkScheduledErrorMsg(String deleteLinkScheduledErrorMsg) {
        DELETE_LINK_SCHEDULED_ERROR_MSG = deleteLinkScheduledErrorMsg;
    }
    @Value("${msg.delete_link_scheduled_error.zh}")
    private void setDeleteLinkScheduledErrorMsgZh(String deleteLinkScheduledErrorMsgZh) {
        DELETE_LINK_SCHEDULED_ERROR_MSG_ZH = deleteLinkScheduledErrorMsgZh;
    }
    public static String DELETE_LINK_SCHEDULED_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return DELETE_LINK_SCHEDULED_ERROR_MSG_ZH;
        }
        return DELETE_LINK_SCHEDULED_ERROR_MSG;
    }

    private static String DELETE_SUCCEEDED_MSG;
    private static String DELETE_SUCCEEDED_MSG_ZH;
    @Value("${msg.delete_succeeded}")
    private void setDeleteSucceededMsg(String deleteSucceededMsg) {
        DELETE_SUCCEEDED_MSG = deleteSucceededMsg;
    }
    @Value("${msg.delete_succeeded.zh}")
    private void setDeleteSucceededMsgZh(String deleteSucceededMsgZh) {
        DELETE_SUCCEEDED_MSG_ZH = deleteSucceededMsgZh;
    }
    public static String DELETE_SUCCEEDED_MSG(){
        if("ZH".equals(LANGUAGE)){
            return DELETE_SUCCEEDED_MSG_ZH;
        }
        return DELETE_SUCCEEDED_MSG;
    }

    private static String NO_DATA_UPDATE_MSG;
    private static String NO_DATA_UPDATE_MSG_ZH;
    @Value("${msg.no_data_update}")
    private void setNoDataUpdateMsg(String noDataUpdateMsg) {
        NO_DATA_UPDATE_MSG = noDataUpdateMsg;
    }
    @Value("${msg.no_data_update.zh}")
    private void setNoDataUpdateMsgZh(String noDataUpdateMsgZh) {
        NO_DATA_UPDATE_MSG_ZH = noDataUpdateMsgZh;
    }
    public static String NO_DATA_UPDATE_MSG(){
        if("ZH".equals(LANGUAGE)){
            return NO_DATA_UPDATE_MSG_ZH;
        }
        return NO_DATA_UPDATE_MSG;
    }

    private static String DATA_PROPERTY_IS_NULL_MSG;
    private static String DATA_PROPERTY_IS_NULL_MSG_ZH;
    @Value("${msg.data_property_is_null}")
    private void setDataPropertyIsNullMsg(String dataErrorMsg) {
        DATA_PROPERTY_IS_NULL_MSG = dataErrorMsg;
    }
    @Value("${msg.data_property_is_null.zh}")
    private void setDataPropertyIsNullMsgZh(String dataErrorMsgZh) {
        DATA_PROPERTY_IS_NULL_MSG_ZH = dataErrorMsgZh;
    }
    public static String DATA_PROPERTY_IS_NULL_MSG(String name){
        if("ZH".equals(LANGUAGE)){
            return String.format(DATA_PROPERTY_IS_NULL_MSG_ZH, name);
        }
        return String.format(DATA_PROPERTY_IS_NULL_MSG, name);
    }

    private static String NO_PERMISSION_MSG;
    private static String NO_PERMISSION_MSG_ZH;
    @Value("${msg.no_permission}")
    private void setNoPermissionMsg(String noPermissionMsg) {
        NO_PERMISSION_MSG = noPermissionMsg;
    }
    @Value("${msg.no_permission.zh}")
    private void setNoPermissionMsgZh(String noPermissionMsgZh) {
        NO_PERMISSION_MSG_ZH = noPermissionMsgZh;
    }
    public static String NO_PERMISSION_MSG(){
        if("ZH".equals(LANGUAGE)){
            return NO_PERMISSION_MSG_ZH;
        }
        return NO_PERMISSION_MSG;
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
    public static String ILLEGAL_OPERATION_MSG(){
        if("ZH".equals(LANGUAGE)){
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
    public static String ILLEGAL_USER_MSG(){
        if("ZH".equals(LANGUAGE)){
            return ILLEGAL_USER_MSG_ZH;
        }
        return ILLEGAL_USER_MSG;
    }

    private static String NO_DATA_XXX_IS_NULL_MSG;
    private static String NO_DATA_XXX_IS_NULL_MSG_ZH;
    @Value("${msg.no_data_xxx_is_null}")
    private void setNoDataXxxIsNullMsg(String noDataMsg) {
        NO_DATA_XXX_IS_NULL_MSG = noDataMsg;
    }
    @Value("${msg.no_data_xxx_is_null.zh}")
    private void setNoDataXxxIsNullMsgZh(String noDataMsgZh) {
        NO_DATA_XXX_IS_NULL_MSG_ZH = noDataMsgZh;
    }
    public static String NO_DATA_XXX_IS_NULL_MSG(String xxx){
        if("ZH".equals(LANGUAGE)){
            return String.format(NO_DATA_XXX_IS_NULL_MSG_ZH, xxx);
        }
        return String.format(NO_DATA_XXX_IS_NULL_MSG, xxx);
    }

    private static String NO_PATH_DATA_MSG;
    private static String NO_PATH_DATA_MSG_ZH;
    @Value("${msg.no_path_data}")
    private void setNoPathDataMsg(String noPathDataMsg) {
        NO_PATH_DATA_MSG = noPathDataMsg;
    }
    @Value("${msg.no_path_data.zh}")
    private void setNoPathDataMsgZh(String noPathDataMsgZh) {
        NO_PATH_DATA_MSG_ZH = noPathDataMsgZh;
    }
    public static String NO_PATH_DATA_MSG(){
        if("ZH".equals(LANGUAGE)){
            return NO_PATH_DATA_MSG_ZH;
        }
        return NO_PATH_DATA_MSG;
    }

    private static String NO_DATA_MSG;
    private static String NO_DATA_MSG_ZH;
    @Value("${msg.no_data}")
    private void setNoDataMsg(String noDataMsg) {
        NO_DATA_MSG = noDataMsg;
    }
    @Value("${msg.no_data.zh}")
    private void setNoDataMsgZh(String noDataMsgZh) {
        NO_DATA_MSG_ZH = noDataMsgZh;
    }
    public static String NO_DATA_MSG(){
        if("ZH".equals(LANGUAGE)){
            return NO_DATA_MSG_ZH;
        }
        return NO_DATA_MSG;
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
    public static String NO_DATA_BY_ID_XXX_MSG(String xxx){
        if("ZH".equals(LANGUAGE)){
            return String.format(NO_DATA_BY_ID_XXX_MSG_ZH, xxx);
        }
        return String.format(NO_DATA_BY_ID_XXX_MSG, xxx);
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
    public static String UPLOAD_FAILED_FILE_EMPTY_MSG(){
        if("ZH".equals(LANGUAGE)){
            return UPLOAD_FAILED_FILE_EMPTY_MSG_ZH;
        }
        return UPLOAD_FAILED_FILE_EMPTY_MSG;
    }

    private static String UPLOAD_FAILED_MSG;
    private static String UPLOAD_FAILED_MSG_ZH;
    @Value("${msg.upload_failed}")
    private void setUploadFailedMsg(String uploadFailedMsg) {
        UPLOAD_FAILED_MSG = uploadFailedMsg;
    }
    @Value("${msg.upload_failed.zh}")
    private void setUploadFailedMsgZh(String uploadFailedMsgZh) {
        UPLOAD_FAILED_MSG_ZH = uploadFailedMsgZh;
    }
    public static String UPLOAD_FAILED_MSG(){
        if("ZH".equals(LANGUAGE)){
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
    public static String PARAM_IS_NULL_MSG(String param){
        if("ZH".equals(LANGUAGE)){
            return String.format(PARAM_IS_NULL_MSG_ZH, param);
        }
        return String.format(PARAM_IS_NULL_MSG, param);
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
    public static String PARAM_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return PARAM_ERROR_MSG_ZH;
        }
        return PARAM_ERROR_MSG;
    }

    private static String NO_XXX_NODE_MSG;
    private static String NO_XXX_NODE_MSG_ZH;
    @Value("${msg.no_xxx_node}")
    private void setNoXxxNodeMsg(String noFlowNodeMsg) {
        NO_XXX_NODE_MSG = noFlowNodeMsg;
    }
    @Value("${msg.no_xxx_node.zh}")
    private void setNoXxxNodeMsgZh(String noFlowNodeMsgZh) {
        NO_XXX_NODE_MSG_ZH = noFlowNodeMsgZh;
    }
    public static String NO_XXX_NODE_MSG(String xxx){
        if("ZH".equals(LANGUAGE)){
            return String.format(NO_XXX_NODE_MSG_ZH, xxx);
        }
        return String.format(NO_XXX_NODE_MSG, xxx);
    }

    private static String DUPLICATE_NAME_MSG;
    private static String DUPLICATE_NAME_MSG_ZH;
    @Value("${msg.duplicate_name}")
    private void setDuplicateNameMsg(String duplicateNameMsg) {
        DUPLICATE_NAME_MSG = duplicateNameMsg;
    }
    @Value("${msg.duplicate_name.zh}")
    private void setDuplicateNameMsgZh(String duplicateNameMsgZh) {
        DUPLICATE_NAME_MSG_ZH = duplicateNameMsgZh;
    }
    public static String DUPLICATE_NAME_MSG(String name){
        if("ZH".equals(LANGUAGE)){
            return String.format(DUPLICATE_NAME_MSG_ZH, name);
        }
        return String.format(DUPLICATE_NAME_MSG, name);
    }

    private static String DUPLICATE_NAME_PLEASE_MODIFY_MSG;
    private static String DUPLICATE_NAME_PLEASE_MODIFY_MSG_ZH;
    @Value("${msg.duplicate_name_please_modify}")
    private void setDuplicateNamePleaseModifyMsg(String duplicateNamePleaseModifyMsg) {
        DUPLICATE_NAME_PLEASE_MODIFY_MSG = duplicateNamePleaseModifyMsg;
    }
    @Value("${msg.duplicate_name_please_modify.zh}")
    private void setDuplicateNamePleaseModifyMsgZh(String duplicateNamePleaseModifyMsgZh) {
        DUPLICATE_NAME_PLEASE_MODIFY_MSG_ZH = duplicateNamePleaseModifyMsgZh;
    }
    public static String DUPLICATE_NAME_PLEASE_MODIFY_MSG(String name){
        if("ZH".equals(LANGUAGE)){
            return String.format(DUPLICATE_NAME_PLEASE_MODIFY_MSG_ZH, name);
        }
        return String.format(DUPLICATE_NAME_PLEASE_MODIFY_MSG, name);
    }

    private static String XXX_AVAILABLE_MSG;
    private static String XXX_AVAILABLE_MSG_ZH;
    @Value("${msg.xxx_available}")
    private void setXxxAvailableMsg(String duplicateNameMsg) {
        XXX_AVAILABLE_MSG = duplicateNameMsg;
    }
    @Value("${msg.xxx_available.zh}")
    private void setXxxAvailableMsgZh(String duplicateNameMsgZh) {
        XXX_AVAILABLE_MSG_ZH = duplicateNameMsgZh;
    }
    public static String XXX_AVAILABLE_MSG(String name){
        if("ZH".equals(LANGUAGE)){
            return String.format(XXX_AVAILABLE_MSG_ZH, name);
        }
        return String.format(XXX_AVAILABLE_MSG, name);
    }

    private static String XXX_ALREADY_TAKEN_MSG;
    private static String XXX_ALREADY_TAKEN_MSG_ZH;
    @Value("${msg.xxx_already_taken}")
    private void setXxxAlreadyTakenMsg(String duplicateNameMsg) {
        XXX_ALREADY_TAKEN_MSG = duplicateNameMsg;
    }
    @Value("${msg.xxx_already_taken.zh}")
    private void setXxxAlreadyTakenMsgZh(String duplicateNameMsgZh) {
        XXX_ALREADY_TAKEN_MSG_ZH = duplicateNameMsgZh;
    }
    public static String XXX_ALREADY_TAKEN_MSG(String name){
        if("ZH".equals(LANGUAGE)){
            return String.format(XXX_ALREADY_TAKEN_MSG_ZH, name);
        }
        return String.format(XXX_ALREADY_TAKEN_MSG, name);
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
    public static String CONVERSION_FAILED_MSG(){
        if("ZH".equals(LANGUAGE)){
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
    public static String INTERFACE_RETURN_VALUE_IS_NULL_MSG(){
        if("ZH".equals(LANGUAGE)){
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
    public static String INTERFACE_CALL_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return INTERFACE_CALL_ERROR_MSG_ZH;
        }
        return INTERFACE_CALL_ERROR_MSG;
    }

    private static String INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG;
    private static String INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG_ZH;
    @Value("${msg.interface_call_succeeded_save_error}")
    private void setInterfaceCallSucceededSaveErrorMsg(String interfaceCallSucceededSaveErrorMsg) {
        INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG = interfaceCallSucceededSaveErrorMsg;
    }
    @Value("${msg.interface_call_succeeded_save_error.zh}")
    private void setInterfaceCallSucceededSaveErrorMsgZh(String interfaceCallSucceededSaveErrorMsgZh) {
        INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG_ZH = interfaceCallSucceededSaveErrorMsgZh;
    }
    public static String INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG_ZH;
        }
        return INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG;
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
    public static String INIT_COMPONENTS_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
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
    public static String INIT_COMPONENTS_COMPLETED_MSG(){
        if("ZH".equals(LANGUAGE)){
            return INIT_COMPONENTS_COMPLETED_MSG_ZH;
        }
        return INIT_COMPONENTS_COMPLETED_MSG;
    }

    private static String SCHEDULED_TASK_ERROR_MSG;
    private static String SCHEDULED_TASK_ERROR_MSG_ZH;
    @Value("${msg.scheduled_task_error}")
    private void setScheduledTaskErrorMsg(String scheduledTaskErrorMsg) {
        SCHEDULED_TASK_ERROR_MSG = scheduledTaskErrorMsg;
    }
    @Value("${msg.scheduled_task_error.zh}")
    private void setScheduledTaskErrorMsgZh(String scheduledTaskErrorMsgZh) {
        SCHEDULED_TASK_ERROR_MSG_ZH = scheduledTaskErrorMsgZh;
    }
    public static String SCHEDULED_TASK_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return SCHEDULED_TASK_ERROR_MSG_ZH;
        }
        return SCHEDULED_TASK_ERROR_MSG;
    }

    private static String SCHEDULED_TYPE_OR_DATA_ERROR_MSG;
    private static String SCHEDULED_TYPE_OR_DATA_ERROR_MSG_ZH;
    @Value("${msg.scheduled_type_or_data_error}")
    private void setScheduledTypeOrDataErrorMsg(String scheduledTypeOrDataErrorMsg) {
        SCHEDULED_TYPE_OR_DATA_ERROR_MSG = scheduledTypeOrDataErrorMsg;
    }
    @Value("${msg.scheduled_type_or_data_error.zh}")
    private void setScheduledTypeOrDataErrorMsgZh(String scheduledTypeOrDataErrorMsgZh) {
        SCHEDULED_TYPE_OR_DATA_ERROR_MSG_ZH = scheduledTypeOrDataErrorMsgZh;
    }
    public static String SCHEDULED_TYPE_OR_DATA_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return SCHEDULED_TYPE_OR_DATA_ERROR_MSG_ZH;
        }
        return SCHEDULED_TYPE_OR_DATA_ERROR_MSG;
    }

    private static String LOAD_TYPE_ERROR_MSG;
    private static String LOAD_TYPE_ERROR_MSG_ZH;
    @Value("${msg.load_type_error}")
    private void setLoadTypeErrorMsg(String loadTypeErrorMsg) {
        LOAD_TYPE_ERROR_MSG = loadTypeErrorMsg;
    }
    @Value("${msg.load_type_error.zh}")
    private void setLoadTypeErrorMsgZh(String loadTypeErrorMsgZh) {
        LOAD_TYPE_ERROR_MSG_ZH = loadTypeErrorMsgZh;
    }
    public static String LOAD_TYPE_ERROR_MSG(){
        if("ZH".equals(LANGUAGE)){
            return LOAD_TYPE_ERROR_MSG_ZH;
        }
        return LOAD_TYPE_ERROR_MSG;
    }




}