package com.example.springbootdemo.multi_threading.ex2;

public class CustomerThread extends Thread {
    private final ProducerCustomerLimit producerCustomer;

    public CustomerThread(ProducerCustomerLimit producerCustomer) {
        this.producerCustomer = producerCustomer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            producerCustomer.consume();
        }
    }
}
