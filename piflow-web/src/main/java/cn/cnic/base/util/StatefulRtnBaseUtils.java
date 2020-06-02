package cn.cnic.base.util;

import cn.cnic.base.vo.StatefulRtnBase;
import org.slf4j.Logger;

public class StatefulRtnBaseUtils {

    private static Logger logger = LoggerUtil.getLogger();

    /**
     * set Failure information
     *
     * @param errorMsg
     * @return
     */
    public static StatefulRtnBase setFailedMsg(String errorMsg) {
        StatefulRtnBase statefulRtnBase = new cn.cnic.base.vo.StatefulRtnBase();
        logger.info(errorMsg);
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
