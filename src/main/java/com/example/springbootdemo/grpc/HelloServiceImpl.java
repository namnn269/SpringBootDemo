package com.example.springbootdemo.grpc;

import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(String.join(" ", "Hello from server: ", firstName, lastName))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
