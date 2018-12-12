package com.nature.base.util;

import com.nature.base.vo.StatefulRtnBase;
import org.slf4j.Logger;

public class StatefulRtnBaseUtils {

    private static Logger logger = LoggerUtil.getLogger();
    /**
     * set失败信息
     *
     * @param errMsg
     * @return
     */
    public static StatefulRtnBase setFailedMsg(String errMsg) {
        StatefulRtnBase statefulRtnBase = new com.nature.base.vo.StatefulRtnBase();
        logger.info(errMsg);
        statefulRtnBase.setReqRtnStatus(false);
        statefulRtnBase.setErrorCode(statefulRtnBase.ERRCODE_FAIL);
        statefulRtnBase.setErrorMsg(errMsg);
        return statefulRtnBase;
    }

    /**
     * set成功信息
     *
     * @param SuccessdMsg
     * @return
     */
    public static StatefulRtnBase setSuccessdMsg(String SuccessdMsg) {
        StatefulRtnBase statefulRtnBase = new com.nature.base.vo.StatefulRtnBase();
        statefulRtnBase.setReqRtnStatus(true);
        statefulRtnBase.setErrorCode(statefulRtnBase.ERRCODE_SUCCESS);
        statefulRtnBase.setErrorMsg(SuccessdMsg);
        return statefulRtnBase;
    }
}
