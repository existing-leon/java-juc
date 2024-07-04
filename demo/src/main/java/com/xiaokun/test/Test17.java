package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/4 20:22
 */
@Slf4j(topic = "c.Test17")
public class Test17 {
    static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter++;
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter--;
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}", counter);
    }
}
