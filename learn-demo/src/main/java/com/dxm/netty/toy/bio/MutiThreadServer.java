package com.dxm.netty.toy.bio;

import com.dxm.netty.toy.util.CommonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService executorService = Executors.newFixedThreadPool(3);
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
