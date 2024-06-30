package com.xiaokun.nc;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class Async {
    public static void main(String[] args) {
        new Thread(()->{
            try {
                sleep(10000);
                log.info("这是需要长时间执行的代码...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        log.info("这是接下来要执行的代码...");
    }
}
