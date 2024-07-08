package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/8 22:03
 */
@Slf4j(topic = "c.Test19")
public class Test19 {
    public static void main(String[] args) {

    }
}

class Test01 {
    public synchronized void test() {

    }
}

// 等价于
class Test02 {
    public void test() {
        synchronized (this) {

        }
    }
}

class Test03 {
    public synchronized static void test() {

    }
}
// 等价于
class Test04{
    public static void test() {
        synchronized (Test04.class) {

        }
    }
}