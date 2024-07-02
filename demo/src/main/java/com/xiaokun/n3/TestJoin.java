package com.xiaokun.n3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/2 23:41
 */
@Slf4j(topic = "c.TestJoin")
public class TestJoin {

    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test2();
    }

    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                sleep(1000);
                r1 = 10;
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                sleep(2000);
                r2 = 20;
            }
        });


        t1.start();
        t2.start();
        long start = System.currentTimeMillis();
        log.debug("join begin...");
        t1.join();
        log.debug("t1 join end...");
        t2.join();
        log.debug("t2 join end...");
        long end = System.currentTimeMillis();
        log.debug("r1: {}, r2: {}, cost: {}", r1, r2, end - start);
    }
}
