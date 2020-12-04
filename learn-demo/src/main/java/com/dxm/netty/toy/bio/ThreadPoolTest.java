package com.dxm.netty.toy.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 4, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

        for (int i = 0; i <100 ; i++) {
            pool.execute(new Task(i));
        }
        pool.shutdown();

    }
    static class Task implements Runnable{
        private Integer index;
        public Task(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                System.out.println("任务开始执行"+this.index);
                Thread.sleep(6);
                //System.out.println("任务执行结束"+this.index);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
