package com.dxm.netty.toy.bio;

import com.dxm.netty.toy.util.CommonUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JdkBioClient {
    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);
        try(Socket socket = new Socket("localhost",5500)){
            System.out.println("启动客户端,通信开始......");




                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String line2 = "hello\n";
                if (line2.equals("quit")) {
                    //break;
                }
                out.println(line2);
                System.out.println(String.format("发送给服务器数据:%s,时间为:%s", line2, CommonUtil.getNowTime()));




                String data = in.readLine();
                //out.write(String.format("ECHO----****----:%s,当前时间:%s",data.toString(), CommonUtil.getNowTime()));
                System.out.println(String.format("获得服务的的回复:%s,时间为:%s",data.toString(),CommonUtil.getNowTime()));





        } catch (IOException e) {
            System.out.println("客户端发生异常,通信中断......");
            e.printStackTrace();
        }
        System.out.println("关闭客户端,通信结束......");
    }
}
