package com.nature.common.executor;

import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServicesExecutor {

    private static Logger logger = LoggerUtil.getLogger();

    /* 定义一个执行线程的执行器用于服务 */
    private static ExecutorService servicesExecutorService;

    /* 定义一个执行线程的执行器用于日志 */
    private static ExecutorService          logExecutorService;

    private static ScheduledExecutorService scheduledThreadPool;

    /* 初始化线程池 */
    static {
        initServicesExecutorService();
        //initLogExecutorService();
        //initScheduledThreadPool();
    }

    private synchronized static void initServicesExecutorService() {
        /**
         * 创建固定固定线程数的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。 线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程。
         * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()
         */
        servicesExecutorService = null;
        servicesExecutorService = Executors.newFixedThreadPool(2);
        logger.info("异步同步线程池初始化完毕...");
    }

    private synchronized static void initLogExecutorService() {
        /**
         * 创建固定固定线程数的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。 线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程。
         */
        logExecutorService = null;
        logExecutorService = Executors.newFixedThreadPool(4);
        logger.info("异步日志处理线程池初始化完毕...");

        // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        // ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    }

    private synchronized static void initScheduledThreadPool() {
        /**
         * 创建一个定长线程池，支持定时及周期性任务执行
         */
        scheduledThreadPool = null;
        scheduledThreadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 1);

    }

    /**
     * 得到执行器实例，用于关闭该执行器时调用或者其他地方调用
     *
     * @return ExecutorService
     */
    public static ExecutorService getServicesExecutorServiceService() {
        if (null == servicesExecutorService || servicesExecutorService.isShutdown()) {
            initServicesExecutorService();
        }
        return servicesExecutorService;
    }

    public static ExecutorService getLogExecutorService() {
        if (null == logExecutorService || logExecutorService.isShutdown()) {
            initLogExecutorService();
        }
        return logExecutorService;
    }

    public static ScheduledExecutorService getScheduledServicesExecutorServiceService() {
        if (null == scheduledThreadPool || scheduledThreadPool.isShutdown()) {
            initScheduledThreadPool();
        }
        return scheduledThreadPool;
    }

}
