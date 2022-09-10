package cn.xiaym.simplemiraibot.utils.bot;

import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandManager;
import cn.xiaym.simplemiraibot.utils.ArgumentParser;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements Completer {
    private static Completer commandsCompleter = new ArgumentCompleter(
            new StringsCompleter(CommandManager.getCommandNames()), new NullCompleter());

    public static void updateCompleter() {
        commandsCompleter = new ArgumentCompleter(
                new StringsCompleter(CommandManager.getCommandNames()), new NullCompleter());
    }

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        String line = parsedLine.line();
        ArrayList<String> args = ArgumentParser.parse(line);

        // Get command's completer
        Command cmd;

        if (args.size() < 1 || !CommandManager.getCommandNames().contains(args.get(0))) {
            commandsCompleter.complete(lineReader, parsedLine, list);
            return;
        }

        cmd = CommandManager.getCommand(args.get(0).substring(1));

        if (cmd == null) return;

        Completer cmdCompleter = cmd.getCommandCompleter(line, args);
        if (cmdCompleter == null) cmdCompleter = new NullCompleter();

        new ArgumentCompleter(new StringsCompleter(CommandManager.getCommandNames()), cmdCompleter, new NullCompleter())
                .complete(lineReader, parsedLine, list);
    }
}
