package com.xiaokun.n3;

/**
 * @author huxk
 * @create 2024/7/1 15:32
 *
 * 断点模式记得使用右键切换为 Thread, 这样才可以观察到多个线程同时运行的情况
 * 主要是为了说明每个线程的栈帧是独立的
 */
public class TestFrames {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                method1(20);
            }
        });
        t1.setName("t1");
        t1.start();

        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
