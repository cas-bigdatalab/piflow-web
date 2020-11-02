package cn.cnic.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    /**
     * Get the current "class" name, call this method directly in all places where the system uses "logger"
     *
     * @return
     */
    public static Logger getLogger() {
        StackTraceElement[] stackEle = new RuntimeException().getStackTrace();
        return LoggerFactory.getLogger(stackEle[1].getClassName());
    }
}
