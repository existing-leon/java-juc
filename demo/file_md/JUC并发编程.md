# JUC并发编程

## 1 预备知识

```pom
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
    </dependencies>
```

**日志配置文件**（可以不用）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration
        xmlns="http://ch.qos.logback/xml/ns/logback"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://ch.qos.logback/xml/ns/logback logback.xsd">


    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                %date{HH:mm:ss} [%t] %logger - %m%n
            </pattern>
        </encoder>
    </appender>

    <logger name="c" level="debug" additivity="false">
        <appender-ref ref="STDOUT"/>
    </logger>

    <root level="ERROR">
        <appender-ref ref="STDOUT"/>
    </root>

</configuration>

```



## 2 进程与线程

### 2.1 进程与线程

#### 进程

* 程序是由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理IO的
* 当一个程序被运行，从磁盘加载这个程序的代码至内存，这时就开启了一个线程。
* 进程就可以视为程序的一个实例。大部分程序可以同时运行多个实例进程（例如记事本、画图、浏览器等），也有的程序只能启动一个实例进程（例如网易云音乐、360安全卫士等）

#### 线程

* 一个进程之内可以分为一到多个线程
* 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给CPU执行
* Java中，线程作为最小调度单位，进程作为资源分配的最小单位。在windows中进程是不活动的，只是作为线程的容器。

#### 二者对比

* 进程基本上相互独立的，而线程存在于进程内，是进程的一个子集
* 进程拥有共享的资源，如内存空间等，供其内部的线程共享
* 进程间通信较为复杂
  * 同一台计算机的进程通信称为IPC（Inter-process communication）
  * 不同计算机之间的进程通信，需要通过网络，并遵守共同的协议，例如HTTP
* 线程通信相对简单，因为他们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量
* 线程更轻量，线程上下文切换成本一般上要比进程上下文切换低

### 2.2 并行和并发

#### 并发

单核cpu下，线程实际还是**串行执行**的。操作系统中有一个组件叫做任务调度器，将cpu的时间片（windows下时间片最小约为15毫秒）分给不同的线程使用，只是由于cpu在线程间（时间片很短）的切换非常快，人类感觉是**同时运行的**。总结一句话就是：**微观串行，宏观并行**，一般会将这种**线程轮流使用**CPU的做法称为并发，concurrent

| CPU  | 时间片1 | 时间片2 | 时间片3 | 时间片4 |
| ---- | ------- | ------- | ------- | ------- |
| core | 线程1   | 线程2   | 线程3   | 线程4   |

#### 并行

多核cpu下，每个核（core）都可以调度运行线程，这时候线程可以是并行

| CPU   | 时间片1 | 时间片2 | 时间片3 | 时间片4 |
| ----- | ------- | ------- | ------- | ------- |
| core1 | 线程1   | 线程1   | 线程3   | 线程3   |
| core2 | 线程2   | 线程4   | 线程2   | 线程4   |

引用 Rob Pike的一段描述：

* 并发（concurrent）是同一时间应对（dealing with）多件事情的能力
* 并行（parallel）是同一时间动手做（doing）多件事情的能力



例子

* 家庭主妇做饭、打扫卫生、给孩子喂奶，她一个人轮流交替做这多件事，这时就是并发
* 家庭主妇雇了个保姆，他们一起做这些事，这时既有并发，也有并行（这时会产生竞争，例如锅只有一口，一个人用锅时，另一个人就得等待）
* 雇了三个保姆，一个专门做饭，一个专门打扫卫生，一个专门喂奶，互不干扰，这时是并行

### 2.3 应用

#### 应用之异步调用（案例1）

从方法调用得角度来讲，如果

* 需要等带结果返回，才能继续运行就是同步
* 不需要等待结果返回，就能继续运行就是异步

注意：同步在多线程中还有另外一层意思，是让多个线程步调一致

**1）设计**

多线程可以让方法执行变为异步的（即不要干巴巴等着）比如说读取磁盘文件时，假设读取操作花费了5秒钟，如果没有线程调度机制，这5秒调用者什么都做不了，其他代码都得暂停...

**2）结论**

* 比如在项目中，视频文件需要转换格式等操作比较费时，这时开一个新线程处理视频转换，避免阻塞主线程
* tomcat得异步 servlet 也是类似的目的，让用户线程处理耗时较长的操作，避免阻塞tomcat的工作线程
* ui程序中，开线程进行其他操作，避免阻塞ui线程

#### 应用之提高效率（案例1）

充分利用多核cpu的优势，提高运行效率。想象下面的场景，执行3个计算，最后将计算结果汇总。

```md
计算1花费10ms
计算2花费11ms
计算3花费9ms
汇总需要1ms
```

* 如果是串行执行，那么总共花费的时间是10+11+9+1=31ms

* 但如果是四核cpu，各个核心分别使用线程1执行计算1，线程2执行计算2，线程3执行计算3，那么3个线程是并线的，花费时间只取决于最长的那个线程运行的时间，即11ms，最后加上汇总时间只会花费12ms

  注意：

  需要在多核cpu才能提高效率，单核仍然是轮流执行

**1）设计**

**2）结论**

1. 单核cpu下，多线程不能实际提高运行效率，只是为了能够在不同的任务之间切换，不同线程轮流使用cpu，不至于一个线程总占用cpu，别的线程没法干活
2. 多核cpu可以并行跑多个线程，但能否提高程序运行效率还是要分情况的
   * 有些任务，经过精心设计，将任务拆分，并行执行，当然可以提高程序的运行效率。但不是所有计算任务都能拆分（参考【阿姆达尔定律】）
   * 也不是所有任务都需要拆分，任务的目的如果不同，谈拆分和效率没啥意义
3. IO操作不占用cpu，只是我们一般拷贝文件使用的是【阻塞IO】，这时相当于线程虽然不用cpu，但需要一直等待IO结束，没能充分利用线程，所以才有后面的【非阻塞IO】和【异步IO】优化



## 3 Java线程

### 3.1 创建和运行线程

#### 方法一：直接使用Thread

```java
    public static void main(String[] args) {

        Thread t = new Thread() {
            @Override
            public void run() {
                log.debug("running");
            }
        };
        t.setName("t1");
        t.start();

        log.debug("running");
    }
```

#### 方法二：使用Runnable配合Thread

```java
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
```

* #### 原理之Thread与Runnable的关系

  ```java
   * 原理之Thread与Runnable的关系：
   * 分析Thread的源码，理清它与Runnable的关系
   * 小结：
   *  * 方法1是把线程和任务合并在了一起，方法2是把线程和任务分开了
   *  * 用Runnable更容易与线程池等高级API配合
   *  * 用Runnable让任务脱离了Thread继承体系，更灵活
  ```

#### 方法三：FutureTask配合Thread

```java
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建任务对象
        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.info("running task");
            sleep(2000);
            return 100;
        });

        Thread t = new Thread(task, "t1");

        t.start();

        // 获取task线程的返回值：主线程阻塞等待子线程返回值
        log.debug("{}", task.get());

        log.debug("running main");

    }
```



### 3.2 观察多个线程同时运行

```java
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    log.debug("running");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        },"t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    log.debug("running");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "t2").start();
    }
```



### 3.3 查看进程线程的方法

#### **Java查看进程线程的方法：**

- jps命令查看所有Java进程

- jstack <PID> 查看某个Java进程（PID）的所有线程状态

- jconsole来查看某个Java进程中线程的运行情况（图形界面）

  jconsole远程监控配置（JDK自带工具，只需要win + R 输入 jconsole）

  - 需要以如下方式运行你的java类

    ```java
    java -Djava.rmi.server.hostname=`ip地址` -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=`连接端口` -Dcom.sun.management.jmxremote.ssl=是否安全连接 -Dcom.sun.management.jmxremote.authenticate=是否认证 java类
    ```

  - 修改 /etc/hosts 文件将 127.0.0.1映射至主机名

  如果要认证访问，还需要做如下步骤

  - 复制 jmxremote.password 文件
  - 修改 jmxremote.password 和 jmxremote.access 文件的权限为 600 即文件所有者可读写
  - 连接时填入 controlRole （用户名），R&D （密码）

### 3.4 原理之线程运行

#### **栈与栈帧：**

Java Virtual Machine Stacks（虚拟机栈）

我们都知道 JVM 中由堆，栈，方法区所组成，其中栈内存是给谁用的呢？其实就是线程。每个线程启动后，虚拟机就会为其分配一块栈内存

- 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
- 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

#### 线程上下文切换（Thread Context Switch）：

因为一下一些原因导致cpu不再执行当前的线程，转而执行另一个线程的代码

- 线程的cpu时间片用完
- 垃圾回收
- 有更高优先级的线程需要运行
- 线程自己调用了 sleep、yield、wait、join、park、synchronized、lock等方法

当Context Switch发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java中对应的概念就是程序计数器（Program Counter Register），它的作用是记住下一条jvm指令的执行地址，是线程私有的

- 状态包括程序技术器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
- Context Switch频繁发生会影响性能



### 3.5 常见方法



### 3.6 start方法详解

```java
    public static void main(String[] args) {

        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("running...");
            }
        };

        // 调用run()方法还是主线程来运行, 并不能解决线程阻塞问题
        t1.run();

        // 调用start()方法才可以解决线程阻塞问题
        t1.start();

        log.debug("do other things...");

    }
```

```java
    public static void main(String[] args) {

        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        System.out.println(t1.getState());
        t1.start();
//        t1.start();
        System.out.println(t1.getState());
    }
```



### 3.7 Sleep 与 yield

#### sleep

1. 调用sleep会让当前线程从Running进入Timed Waiting状态
2. 其它线程可以使用interrupt方法打断正在睡眠的线程，这时sleep方法会抛出 InterruptedException
3. 睡眠结束后的线程未必会立刻得到执行
4. 建议用TimeUnit的sleep代替Thread的sleep来获得更好的可读性

#### yield

1. 调用yield会让当前线程从Running进入Runnable状态，然后调度执行其他同优先级的线程。如果这时没有同优先级的线程，那么不能保证让当前线程暂停的效果
2. 具体的实现依赖于操作系统的任务调度器



#### 线程优先级

- 线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
- 如果cpu 比较忙，那么线程优先级高的线程会获得更多的时间片，但cpu 闲时，优先级几乎没作用



#### 案例-防止CPU占用100%

**sleep实现**

在没有利用cpu来计算时，不要让while(true)空转浪费cpu，这时可以使用yield或sleep来让出cpu的使用权给其他程序

```java
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
```

- 可以用wait或条件变量达到类似的效果
- 不同的是，后两种都需要加锁，并且需要相应的唤醒操作，一般适用于要进行同步的场景
- sleep使用与无需锁同步的场景



### 3.8 Join方法详解

#### 为什么需要join

下面的代码执行，打印r是什么？

```java
    static int r = 0;

    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        log.debug("test1方法开始执行");
        Thread t1 = new Thread(() -> {
            log.debug("run方法开始执行");
            try {
                sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("run方法执行结束");
            r = 10;
        });

        t1.start();
        log.debug("结果为：{}", r);
        log.debug("test1方法执行结束");
    }
```

分析：

- 因为主线程和线程 t1 是并行执行的，t1线程需要1毫秒之后才能算出 r = 10
- 而主线程一开始就要打印 r 的结果，所以只能打印出 r = 0

解决方法：

- 用sleep行不行？为什么？等待的时间不好确定
- 用join，加在 t1.start() 之后即可

```java
    static int r = 0;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    private static void test1() throws InterruptedException {
        log.debug("test1方法开始执行");
        Thread t1 = new Thread(() -> {
            log.debug("run方法开始执行");
            try {
                sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("run方法执行结束");
            r = 10;
        });

        t1.start();
        t1.join();
        log.debug("结果为：{}", r);
        log.debug("test1方法执行结束");
    }
```

**等待多个结果的时候**

程序等待时间为单个子任务执行最长时间



### 3.9 interrupt 方法详解

#### 打断sleep，wait，join的线程

打断 sleep 的线程，会清空打断状态，以 sleep为例

```java
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("sleep...");
                sleep(5000);
            }
        }, "t1");

        t1.start();
        sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
        log.debug("打断状态：{}", t1.isInterrupted());

    }
```



#### 两阶段终止模式

Two Phase Termination

在一个线程T1中如何 “优雅” 终止线程 T2？这里的【优雅】指的是给T2一个料理后事的机会

1、**错误思路**

- 使用线程对象的stop()方法停止线程
  - stop方法会真正杀死线程，如果这时线程锁住了共享资源，那么当它被杀死后就再也没有机会释放锁，其它线程将永远无法获取锁
- 使用System.exit(int)方法停止线程
  - 目的仅是停止一个线程，但这种做法会让整个程序都停止



#### 打断park线程

打断park线程，不会清空打断状态

```java
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug("park...");
                LockSupport.park();
                log.debug("unpark...");
                log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
            }
        }, "t1");

        t1.start();

        sleep(1000);
        t1.interrupt();
        
    }
```



### 3.10 不推荐的方法

还有一些不推荐的方法，这些方法已经过时，容易破坏同步代码块，造成线程死锁

| 方法名    | static | 功能说明             |
| --------- | ------ | -------------------- |
| stop()    |        | 停止线程运行         |
| suspend() |        | 挂起（暂停）线程运行 |
| resume()  |        | 恢复线程运行         |



### 3.11 主线程和守护线程

默认情况下，Java进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其他非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。

```java
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
                log.debug("结束...");
            }
        }, "t1");

        // 将t1线程设为守护线程, 这样就可以在主线程结束之后停止Java进程
        t1.setDaemon(true);
        t1.start();

        Thread.sleep(1000);
        log.debug("结束...");
    }
```

**注意**

* 垃圾回收器线程就是一种守护线程
* Tomcat中的Acceptor和Poller线程都是守护线程，所以Tomcat接收到shutdown命令后，不会等待他们处理完当前请求



### 3.12 五种状态

这是从操作系统层面来描述的

* 【初始状态】仅是在语言层面创建了线程对象，还未与操作系统线程关联
* 【可运行状态】指该线程已经被创建（与操作系统线程关联），可以由CPU调度执行
* 【运行状态】指获取了CPU时间片运行中的状态
  * 当CPU时间片用完，会从【运行状态】转换至【可运行状态】，会导致线程的上下文切换
* 【阻塞状态】
  * 如果调用了阻塞API，如BIO读写文件，这时该线程实际不会用到CPU，会导致线程上下文切换，进入【阻塞状态】
  * 等BIO操作完毕，会由此操作系统唤醒阻塞的线程，转换至【可运行状态】
  * 与【可运行状态】的区别是，对【阻塞状态】的线程来说，只要它们一直不唤醒，调度器就一直不会考虑调度他们
* 【终止状态】表示线程已经执行完毕，生命周期已经结束，不会再转换为其他状态



### 3.13 六种状态

这是从Java API层面来描述的

根据Thread.State枚举，分为六种状态

- 【NEW】线程刚被创建，但是还没有调用start()方法
- 【RUNNABLE】当调用了start()方法之后，注意，**Java API** 层面的RUNNABLE状态涵盖了 **操作系统** 层面的【可运行状态】、【运行状态】和【阻塞状态】（由于BIO导致的线程阻塞，在Java里无法区分，仍然认为是可运行）
- BLOCKED，WAITTING，TIMED_WAITTING 都是 Java API层面对【阻塞状态】的细分
- TERMINATED 当线程代码运行结束

**代码演示：**

```java
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug("running...");    // 因为没调用, 所以是new状态
            }
        });

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) {      // runnable

                }
            }
        };
        t2.start();

        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                log.debug("running...");    // 正常结束, 是terminated状态
            }
        };
        t3.start();

        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (TestState.class) {
                    try {
                        Thread.sleep(1000000);      // timed_waiting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join();      // t2是循环, 导致t5一直等待, 所以是 waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                synchronized (TestState.class) {
                    try {
                        Thread.sleep(1000000);      // 因为被t4抢到了锁, 所以t6一直是blocked状态
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t6.start();


        log.debug("t1 state {}", t1.getState());
        log.debug("t2 state {}", t2.getState());
        log.debug("t3 state {}", t3.getState());
        log.debug("t4 state {}", t4.getState());
        log.debug("t5 state {}", t5.getState());
        log.debug("t6 state {}", t6.getState());
    }
```

**运行结果如下：**

```java
11:22:32.969 [main] DEBUG c.TestState - t1 state NEW
11:22:32.969 [t3] DEBUG c.TestState - running...
11:22:32.972 [main] DEBUG c.TestState - t2 state RUNNABLE
11:22:32.973 [main] DEBUG c.TestState - t3 state TERMINATED
11:22:32.973 [main] DEBUG c.TestState - t4 state TIMED_WAITING
11:22:32.973 [main] DEBUG c.TestState - t5 state WAITING
11:22:32.973 [main] DEBUG c.TestState - t6 state BLOCKED
```



### 3.14 习题

阅读华罗庚《统筹方法》，给出烧水泡茶的多线程解决方案，提示

* 参考图二，用两个线程（两个人协作）模拟烧水泡茶过程
  * 文中方法乙、丙都相当于任务串行
  * 而图一相当于启动了4个线程，有点浪费
* 用sleep(n) 模拟洗茶壶、洗水壶等耗费的时间

附：华罗庚《统筹方法》

​	统筹方法，是一种安排工作进程的数学方法。它的实用范围极广泛，在企业管理和基本建设中，以及关系复杂的科研项目的组织与管理中，都可以应用。

​	怎样应用呢？主要是把工序安排好。

​	比如，想泡壶茶喝。当时的情况是：开水没有；水壶要洗，茶壶、茶杯要洗；火已生了，茶叶也有了。怎么办？

* 方法甲：洗好水壶，灌上凉水，放在火上；在等待水开的时间里，洗茶壶、洗茶杯、拿茶叶；等水开了，泡茶喝。
* 方法乙：先做好一些准备工作，洗水壶，洗茶壶茶杯，拿茶叶；一切就绪，灌水烧水；坐待水开了，泡茶喝。
* 方法丙：洗净水壶，灌上凉水，放在火上，坐待水开；水开了之后，急急忙忙找茶叶，洗茶壶茶杯，泡茶喝。

哪一种方法省时间？我们能一眼看出，第一种办法好，后两种办法都窝了工。

这时小事，但这时引子，可以引出生产管理等方面有用的方法来。









