package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import cn.xiaym.simplemiraibot.utils.mirai.ExtResourceUtil;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class imageCommand extends Command implements CommandExecutor {
    public imageCommand() {
        super("image", "图片相关操作", "/image <query|upload|send> <参数>");
        setExecutor(this);
    }

    @Override
    public Completer getCommandCompleter(String label, ArrayList<String> args) {
        List<String> argList = List.of("query", "send", "upload");

        if (args.size() < 2 || !argList.contains(args.get(1)))
            return new StringsCompleter("query", "send", "upload");
        if (args.get(1).equals("upload") || args.get(1).equals("send"))
            return new ArgumentCompleter(
                    new StringsCompleter("send", "upload"),
                    new Completers.FilesCompleter(new File("."))
            );

        return new NullCompleter();
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() < 2) {
            Logger.warning("用法: " + getUsage());
            return;
        }

        switch (args.get(1)) {
            case "query" -> {
                if (args.size() < 3) {
                    Logger.warning("用法: /image query <消息ID>");
                    return;
                }

                int messageId;
                try {
                    messageId = Integer.parseInt(args.get(2).trim());
                } catch (NumberFormatException ex) {
                    Logger.warning("无法解析消息 ID.");
                    return;
                }

                MessageSource msg = BotMain.getMessageByMessageID(messageId);
                if (msg == null) {
                    Logger.warning("消息不存在!");
                    return;
                }

                Logger.info("=-=-=-= 图片列表 =-=-=-=");

                for (SingleMessage singleMessage : msg.getOriginalMessage()) {
                    if (singleMessage instanceof Image image) {
                        Logger.info(image.isEmoji() ? "[动画表情]" : "[图片]");
                        Logger.info(" - Mirai Code: [mirai:image:" + image.getImageId() + "]");
                        Logger.info(" - 图片大小: " + image.getHeight() + " x " + image.getWidth());
                        Logger.info(" - 占用空间: " + image.getSize() / 1024L + "KB");
                    }
                }
            }

            case "upload" -> {
                if (args.size() < 3) {
                    Logger.warning("用法: /image upload <图片路径>");
                    return;
                }

                new Thread(() -> {
                    Logger.info("正在上传图片...");

                    try {
                        Image image = ExtResourceUtil.uploadImage(input.substring("/image upload ".length()));
                        Logger.info("图片上传成功! 图片的 Mirai 代码为: [mirai:image:" + image.getImageId() + "]");
                    } catch (Exception e) {
                        Logger.err("图片上传失败: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }

            case "send" -> {
                if (args.size() < 3) {
                    Logger.warning("用法: /image send <图片路径>");
                    return;
                }

                new Thread(() -> {
                    Logger.info("正在上传图片...");

                    try {
                        Image image = ExtResourceUtil.uploadImage(input.substring("/image send ".length()));
                        Logger.info("图片上传成功! 图片的 Mirai 代码为: [mirai:image:" + image.getImageId() + "]");
                        Logger.info("正在发送图片...");

                        try {
                            BotMain.getCurrentGroup().sendMessage(image);
                            Logger.info("图片发送成功!");
                        } catch (Exception ex) {
                            Logger.err("图片发送失败!");
                            ex.printStackTrace();
                        }
                    } catch (Exception e) {
                        Logger.err("图片上传失败: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }

            default -> Logger.warning("用法: " + getUsage());
        }
    }
}
