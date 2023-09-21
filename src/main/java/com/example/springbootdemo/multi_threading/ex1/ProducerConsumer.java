package com.example.springbootdemo.multi_threading.ex1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProducerConsumer {
    private int product;
    private boolean isProduced;
    private final Lock lock;
    private final Condition produceCondition;
    private final Condition consumeCondition;

    public ProducerConsumer() {
        this.product = 0;
        this.isProduced = false;
        this.lock = new ReentrantLock();
        this.consumeCondition = lock.newCondition();
        this.produceCondition = lock.newCondition();
    }

    public void produce() {
        try {
            lock.lock();
            while (isProduced) {
                try {
                    produceCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            product++;
            System.out.println("Produced: " + product);
            this.isProduced = true;
            consumeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume() {
        try {
            lock.lock();
            while (!isProduced) {
                try {
                    consumeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Consume: " + product);
            isProduced = false;
            produceCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
