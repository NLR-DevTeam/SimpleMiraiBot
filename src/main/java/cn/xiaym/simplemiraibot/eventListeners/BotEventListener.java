package cn.xiaym.simplemiraibot.eventListeners;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.utils.Logger;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;

public class BotEventListener implements ListenerHost {
    @EventHandler
    public void onBotOffline(BotOfflineEvent event) {
        Logger.info("机器人 \"" + event.getBot().getNick() + "\" 已下线");
    }

    @EventHandler
    public void onBotOnline(BotOnlineEvent event) {
        if (BotMain.hasAttr("botLoggedIn")) Logger.info("机器人 \"" + event.getBot().getNick() + "\" 已上线");
    }

    @EventHandler
    public void onBotOffline_Force(BotOfflineEvent.Force event) {
        Logger.info("机器人 \"" + event.getBot().getNick() + "\" 被挤下线，正在停止机器人...");
        BotMain.shutdown();
    }

    @EventHandler
    public void onNudge(NudgeEvent event) {
        UserOrBot sender = event.getFrom();
        UserOrBot target = event.getTarget();

        Logger.info(String.format("%s %s %s %s",
                sender.getNick(), event.getAction(), target.getNick(), event.getSuffix()));
    }
}
