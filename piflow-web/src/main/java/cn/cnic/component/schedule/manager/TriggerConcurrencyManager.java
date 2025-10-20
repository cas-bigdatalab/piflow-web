package cn.cnic.component.schedule.manager;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Log4j2
@Component
public class TriggerConcurrencyManager {

    // Key: definitionId, Value: 对应的 Semaphore 实例
    private final ConcurrentHashMap<Long, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    /**
     * 尝试非阻塞地获取一个许可。
     * 如果无法立即获取许可，返回false；否则返回true。
     *
     * @param definitionId      任务定义的唯一ID
     * @param triggerInstanceId 实例id
     * @param concurrencyLimit  此定义配置的并发数
     * @return true表示成功获取许可，false表示无法获取许可
     */
    public boolean tryAcquire(Long definitionId, String triggerInstanceId, Integer concurrencyLimit) {
        // 如果并发数小于等于0，则认为不限制，直接返回true
        if (concurrencyLimit == null || concurrencyLimit <= 0) {
            return true;
        }

        // 使用 computeIfAbsent 原子性地创建 Semaphore，避免并发问题
        Semaphore semaphore = semaphoreMap.computeIfAbsent(definitionId, k -> {
            log.info("为 Definition ID: {} 创建新的 Semaphore，并发限制为: {}", definitionId, concurrencyLimit);
            return new Semaphore(concurrencyLimit, true); // true 表示公平模式
        });

        boolean acquired = semaphore.tryAcquire();
        if (acquired) {
            int permits = semaphore.availablePermits();
            log.info("[SEMAPHORE_TRY_ACQUIRE_SUCCESS] 成功获取许可。definitionId={}, triggerInstanceId={}, availablePermits={}",
                    definitionId, triggerInstanceId, permits);
        } else {
            log.info("[SEMAPHORE_TRY_ACQUIRE_FAIL] 无法获取许可，将保持PENDING状态。definitionId={}, triggerInstanceId={}, availablePermits={}",
                    definitionId, triggerInstanceId, semaphore.availablePermits());
        }
        return acquired;
    }

    /**
     * 为指定的任务定义释放一个许可。
     *
     * @param definitionId     任务定义的唯一ID
     * @param concurrencyLimit 此定义配置的并发数
     */
    public void release(Long definitionId, Integer concurrencyLimit, int runningCount) {
        if (concurrencyLimit == null || concurrencyLimit <= 0) {
            return;
        }

        Semaphore semaphore = semaphoreMap.get(definitionId);
        if (semaphore == null) {
            log.warn("尝试释放许可，但未找到对应的 Semaphore。definitionId={}", definitionId);
            return;
        }

        if (runningCount < concurrencyLimit) {
            semaphore.release();
            log.info("[SEMAPHORE_RELEASE] 任务完成 for Definition ID: {}。当前运行数 ({}) < 限制 ({})。释放一个许可。",
                    definitionId, runningCount, concurrencyLimit);
        } else {
            log.info("[SEMAPHORE_RELEASE] 任务完成 for Definition ID: {}。当前运行数 ({}) >= 限制 ({})。不释放许可，以降低并发。",
                    definitionId, runningCount, concurrencyLimit);
        }
    }

    /**
     * 在系统重启恢复时，根据实际运行状态重建 Semaphore。
     *
     * @param definitionId     任务定义的唯一ID
     * @param concurrencyLimit 此定义配置的并发数
     */
    public void reacquireOnRestart(Long definitionId, int concurrencyLimit, int runningCount) {
        if (concurrencyLimit <= 0) {
            log.warn("并发限制无效 (<=0)，跳过 definitionId={} 的 Semaphore 恢复。", definitionId);
            return;
        }

        // 计算初始时应该有多少可用许可
        int initialPermits = Math.max(0, concurrencyLimit - runningCount);

        // 创建 Semaphore 并设置好初始许可数
        Semaphore semaphore = new Semaphore(initialPermits, true);
        semaphoreMap.put(definitionId, semaphore);

        log.info("恢复 Definition ID: {}。限制: {}, 实际运行中: {}, Semaphore 初始可用许可: {}",
                definitionId, concurrencyLimit, runningCount, initialPermits);
    }

    /**
     * 清理旧的 Semaphore。
     * 在热更新时，先清理旧的 Semaphore，等待一段时间后再创建新的。
     *
     * @param definitionId 任务定义的唯一ID
     */
    public void cleanupOldSemaphore(Long definitionId) {
        Semaphore oldSemaphore = semaphoreMap.remove(definitionId);
        if (oldSemaphore != null) {
            log.info("[HOT_UPDATE_CLEANUP] 已移除旧的 Semaphore。definitionId={}", definitionId);
        }
    }

    /**
     * 移除指定任务定义的并发控制器。
     * 由于使用非阻塞的tryAcquire，不需要中断等待线程。
     *
     * @param definitionId 任务定义的唯一ID
     */
    public void remove(Long definitionId) {
        Semaphore semaphore = semaphoreMap.remove(definitionId);
        if (semaphore != null) {
            log.info("[SEMAPHORE_REMOVE] 成功移除 Definition ID: {} 的并发控制器。", definitionId);
        } else {
            log.info("[SEMAPHORE_REMOVE] Definition ID: {} 对应的并发控制器不存在，无需移除。", definitionId);
        }
    }
}