package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/3 9:28
 */
@Slf4j(topic = "c.Test12")
public class Test12 {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // 获取打断标记
                    if (interrupted() == true) {
                        log.debug("被打断了, 退出循环...");
                        break;
                    }
                }
            }
        }, "t1");

        t1.start();

        sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
    }
}
