package com.example.springbootdemo.multi_threading.ex3;

import com.example.springbootdemo.entity.User;

import java.util.concurrent.*;

public class MyThead implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("name: " + Thread.currentThread().getName());
    }

    static class Call implements Callable<User> {
        @Override
        public User call() throws Exception {
            System.out.println("in call: " + Thread.currentThread().getName());
            Thread.sleep(2000);
            return User.builder().id(1).email("aaa@gmail.com").address("TB").build();
        }
    }

    static class Delay implements Delayed{
        @Override
        public long getDelay(TimeUnit unit) {
            return 1000;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Runtime.getRuntime().availableProcessors() + " --> " + Thread.currentThread().getName());
        ExecutorService executor = Executors.newFixedThreadPool(10);
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(5);
//            executor.submit(new MyThead());
//            Thread thread1 = new Thread(new MyThead());
//            thread1.start();
//            ScheduledFuture<User> schedule = scheduledService.schedule(new Call(), 1000, TimeUnit.MILLISECONDS);
//            System.out.println(schedule.get());
//            scheduledService.scheduleAtFixedRate(new MyThead(), 0, 500, TimeUnit.MILLISECONDS);
//            scheduledService.scheduleWithFixedDelay(new MyThead(), 0, 500, TimeUnit.MILLISECONDS);
    }
}
