package com.example.springbootdemo.multi_threading.ex2;

public class Main {
    public static void main(String[] args) {
        ProducerCustomerLimit producerCustomerLimit = new ProducerCustomerLimit();
        ProducerThread producerThread = new ProducerThread(producerCustomerLimit,"producer_1");
        ProducerThread producerThread2 = new ProducerThread(producerCustomerLimit,"producer_2");
        CustomerThread customerThread = new CustomerThread(producerCustomerLimit);

        producerThread.start();
        producerThread2.start();
        customerThread.start();

//        Runnable producer = () -> {
//            try {
//                while (true) {
//                    producerCustomerLimit.produce();
//                    Thread.sleep(1000);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//
//        Runnable consumer = () -> {
//            try {
//                while (true) {
//                    producerCustomerLimit.consume();
//                    Thread.sleep(1000);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//
//        Thread producerThread1 = new Thread(producer);
//        Thread producerThread2 = new Thread(producer);
//        Thread consumerThread = new Thread(consumer);
//        producerThread1.start();
//        producerThread2.start();
//        consumerThread.start();
    }
}
