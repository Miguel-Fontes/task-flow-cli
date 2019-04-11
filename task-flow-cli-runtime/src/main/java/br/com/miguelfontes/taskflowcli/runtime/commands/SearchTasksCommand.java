package br.com.miguelfontes.taskflowcli.runtime.commands;

import br.com.miguelfontes.taskflow.ports.tasks.SearchTasks;
import br.com.miguelfontes.taskflow.ports.tasks.SearchTasksRequest;
import br.com.miguelfontes.taskflowcli.runtime.Command;
import com.beust.jcommander.Parameters;

/**
 * @author Miguel Fontes
 */
@Parameters(commandNames = "search-tasks", commandDescription = "searches tasks by a given criteria")
public class SearchTasksCommand implements Command {

    private final SearchTasks searchTasks;

    private SearchTasksCommand(SearchTasks searchTasks) {
        this.searchTasks = searchTasks;
    }

    public static SearchTasksCommand instance(SearchTasks searchTasks) {
        return new SearchTasksCommand(searchTasks);
    }

    public void execute() {
        System.out.println("Searching for tasks...");

        var request = SearchTasksRequest.builder().build();
        var response = searchTasks.execute(request);

        System.out.println("Found tasks: " + (response.getTasks().toString()));
    }


}
