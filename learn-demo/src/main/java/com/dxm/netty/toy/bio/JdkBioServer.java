package com.dxm.netty.toy.bio;

import com.dxm.netty.toy.util.CommonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class JdkBioServer {


    public static void main(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(22222)) {
            System.out.println("启动服务器,服务开始......");

            try {
                doRequest(serverSocket);
            } catch (SocketException e) {
                System.out.println("服务器中断客户端的链接.....");
            }

        } catch (IOException e) {
            System.out.println("服务器发生异常,服务中断中断......");
            e.printStackTrace();
        }
        System.out.println("关闭服务器,服务停止......");


    }

    private static void doRequest(ServerSocket serverSocket) throws IOException {


        while (true) {
            Socket socket = serverSocket.accept();
            try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {

                while (true) {
                    String read = in.readLine();
                    System.out.println(String.format("从客户端获取到的数据:%s,时间为:%s", read, CommonUtil.getNowTime()));

                    if (read.equals("quit")) break;

                    String msg = read + ",\n服务器处理线程:" + Thread.currentThread().getName();
                    out.println(msg + "\n");
                    out.flush();
                    System.out.println(
                            String.format("发送给客户端的数据:%s,时间为:%s", msg, CommonUtil.getNowTime())
                    );

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
