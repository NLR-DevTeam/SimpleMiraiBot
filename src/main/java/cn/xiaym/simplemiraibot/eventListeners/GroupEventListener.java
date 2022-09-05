package cn.xiaym.simplemiraibot.eventListeners;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;

import java.util.Objects;

public class GroupEventListener implements ListenerHost {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        String senderName = event.getSenderName();
        String message = event.getMessage().contentToString();
        long senderQQ = event.getSender().getId();
        long groupId = event.getGroup().getId();

        if (!BotMain.isAllowedGroup(groupId)) return;

        int MessageID = BotMain.requestNewMessageID(event.getSource());

        Logger.info(String.format("群/%d - %s (%d) : %s",
                groupId, senderName, senderQQ, message), "[" + MessageID + "] ");
    }

    @EventHandler
    public void onGroupMessagePostSendEvent(GroupMessagePostSendEvent event) {
        String botName = event.getBot().getNick();
        String message = event.getMessage().contentToString();
        long targetId = event.getTarget().getId();

        int MessageID = BotMain.requestNewMessageID(Objects.requireNonNull(event.getReceipt()).getSource());

        Logger.info(String.format("%s -> 群 %d : %s",
                botName, targetId, message), "[" + MessageID + "] ");
    }

    @EventHandler
    public void onGroupMessageRecallEvent(MessageRecallEvent.GroupRecall event) {
        if (event.getAuthorId() == event.getBot().getId()) return;

        String targetName = Objects.requireNonNull(event.getOperator()).getNick();
        long targetId = event.getAuthorId();
        long groupId = event.getGroup().getId();
        int messageId = BotMain.getMessageIDByMessageTime(event.getMessageTime());

        if (!BotMain.isAllowedGroup(groupId)) return;

        Logger.info(String.format("%s (%d) 撤回了群聊 %s 中的消息 [%d]",
                targetName, targetId, groupId, messageId));
    }

    @EventHandler
    public void onMemberMute(MemberMuteEvent event) {
        String groupName = event.getGroup().getName();
        String optName = Objects.requireNonNull(event.getOperator()).getNameCard();
        String userName = event.getUser().getNameCard();
        long groupId = event.getGroupId();
        long optQQ = event.getOperator().getId();
        long userQQ = event.getUser().getId();
        int time = event.getDurationSeconds();

        if (!BotMain.isAllowedGroup(groupId)) return;

        Logger.info(String.format("%s (%d) 在群 %s (%d) 中被 %s (%d) 禁言 %d 秒.",
                userName, userQQ, groupName, groupId, optName, optQQ, time));
    }

    @EventHandler
    public void onBotMute(BotMuteEvent event) {
        String groupName = event.getGroup().getName();
        String optName = Objects.requireNonNull(event.getOperator()).getNameCard();
        long groupId = event.getGroupId();
        long optQQ = event.getOperator().getId();
        int time = event.getDurationSeconds();

        if (!BotMain.isAllowedGroup(groupId)) return;

        Logger.info(String.format("机器人在群 %s (%d) 被 %s (%d) 禁言 %d 秒.",
                groupName, groupId, optName, optQQ, time));
    }

    @EventHandler
    public void onGroupMuteAll(GroupMuteAllEvent event) {
        String groupName = event.getGroup().getName();
        String optName = Objects.requireNonNull(event.getOperator()).getNameCard();
        long groupId = event.getGroupId();
        long optQQ = event.getOperator().getId();

        if (!BotMain.isAllowedGroup(groupId)) return;

        Logger.info(String.format("群 %s (%d) 被 %s (%d) %s了全员禁言.",
                groupName, groupId, optName, optQQ, event.getNew() ? "开启" : "关闭"));
    }

    @EventHandler
    public void onBotGroupPermissionChange(BotGroupPermissionChangeEvent event) {
        String groupName = event.getGroup().getName();
        long groupId = event.getGroupId();
        int origin = event.getOrigin().getLevel();
        int newPerm = event.getNew().getLevel();

        if (!BotMain.isAllowedGroup(groupId)) return;

        Logger.info(String.format("机器人在群 %s (%d) 的权限等级从 %d 提升到了 %s.",
                groupName, groupId, origin, newPerm));
    }
}
