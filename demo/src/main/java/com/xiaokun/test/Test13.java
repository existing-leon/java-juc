package com.xiaokun.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/3 20:28
 */
@Slf4j(topic = "c.Test13")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3500);
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {

    private Thread monitor;

    // 启动监控线程
    public void start() {
        monitor = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Thread currentThread = Thread.currentThread();
                    if (currentThread.isInterrupted()) {
                        log.debug("料理后事中...");
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        log.debug("执行监控记录");
                    } catch (InterruptedException e) {  // Interrupt会将打断标记置为假, 无法进入上面的循环
                        e.printStackTrace();
                        // 重新设置打断标记, 这样才能在下次循环的时候跳出循环
                        currentThread.interrupt();
                    }
                }
            }
        });

        monitor.start();
    }

    // 停止监控线程
    public void stop() {
        monitor.interrupt();
    }

}
