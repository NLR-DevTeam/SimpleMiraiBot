package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.SingleMessage;

import java.util.ArrayList;

public class queryImageCommand extends Command implements CommandExecutor {
    public queryImageCommand() {
        super("queryImage", "查询一个消息中所有的图片.", "/queryImage <消息 ID>");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        int messageId;
        try {
            messageId = Integer.parseInt(args.get(1));
        } catch (NumberFormatException ex) {
            Logger.warning("无法解析消息 ID.");
            return;
        }

        MessageSource msg = BotMain.getMessageByMessageID(messageId);
        if (msg == null) {
            Logger.warning("消息不存在!");
            return;
        }

        for (SingleMessage singleMessage : msg.getOriginalMessage()) {
            if (singleMessage instanceof Image image) {
                Logger.info(image.isEmoji() ? "[动画表情]" : "[图片]");
                Logger.info(" - Mirai Code: [mirai:image:" + image.getImageId() + "]");
                Logger.info(" - 图片大小: " + image.getHeight() + " x " + image.getWidth());
                Logger.info(" - 占用空间: " + image.getSize() / 1024L + "KB");
            }
        }
    }
}
