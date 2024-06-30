package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

// 使用Runnable配合Thread
@Slf4j(topic = "c.Test2")
public class Test2 {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("running");
            }
        };

        // 创建线程对象
        Thread t = new Thread(runnable,"t2");

        // 启动线程
        t.start();
    }
}
