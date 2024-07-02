package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author huxk
 * @create 2024/7/2 20:51
 */
@Slf4j(topic = "c.Test8")
public class Test8 {
    public static void main(String[] args) throws InterruptedException {

        log.debug("enter sleep...");
        // 睡眠 1s
        TimeUnit.SECONDS.sleep(1);
        log.debug("end sleep...");

        log.debug("enter sleep...");
        // 睡眠 1s
        Thread.sleep(1000);
        log.debug("end sleep...");

        // 上面两种效果一样

    }
}
