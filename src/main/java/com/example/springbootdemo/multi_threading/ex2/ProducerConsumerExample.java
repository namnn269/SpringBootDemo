package com.example.springbootdemo.multi_threading.ex2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProducerConsumerExample {
    private static final int MAX_CAPACITY = 5;
    private final Queue<Integer> buffer;
    private final Lock lock;
    private final Condition bufferNotFull;
    private final Condition bufferNotEmpty;

    public ProducerConsumerExample() {
        buffer = new LinkedList<>();
        lock = new ReentrantLock();
        bufferNotFull = lock.newCondition();
        bufferNotEmpty = lock.newCondition();
    }

    public void produce() throws InterruptedException {
        try {
            lock.lock();
            while (buffer.size() == MAX_CAPACITY) {
                bufferNotFull.await();
            }
            int item = new Random().nextInt(100);
            buffer.add(item);
            System.out.println("Produced: " + item);
            bufferNotEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        try {
            lock.lock();
            while (buffer.isEmpty()) {
                bufferNotEmpty.await();
            }
            int item = buffer.poll();
            System.out.println("Consumed: " + item);
            bufferNotFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ProducerConsumerExample example = new ProducerConsumerExample();

        Runnable producerTask = () -> {
            try {
                while (true) {
                    example.produce();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable consumerTask = () -> {
            try {
                while (true) {
                    example.consume();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread producerThread1 = new Thread(producerTask);
        Thread producerThread2 = new Thread(producerTask);
        Thread consumerThread = new Thread(consumerTask);

        producerThread1.start();
        producerThread2.start();
        consumerThread.start();

//        try {
//            Thread.sleep(10000);
//            producerThread1.interrupt();
//            producerThread2.interrupt();
//            consumerThread.interrupt();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}

