package com.dxm.netty.toy.bio;

import com.dxm.netty.toy.util.CommonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程socket服务器
 */
public class MutiThreadServer {


    public static void main(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("启动服务器,服务开始......");
            try {
                doRequest(serverSocket);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("服务器中断客户端的链接.....");
            }
        } catch (IOException e) {
            System.out.println("服务器发生异常,服务中断中断......");
            e.printStackTrace();
        }
        System.out.println("关闭服务器,服务停止......");


    }

    private static void doRequest(ServerSocket serverSocket) throws IOException {
         int i=1;
        /**
         * int corePoolSize,                 核心队列
         * int maximumPoolSize,              最大队列
         * long keepAliveTime,               保活时间  when the number of threads is greater than the core, this is the maximum time that excess idle threads  will wait for new tasks before terminating.
         * TimeUnit unit,                    时间单位  the time unit for the {@code keepAliveTime} argument
         * BlockingQueue<Runnable> workQueue 阻塞队列   不管并发有多高,任意时刻,永远只有一个任务可以入队或者出队,也就意味着他是一个线程安全的队列
         *                                             有界队列/无界队列
         *  拒绝策略 :  (DiscardPolicy  DiscardOldestPolicy  CallerRunsPolicy  AbortPolicy)
         *
         *  线程池运行原理:
         *  初始化 0个线程
         *  任务进来 先创建核心线程处理任务
         *  核心线程满载,还有任务进来,把任务放在阻塞队列中
         *  阻塞队列放满了,还有任务进来,创建非核心线程处理任务
         *  入过非核心线程满载,还有任务进来,启用拒绝策略
         *
         *
         */
        ArrayBlockingQueue waitQueue  = new ArrayBlockingQueue(5);
        ExecutorService executorService = new ThreadPoolExecutor(10,15,60, TimeUnit.SECONDS,waitQueue);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread task = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                String address = socket.getInetAddress().getHostAddress();
                System.out.println(String.format("服务器线程%s,正在处理客户端:%s的请求.", threadName, address));
                try (
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ) {

                    while (true) {
                        String read = in.readLine();
                        System.out.println(String.format("从客户端[%s]获取到的数据:%s,时间为:%s", address, read, CommonUtil.getNowTime()));

                        if (read.equals("quit")) {
                            System.out.println(String.format("客户端:%s退出,时间为:%s", address, CommonUtil.getNowTime()));
                            break;
                        }

                        String msg = read + ",服务器处理线程:" + Thread.currentThread().getName();
                        out.println(msg);

                        System.out.println(
                                String.format("发送给客户端[%s]的数据:%s,时间为:%s", address, msg, CommonUtil.getNowTime())
                        );

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "工作线程" + i++);

            executorService.submit(task);

        }

    }
}
