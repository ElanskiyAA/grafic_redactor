package Comand;

import java.util.ArrayList;

public class CM {
    private CM(){}
    private static CM instance = null;
    private boolean LOCK = false;
    private final ArrayList<ICommand> CommandList = new ArrayList<ICommand>();
    public static CM GetInstance(){
        if (instance == null){
            instance = new CM();
        }
        return instance;
    }
    public void Registry(ICommand c){
        if (LOCK){
            return;
        }
        CommandList.add(c);
    }
    public void Undo(){
        if (CommandList.isEmpty()){
            return;
        }
        LOCK = true;
        System.out.println("Последняя комманда:");
        System.out.println(CommandList.getLast().toString());
        CommandList.removeLast();
        System.out.println("Комманды:");
        for (ICommand c : CommandList){
            System.out.println(c.toString());
            c.Execute();
        }
        LOCK = false;
    }
}
