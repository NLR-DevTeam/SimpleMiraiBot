package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.contact.Group;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.ArrayList;

public class changeGroupCommand extends Command implements CommandExecutor {
    public changeGroupCommand() {
        super("changeGroup", "切换机器人当前的聊群.", "/changeGroup <整数: 聊群号>");
        setExecutor(this);
    }

    @Override
    public Completer getCommandCompleter(String label, ArrayList<String> args) {
        ArrayList<String> arr = new ArrayList<>();

        for (Group group : BotMain.getBot().getGroups().stream().filter(group -> group.getMembers().size() > 0).toList()) {
            arr.add(String.valueOf(group.getId()));
        }

        return new StringsCompleter(arr);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        try {
            BotMain.changeGroup(Long.parseLong(args.get(1).trim()));
        } catch (NumberFormatException ex) {
            Logger.warning("无法解析聊群号，请重试.");
        }
    }
}
