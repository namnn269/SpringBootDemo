package com.example.springbootdemo.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        long t1 = System.currentTimeMillis();
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 8088)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(managedChannel);

        HelloRequest helloRequest = HelloRequest.newBuilder()
                .setFirstName("nguyen")
                .setLastName("nam")
                .build();

        StreamObserver<HelloResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse value) {
                System.out.println("OK: " + value);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("ERROR: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("COMPLETED:");
            }
        };
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);
        stub.hello(helloRequest, responseObserver);

        long t2 = System.currentTimeMillis();
        System.out.printf("CLIENT SHUTDOWN: %s ms\n", t2 - t1);
        managedChannel.awaitTermination(2, TimeUnit.SECONDS);
        managedChannel.shutdown();
    }
}
