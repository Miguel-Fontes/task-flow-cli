package br.com.miguelfontes.taskflowcli.grpc;

import br.com.miguelfontes.taskflow.ports.tasks.SearchTasks;
import br.com.miguelfontes.taskflow.ports.tasks.SearchTasksRequest;
import br.com.miguelfontes.taskflow.ports.tasks.SearchTasksResponse;
import br.com.miguelfontes.taskflow.ports.tasks.TaskDTO;
import br.com.miguelfontes.taskflow.tasks.grpc.TasksServiceGrpc;
import br.com.miguelfontes.taskflow.tasks.grpc.TasksServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static br.com.miguelfontes.taskflow.tasks.grpc.TasksServiceGrpc.newBlockingStub;

public class SearchTasksGrpc implements SearchTasks {

    private final ManagedChannel channel;
    private final TasksServiceGrpc.TasksServiceBlockingStub stub;


    public SearchTasksGrpc(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    private SearchTasksGrpc(ManagedChannel channel) {
        this.channel = channel;
        stub = newBlockingStub(channel);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    @Override
    public SearchTasksResponse execute(SearchTasksRequest searchTasksRequest) {
        var request = TasksServiceOuterClass.SearchTasksRequest.newBuilder()
                .build();

        var response = stub.search(request);

        try {
            shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return SearchTasksResponse.of(response.getTasksList().stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList()));
    }

    private TaskDTO toTaskDTO(TasksServiceOuterClass.Task response) {
        return TaskDTO.of(
                UUID.fromString(response.getId()),
                response.getTitle(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                response.getStatus(),
                UUID.fromString(response.getAuthor())
        );
    }
}
