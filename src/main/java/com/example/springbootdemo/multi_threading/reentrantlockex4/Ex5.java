package com.example.springbootdemo.multi_threading.reentrantlockex4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ex5 {
    private int count;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Ex5() {
        this.count = 0;
    }

    public void increase() {
        lock.lock();
        count++;
        if (count == 10) throw new RuntimeException();
        lock.unlock();
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        Ex5 ex5 = new Ex5();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    ex5.increase();
                } catch (Exception ignore) {
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    ex5.increase();
                } catch (Exception ignore) {
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(ex5.getCount());
    }
}
