package cn.cnic.base.utils;

import cn.cnic.base.vo.StatefulRtnBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatefulRtnBaseUtils {

    /**
     * set Failure information
     *
     * @param errorMsg
     * @return
     */
    public static StatefulRtnBase setFailedMsg(String errorMsg) {
        StatefulRtnBase statefulRtnBase = new cn.cnic.base.vo.StatefulRtnBase();
        log.info(errorMsg);
        statefulRtnBase.setReqRtnStatus(false);
        statefulRtnBase.setErrorCode(statefulRtnBase.ERRCODE_FAIL);
        statefulRtnBase.setErrorMsg(errorMsg);
        return statefulRtnBase;
    }

    /**
     * set Success message
     *
     * @param SuccessdMsg
     * @return
     */
    public static StatefulRtnBase setSuccessdMsg(String SuccessdMsg) {
        StatefulRtnBase statefulRtnBase = new cn.cnic.base.vo.StatefulRtnBase();
        statefulRtnBase.setReqRtnStatus(true);
        statefulRtnBase.setErrorCode(statefulRtnBase.ERRCODE_SUCCESS);
        statefulRtnBase.setErrorMsg(SuccessdMsg);
        return statefulRtnBase;
    }
}
