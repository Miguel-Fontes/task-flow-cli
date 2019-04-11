package br.com.miguelfontes.taskflowcli.grpc;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import br.com.miguelfontes.taskflow.ports.tasks.CreateTask;
import br.com.miguelfontes.taskflow.ports.tasks.CreateTaskRequest;
import br.com.miguelfontes.taskflow.ports.tasks.CreateTaskResponse;
import br.com.miguelfontes.taskflow.ports.tasks.TaskDTO;
import br.com.miguelfontes.taskflow.tasks.grpc.TasksServiceGrpc;
import br.com.miguelfontes.taskflow.tasks.grpc.TasksServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static br.com.miguelfontes.taskflow.tasks.grpc.TasksServiceGrpc.newBlockingStub;

public class CreateTaskGrpc implements CreateTask {

    private final ManagedChannel channel;
    private final TasksServiceGrpc.TasksServiceBlockingStub stub;

    public CreateTaskGrpc(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    private CreateTaskGrpc(ManagedChannel channel) {
        this.channel = channel;
        stub = newBlockingStub(channel);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Override
    public CreateTaskResponse execute(CreateTaskRequest createTaskRequest) {
        var request = TasksServiceOuterClass.CreateTaskRequest.newBuilder()
                .setUserId(UUID.randomUUID().toString())
                .setTitle(createTaskRequest.getTitle())
                .build();

        var response = stub.create(request).getTask();

        try {
            shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return CreateTaskResponse.of(TaskDTO.of(
                UUID.fromString(response.getId()),
                response.getTitle(),
                response.getDescription(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                response.getStatus(),
                UUID.fromString(response.getAuthor())
        ));
    }
}
