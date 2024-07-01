package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;


/**
 * @author huxk
 * @create 2024/7/1 11:56
 *
 * 演示多个线程并发交替执行
 */
@Slf4j(topic = "c.TestMultiThread")
public class TestMultiThread {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    log.debug("running");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        },"t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    log.debug("running");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "t2").start();
    }
}
