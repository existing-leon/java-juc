package com.xiaokun.n3;

import com.xiaokun.util.FileReader;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huxk
 * @create 2024/7/4 8:27
 */
@Slf4j(topic = "c.TestState2")
public class TestState2 {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileReader.read("E:\\windows\\cn_windows_10_multiple_editions_version_1607_updated_jan_2017_x64_dvd_9714394.iso");
            }
        }, "t1").start();

        System.out.println("ok");
    }
}
