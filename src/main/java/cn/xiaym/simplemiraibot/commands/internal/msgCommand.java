package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.contact.Friend;

import java.util.ArrayList;
import java.util.Objects;

public class msgCommand extends Command implements CommandExecutor {
    public msgCommand() {
        super("msg", "私聊一位好友", "/msg <整数: 好友 QQ> <字符串: 消息>");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 2) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        try {
            Friend friend = BotMain.getBot().getFriend(Long.parseLong(args.get(1)));
            Objects.requireNonNull(friend).sendMessage(input.substring(args.get(0).length() + args.get(1).length() + 3));
            Logger.info("发送成功!");
        } catch (NumberFormatException NFEx) {
            Logger.warning("无法解析好友 QQ.");
        } catch (Exception Ex) {
            Logger.info("发送失败!");
            Ex.printStackTrace();
        }
    }
}
