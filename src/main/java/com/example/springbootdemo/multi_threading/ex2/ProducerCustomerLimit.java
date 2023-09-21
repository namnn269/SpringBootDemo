package com.example.springbootdemo.multi_threading.ex2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.*;

public class ProducerCustomerLimit {
    private static final int MAX_SIZE = 5;
    private final Queue<Integer> queue;
    private final Lock lock;
    private final Condition queueNotFull;
    private final Condition queueNotEmpty;

    public ProducerCustomerLimit() {
        queue = new LinkedList<>();
        lock = new ReentrantLock();
        queueNotFull = lock.newCondition();
        queueNotEmpty = lock.newCondition();
    }

    public void produce() {
        try {
            lock.lock();
            while (queue.size() == MAX_SIZE) {
                try {
                    queueNotFull.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            int number = new Random().nextInt(100);
            System.out.println("Producing: " + number);
            queue.add(number);
            queueNotEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void consume() {
        try {
            lock.lock();
            while (queue.isEmpty()) {
                try {
                    queueNotEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("consuming: " + queue.poll());
            queueNotFull.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
