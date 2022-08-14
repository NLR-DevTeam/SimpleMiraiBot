package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.ArrayList;

public class recallCommand extends Command implements CommandExecutor {
    public recallCommand() {
        super("recall", "撤回一条消息.", "/recall <整数: 消息ID>");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        try {
            MessageSource.recall(BotMain.getMessageByMessageID(Integer.parseInt(args.get(1))));
            Logger.info("撤回成功!");
        } catch (Exception e) {
            Logger.warning("撤回失败!");
            e.printStackTrace();
        }
    }
}
