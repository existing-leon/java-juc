package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

// 方法二：使用Runnable配合Thread
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

/**
 * 原理之Thread与Runnable的关系：
 * 分析Thread的源码，理清它与Runnable的关系
 * 小结：
 *  * 方法1是把线程和任务合并在了一起，方法2是把线程和任务分开了
 *  * 用Runnable更容易与线程池等高级API配合
 *  * 用Runnable让任务脱离了Thread继承体系，更灵活
 *
 *
 */

