package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/2 22:01
 */
@Slf4j(topic = "c.Test10")
public class Test10 {
    static int r = 0;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    private static void test1() throws InterruptedException {
        log.debug("test1方法开始执行");
        Thread t1 = new Thread(() -> {
            log.debug("run方法开始执行");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("run方法执行结束");
            r = 10;
        });

        t1.start();
        t1.join();
        log.debug("结果为：{}", r);
        log.debug("test1方法执行结束");
    }
}
