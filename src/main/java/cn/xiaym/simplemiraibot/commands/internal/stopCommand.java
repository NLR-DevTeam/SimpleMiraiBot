package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;

import java.util.ArrayList;

public class stopCommand extends Command implements CommandExecutor {
    public stopCommand() {
        super("stop", "停止并关闭 Simple-Mirai-Bot.", "/stop");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        BotMain.shutdown();
    }
}
