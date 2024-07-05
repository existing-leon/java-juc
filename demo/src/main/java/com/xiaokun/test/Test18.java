package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/4 21:32
 */
@Slf4j(topic = "c.Test18")
public class Test18 {

    static int counter = 0;
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock1) {
                    counter++;
                    log.debug("t1 ==> {}", counter);
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock2) {
                    counter--;
                    log.debug("t2 ==> {}", counter);
                }
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}", counter);
    }
}
