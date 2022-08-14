package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;

import java.util.ArrayList;

public class changeGroupCommand extends Command implements CommandExecutor {
    public changeGroupCommand() {
        super("changeGroup", "切换机器人当前的聊群.", "/changeGroup <整数: 聊群号>");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        try {
            BotMain.changeGroup(Long.parseLong(args.get(1)));
        } catch (NumberFormatException ex) {
            Logger.warning("无法解析聊群号，请重试.");
        }
    }
}
