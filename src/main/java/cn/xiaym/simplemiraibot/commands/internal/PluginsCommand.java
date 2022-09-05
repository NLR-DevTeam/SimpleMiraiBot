package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.plugins.JavaPlugin;
import cn.xiaym.simplemiraibot.plugins.PluginManager;
import cn.xiaym.simplemiraibot.utils.Logger;

import java.util.ArrayList;

public class PluginsCommand extends Command implements CommandExecutor {
    public PluginsCommand() {
        super("plugins", "列出 SMB 已经加载的插件的列表.", "/plugins");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        Logger.info("* 机器人已经加载的插件列表:");
        for (JavaPlugin plugin : PluginManager.getPlugins()) {
            Logger.info(String.format(" - %s (版本: %s, 作者: %s)",
                    plugin.getPluginName(), plugin.getPluginVersion(), plugin.getPluginAuthor()));
        }
    }
}
