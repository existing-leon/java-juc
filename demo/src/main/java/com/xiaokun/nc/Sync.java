package com.xiaokun.nc;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class Sync {
    public static void main(String[] args) throws InterruptedException {
        sleep(10000);
        log.info("这是需要长时间执行的代码...");
        log.info("这是接下来要执行的代码...");
    }
}
