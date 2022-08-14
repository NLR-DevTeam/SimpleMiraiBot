package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.ArrayList;

public class replyCommand extends Command implements CommandExecutor {
    public replyCommand() {
        super("reply", "回复一条消息.", "/reply <整数: 消息ID> <字符串: 消息>");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 2) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        try {
            BotMain.sendMessage(new QuoteReply(
                    BotMain.getMessageByMessageID(Integer.parseInt(args.get(1))))
                    .plus(MiraiCode.deserializeMiraiCode(input.substring(args.get(0).length() + args.get(1).length() + 3))
                    ));
        } catch (Exception e) {
            Logger.warning("回复失败!");
            e.printStackTrace();
        }
    }
}
