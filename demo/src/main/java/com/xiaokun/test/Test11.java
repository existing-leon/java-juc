package com.xiaokun.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/3 9:15
 */
@Slf4j(topic = "c.Test11")
public class Test11 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("sleep...");
                sleep(5000);
            }
        }, "t1");

        t1.start();
        sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
        log.debug("打断状态：{}", t1.isInterrupted());

    }
}
