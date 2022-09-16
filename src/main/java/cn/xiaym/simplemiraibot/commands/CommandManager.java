package cn.xiaym.simplemiraibot.commands;

import cn.xiaym.simplemiraibot.commands.internal.*;
import cn.xiaym.simplemiraibot.plugins.JavaPlugin;
import cn.xiaym.simplemiraibot.utils.bot.CommandCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandManager {
    private static final Map<Command, JavaPlugin> registeredCommands = new HashMap<>();

    static {
        registeredCommands.put(new helpCommand(), null);
        registeredCommands.put(new stopCommand(), null);
        registeredCommands.put(new changeGroupCommand(), null);
        registeredCommands.put(new recallCommand(), null);
        registeredCommands.put(new replyCommand(), null);
        registeredCommands.put(new msgCommand(), null);
        registeredCommands.put(new nudgeCommand(), null);
        registeredCommands.put(new sendAudioCommand(), null);
        registeredCommands.put(new uploadImageCommand(), null);
        registeredCommands.put(new groupListCommand(), null);
        registeredCommands.put(new friendListCommand(), null);
        registeredCommands.put(new memberListCommand(), null);
        registeredCommands.put(new PluginsCommand(), null);
        registeredCommands.put(new queryImageCommand(), null);
        CommandCompleter.updateCompleter();
    }

    public static void registerCommand(@NotNull Command cmd, @NotNull JavaPlugin plugin) {
        if (registeredCommands.containsKey(cmd)) throw new IllegalStateException("Command is already registered.");
        registeredCommands.put(cmd, plugin);
        CommandCompleter.updateCompleter();
    }

    @Nullable
    public static Command getCommand(String commandName) {
        for (Command command : registeredCommands.keySet())
            if (command.getName().equals(commandName))
                return command;

        return null;
    }

    public static Set<Command> getCommands() {
        return registeredCommands.keySet();
    }

    public static ArrayList<String> getCommandNames() {
        ArrayList<String> ret = new ArrayList<>();

        for (Command cmd : getCommands()) ret.add("/" + cmd.getName());

        return ret;
    }
}
