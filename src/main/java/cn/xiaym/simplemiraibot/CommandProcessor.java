package cn.xiaym.simplemiraibot;

import cn.xiaym.simplemiraibot.utils.Logger;
import cn.xiaym.simplemiraibot.utils.mirai.ExtResourceUtil;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;

import java.io.IOException;
import java.util.ArrayList;

public class CommandProcessor {
    public static void helpCommand() {
        Logger.info("""
                =-=-=-=-= 命令帮助 =-=-=-=-=
                 - /stop
                   * 停止 Simple Mirai Bot
                 - /changeGroup <整数: 聊群号>
                   * 切换聊天聊群
                 - /recall <整数: 消息ID>
                   * 撤回一条消息
                 - /reply <整数: 消息ID> <字符串: 消息>
                   * 回复一条消息
                 - /msg <整数: 好友QQ> <字符串: 消息>
                   * 向指定的好友发送一条私聊消息
                 - /groupList
                   * 获取机器人的聊群列表
                 - /friendList
                   * 获取机器人的好友列表
                 - /uploadImage <字符串: 文件路径>
                   * 上传一个图片并返回它的 Mirai 代码
                 - /sendAudio <字符串: 音频路径>
                   * 上传一个音频作为语音并发送到当前聊群
                 - /nudge <整数: 被戳方QQ>
                   * 戳一戳某个人""");
    }

    public static void changeGroupCommand(ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: /changeGroup <整数: 聊群号>");
            return;
        }

        try {
            BotMain.changeGroup(Long.parseLong(args.get(1)));
        } catch(NumberFormatException ex) {
            Logger.warning("无法解析聊群号，请重试.");
        }
    }

    public static void stopCommand() {
        BotMain.shutdown();
    }

    public static void recallCommand(ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: /recall <整数: 消息ID>");
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

    public static void replyCommand(ArrayList<String> args, String input) {
        if (args.size() <= 2) {
            Logger.warning("使用方法: /reply <整数: 消息ID> <字符串: 消息>");
            return;
        }

        try {
            BotMain.sendMessage(new QuoteReply(
                    BotMain.getMessageByMessageID(Integer.parseInt(args.get(1))))
                    .plus(MiraiCode.deserializeMiraiCode(input.substring(args.get(0).length() + args.get(1).length() + 3))
                    ));
        } catch(Exception e) {
            Logger.warning("回复失败!");
            e.printStackTrace();
        }
    }

    public static void msgCommand(ArrayList<String> args, String input) {
        if (args.size() <= 2) {
            Logger.warning("使用方法: /msg <整数: 好友QQ> <字符串: 消息>");
            return;
        }

        try {
            Friend friend = BotMain.getBot().getFriend(Long.parseLong(args.get(1)));
            friend.sendMessage(input.substring(args.get(0).length() + args.get(1).length() + 3));
            Logger.info("发送成功!");
        } catch(NumberFormatException NFEx) {
            Logger.warning("无法解析好友 QQ.");
        } catch(Exception Ex) {
            Logger.info("发送失败!");
            Ex.printStackTrace();
        }
    }

    public static void groupListCommand() {
        Logger.info("* 机器人加入的聊群列表:");
        for (Group group : BotMain.getBot().getGroups()) Logger.info(" - " + group.getName() + " (" + group.getId() + ")");
    }

    public static void friendListCommand() {
        Logger.info("* 机器人的好友列表:");
        for (Friend friend : BotMain.getBot().getFriends()) Logger.info(" - " + friend.getNick() + " (" + friend.getId() + ")");
    }

    public static void memberListCommand() {
        Logger.info("* 当前聊群的成员列表:");
        for (NormalMember user : BotMain.getCurrentGroup().getMembers()) Logger.info(" - " + user.getNick() + " (" + user.getId() + ")");
    }

    public static void uploadImageCommand(ArrayList<String> args, String input) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: /uploadImage <字符串: 图片路径>");
            return;
        }

        try {
            Image image = ExtResourceUtil.uploadImage(input.substring(13));
            Logger.info("图片上传成功! 图片的 Mirai 代码为: [mirai:image:" + image.getImageId() + "]");
        } catch(IOException e) {
            Logger.warning("图片上传失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendAudioCommand(ArrayList<String> args, String input) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: /sendAudio <字符串: 音频路径>");
            return;
        }

        try {
            Audio audio = ExtResourceUtil.uploadAudio(input.substring(11));
            BotMain.getCurrentGroup().sendMessage(audio);
            Logger.info("语音发送成功!");
        } catch(IOException e) {
            Logger.warning("语音上传失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void nudgeCommand(ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: /nudge <整数: 被戳方QQ>");
            return;
        }

        try {
            BotMain.getCurrentGroup()
                    .get(Long.parseLong(args.get(1)))
                    .nudge()
                    .sendTo(BotMain.getCurrentGroup());
            Logger.info("戳一戳成功!");
        } catch(NumberFormatException ex) {
            Logger.warning("无法解析 QQ 号为整数.");
        } catch(NullPointerException ne) {
            Logger.warning("找不到该群成员.");
        } catch(Exception e) {
            Logger.info("戳一戳失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
