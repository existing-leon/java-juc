package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/4 7:42
 */
@Slf4j(topic = "c.Test15")
public class Test15 {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
                log.debug("结束...");
            }
        }, "t1");

        // 将t1线程设为守护线程, 这样就可以在主线程结束之后停止Java进程
        t1.setDaemon(true);
        t1.start();

        Thread.sleep(1000);
        log.debug("结束...");
    }
}
