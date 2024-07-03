package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/3 23:23
 * <p>
 * 打断打断状态
 */
@Slf4j(topic = "c.Test14")
public class Test14 {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug("park...");
                LockSupport.park();
                log.debug("unpark...");
//                log.debug("打断状态：{}", Thread.currentThread().isInterrupted());

                // 只能打断一次, 再次打断就不生效了想要再次生效, 修改上一行代码如下（会重置打断标记为假）：
                log.debug("打断状态：{}", Thread.interrupted());
                LockSupport.park();
                log.debug("unpark...");
            }
        }, "t1");

        t1.start();

        // 注释掉下面两行会卡住
        sleep(1000);
        t1.interrupt();

    }
}
