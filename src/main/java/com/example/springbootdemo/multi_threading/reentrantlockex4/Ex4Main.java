package com.example.springbootdemo.multi_threading.reentrantlockex4;

public class Ex4Main {
    public static void main(String[] args) throws InterruptedException {

        InOutManagement inOut = new InOutManagement(3);

        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                try {
                    Thread.sleep(1000);
                    inOut.produceItems("item -> " + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread_1");

        Thread thread2 = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                try {
                    Thread.sleep(4000);
                    inOut.consumeItems();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread_2");

        thread1.start();
        thread2.start();

    }

}
