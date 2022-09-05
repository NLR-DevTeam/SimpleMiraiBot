package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.contact.UserOrBot;

import java.util.ArrayList;

public class friendListCommand extends Command implements CommandExecutor {
    public friendListCommand() {
        super("friendList", "列出机器人的好友列表.", "/friendList");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        Logger.info("* 机器人的好友列表:");
        for (UserOrBot user : BotMain.getBot().getFriends())
            Logger.info(String.format("- %s (%s)", user.getNick(), user.getId()));
    }
}
