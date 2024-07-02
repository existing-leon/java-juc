package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/2 19:43
 */
@Slf4j(topic = "c.Test5")
public class Test5 {
    public static void main(String[] args) {

        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        System.out.println(t1.getState());
        t1.start();
//        t1.start();
        System.out.println(t1.getState());
    }
}
