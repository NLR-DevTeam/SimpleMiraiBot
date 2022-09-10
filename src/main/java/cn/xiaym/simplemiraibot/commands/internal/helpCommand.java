package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.commands.CommandManager;
import cn.xiaym.simplemiraibot.utils.Logger;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class helpCommand extends Command implements CommandExecutor {
    public helpCommand() {
        super("help", "显示可用的命令与它们的帮助.", "/help [字符串: 命令名]");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            showCommands();
            return;
        }

        showCommandHelp(CommandManager.getCommand(args.get(1)));
    }

    public Completer getCommandCompleter(String label, ArrayList<String> args) {
        ArrayList<String> arr = new ArrayList<>();

        for (Command cmd : CommandManager.getCommands()) arr.add(cmd.getName());

        return new StringsCompleter(arr);
    }

    private void showCommands() {
        Logger.info("=-=-=-=-= 命令帮助 =-=-=-=-=");

        Map<String, Command> unsortedMap = new HashMap<>();

        for (Command command : CommandManager.getCommands()) {
            unsortedMap.put(command.getName(), command);
        }

        for (String name : new TreeSet<>(unsortedMap.keySet())) {
            Command sortedCommand = unsortedMap.get(name);

            Logger.info(String.format("""
                    - /%s
                      * %s""", sortedCommand.getName(), sortedCommand.getDescription()));
        }
    }

    private void showCommandHelp(Command command) {
        if (command == null) {
            Logger.warning("错误: 您指定的命令不存在.");
            return;
        }

        Logger.info(String.format("""
                        命令 %s 的帮助:
                         使用方法:
                          - %s
                         命令描述:
                          - %s""",
                command.getName(), command.getUsage(), command.getDescription()));
    }
}
