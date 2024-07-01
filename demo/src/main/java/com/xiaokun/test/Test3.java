package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/1 11:44
 * <p>
 * 方法三： FutureTask配合Thread
 */
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建任务对象
        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.info("running task");
            sleep(2000);
            return 100;
        });

        Thread t = new Thread(task, "t1");

        t.start();

        // 获取task线程的返回值：主线程阻塞等待子线程返回值
        log.debug("{}", task.get());

        log.debug("running main");

    }
}
