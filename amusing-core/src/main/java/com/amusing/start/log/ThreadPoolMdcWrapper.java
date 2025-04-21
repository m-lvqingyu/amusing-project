package com.amusing.start.log;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2025/2/28
 */
public class ThreadPoolMdcWrapper extends ThreadPoolTaskExecutor {

    public ThreadPoolMdcWrapper() {

    }

    @Override
    public void execute(@Nonnull Runnable task) {
        super.execute(MDCUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(@Nonnull Runnable task, long startTimeout) {
        super.execute(MDCUtils.wrap(task, MDC.getCopyOfContextMap()), startTimeout);
    }

    @Override
    @Nonnull
    public <T> Future<T> submit(@Nonnull Callable<T> task) {
        return super.submit(MDCUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    @Nonnull
    public Future<?> submit(@Nonnull Runnable task) {
        return super.submit(MDCUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    @Nonnull
    public ListenableFuture<?> submitListenable(@Nonnull Runnable task) {
        return super.submitListenable(MDCUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(@Nonnull Callable<T> task) {
        return super.submitListenable(MDCUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

}
