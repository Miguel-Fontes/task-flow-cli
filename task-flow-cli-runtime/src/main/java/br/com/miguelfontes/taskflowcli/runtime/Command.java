package br.com.miguelfontes.taskflowcli.runtime;

/**
 * A marker interface to compute generic commands
 *
 * @author Miguel Fontes
 */
public interface Command {
    /**
     * Executes this command operation
     */
    void execute();
}
