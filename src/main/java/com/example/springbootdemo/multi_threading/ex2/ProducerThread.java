package com.example.springbootdemo.multi_threading.ex2;

public class ProducerThread extends Thread {

    private final ProducerCustomerLimit producerCustomer;
    private String name;

    public ProducerThread(ProducerCustomerLimit producerCustomer, String name) {
        this.producerCustomer = producerCustomer;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("in producing :" + name);
                producerCustomer.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
