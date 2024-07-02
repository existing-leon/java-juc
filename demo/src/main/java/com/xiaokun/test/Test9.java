package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/2 21:11
 */
@Slf4j(topic = "c.Test9")
public class Test9 {
    public static void main(String[] args) {
        Runnable task1 = () -> {
            int count = 0;
            for (; ; ) {
                System.out.println("---->1 " + count++);
            }
        };

        Runnable task2 = () -> {
            int count = 0;
            for (; ; ) {
                // 让出时间片, 会产生1比2增长速度快很多的结果
//                Thread.yield();
                System.out.println("     ---->2 " + count++);
            }
        };

        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");

        // 优先级设置, 理论上2的增长速度应该比1要快很多, 但是在多核情况下, 效果并不明显
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();


    }

}
