package com.xiaokun.n3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * @author huxk
 * @create 2024/7/8 22:17
 */
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
//    public static void main(String[] args) {
//        Number n1 = new Number();
//
//        new Thread(() -> {
//            log.debug("begin");
//            n1.a();
//        }).start();
//
//        new Thread(() -> {
//            log.debug("begin");
//            n1.b();
//        }).start();
//
//        new Thread(() -> {
//            log.debug("begin");
//            n1.c();
//        }).start();
//    }

//    public static void main(String[] args) {
//        Number n1 = new Number();
//        Number n2 = new Number();
//        new Thread(()-> {n1.a();}).start();
//        new Thread(()-> {n2.b();}).start();
//    }

    public static void main(String[] args) {
        Number n = new Number();
        new Thread(()-> {n.a();}).start();
        new Thread(()-> {n.b();}).start();
    }
}

@Slf4j(topic = "c.Number")
class Number {
    @SneakyThrows
    public static synchronized void a() {
        sleep(1000);
        log.debug("1");
    }

    public static synchronized void b() {
        log.debug("2");
    }

    public void c() {
        log.debug("3");
    }
}