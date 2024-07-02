package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/2 17:39
 *
 * start方法和run方法的调用
 */
@Slf4j(topic = "c.Test4")
public class Test4 {
    public static void main(String[] args) {

        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("running...");
            }
        };

        // 调用run()方法还是主线程来运行, 并不能解决线程阻塞问题
        t1.run();

        // 调用start()方法才可以解决线程阻塞问题
        t1.start();

        log.debug("do other things...");

    }
}
