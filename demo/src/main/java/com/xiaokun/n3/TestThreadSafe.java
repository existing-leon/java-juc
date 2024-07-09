package com.xiaokun.n3;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huxk
 * @create 2024/7/9 22:11
 */
@Slf4j(topic = "c.TestThreadSafe")
public class TestThreadSafe {

    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadUnsafe test = new ThreadUnsafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                test.method1(LOOP_NUMBER);
            }, "Thread" + (i + 1)).start();
        }
    }
}

class ThreadUnsafe {
    ArrayList<String> list = new ArrayList<String>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            // { 临界区, 会产生竞态条件
            method2();
            method3();
            // } 临界区
        }
    }


    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}


