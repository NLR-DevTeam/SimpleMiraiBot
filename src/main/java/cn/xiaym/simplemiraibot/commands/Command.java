package cn.xiaym.simplemiraibot.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Command {
    private String name, description, usage;
    private CommandExecutor executor;

    public Command(@NotNull String commandName, @NotNull String commandDescription, @Nullable String commandUsage) {
        this.name = commandName;
        this.description = commandDescription;
        this.usage = commandUsage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }
}
