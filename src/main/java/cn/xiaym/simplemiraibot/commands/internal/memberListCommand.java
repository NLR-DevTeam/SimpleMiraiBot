package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.contact.UserOrBot;

import java.util.ArrayList;

public class memberListCommand extends Command implements CommandExecutor {
    public memberListCommand() {
        super("memberList", "列出机器人当前聊群的成员列表.", "/memberList");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        Logger.info("* 当前聊群成员列表:");
        for (UserOrBot user : BotMain.getCurrentGroup().getMembers())
            Logger.info(String.format("- %s (%s)", user.getNick(), user.getId()));
    }
}
