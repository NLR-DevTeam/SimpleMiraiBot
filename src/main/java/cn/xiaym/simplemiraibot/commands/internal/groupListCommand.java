package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.contact.Group;

import java.util.ArrayList;

public class groupListCommand extends Command implements CommandExecutor {
    public groupListCommand() {
        super("groupList", "列出机器人所在的聊群列表.", "/groupList");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        Logger.info("* 机器人加入的聊群列表:");
        for (Group group : BotMain.getBot().getGroups())
            Logger.info(String.format("""
                    - %s (%s)""", group.getName(), group.getId()));
    }
}
