package com.example.springbootdemo.multi_threading.reentrantlockex4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class InOutManagement {
    private final Queue<String> items;
    private final int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public InOutManagement(int capacity) {
        this.capacity = capacity;
        this.items = new LinkedList<>();
    }

    public void produceItems(String item) {
        lock.lock();
        try {
            while (items.size() == capacity) {
                System.out.println("11111");
                notFull.await();
            }
            items.add(item);
            System.out.println("produced item: " + item + " in thread: " + Thread.currentThread().getName());
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void consumeItems() {
        lock.lock();
        try {
            while (items.isEmpty()) {
                System.out.println("22222");
                notEmpty.await();
            }
            String item = items.poll();
            System.out.println("consumed item: " + item + " in thread: " + Thread.currentThread().getName());
            notFull.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
