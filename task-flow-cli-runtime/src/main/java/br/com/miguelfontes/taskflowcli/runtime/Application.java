package br.com.miguelfontes.taskflowcli.runtime;

import br.com.miguelfontes.taskflow.ports.tasks.CreateTask;
import br.com.miguelfontes.taskflow.ports.tasks.CreateUser;
import br.com.miguelfontes.taskflow.ports.tasks.SearchTasks;
import br.com.miguelfontes.taskflowcli.grpc.CreateTaskGrpc;
import br.com.miguelfontes.taskflowcli.grpc.SearchTasksGrpc;
import br.com.miguelfontes.taskflowcli.runtime.commands.AddTaskCommand;
import br.com.miguelfontes.taskflowcli.runtime.commands.AddUserCommand;
import br.com.miguelfontes.taskflowcli.runtime.commands.SearchTasksCommand;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * The application entry point
 *
 * @author Miguel  Fontes
 */
public final class Application {
    public static void main(String[] args) {
        Dispatcher.newInstance(getCommands(), args).dispatch();
    }

    private static List<Command> getCommands() {
        final CreateTask createTask = new CreateTaskGrpc("localhost", 8080);
        final CreateUser createUser = null;
        final SearchTasks searchTasks = new SearchTasksGrpc("localhost", 8080);


        return asList(AddTaskCommand.instance(createTask),
                AddUserCommand.instance(createUser),
                SearchTasksCommand.instance(searchTasks));
    }

}
