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
//        test2();
        test3();
    }

    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                sleep(2000);
                r1 = 10;
            }
        });

        long start = System.currentTimeMillis();
        t1.start();

        // 线程执行结束会导致join结束
        log.debug("join begin...");
        // 如果线程实际运行时间大于等待时间, 那么只会等待填入的等待时间
        t1.join(1500);
        // 如果线程实际运行时间小于等待时间, 那么只会等待线程实际运行的时间
        // t1.join(3000);
        long end = System.currentTimeMillis();
        log.debug("r1: {}, r2: {}, cost: {}", r1, r2, end - start);
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
