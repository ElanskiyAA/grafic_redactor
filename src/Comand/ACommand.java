package Comand;

public abstract class ACommand implements ICommand {
    public void Execute() {
        this.doExecute();
    }
    public abstract void doExecute();
}
