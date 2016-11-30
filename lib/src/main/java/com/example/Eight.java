package com.example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 作者:马骥，杨松
 * 将字符串格式的整数转为十进制数字
 */

public class Eight {

    public static void main(String[] args) {
        String cmd="bb8d206b";
        byte[] bytes=cmd.getBytes();
        System.out.println(bytes.length);
    }


    private static void scheduledThreadPool(){
        ScheduledExecutorService executorService=Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread : "+Thread.currentThread().getName()+",定时计算");
            }
        },1,2, TimeUnit.SECONDS);
    }
}
