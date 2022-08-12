package cn.xiaym.simplemiraibot.eventListeners;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

import java.util.Objects;

public class GroupMessageListener implements ListenerHost {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        String senderName = event.getSenderName();
        String message = event.getMessage().contentToString();
        long senderQQ = event.getSender().getId();
        long groupId = event.getGroup().getId();

        if (!BotMain.isAllowedGroup(groupId)) return;

        int MessageID = BotMain.requestNewMessageID(event.getSource());

        Logger.info(String.format("[%d] 群/%d - %s (%d) : %s",
                MessageID, groupId, senderName, senderQQ, message));
    }

    @EventHandler
    public void onGroupMessagePostSendEvent(GroupMessagePostSendEvent event) {
        String botName = event.getBot().getNick();
        String message = event.getMessage().contentToString();
        long targetId = event.getTarget().getId();

        int MessageID = BotMain.requestNewMessageID(Objects.requireNonNull(event.getReceipt()).getSource());

        Logger.info(String.format("[%d] %s -> 群 %d : %s",
                MessageID, botName, targetId, message));
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
}
