package cn.xiaym.simplemiraibot.plugins;

import org.simpleyaml.configuration.file.YamlFile;

import java.util.ArrayList;

public class JavaPlugin {
    private String pluginName, pluginVersion, pluginAuthor;

    public final void constructFrom(YamlFile config) {
        this.pluginName = config.getString("name");
        this.pluginVersion = config.getString("version");
        this.pluginAuthor = config.getString("author");
    }

    public final String getPluginName() {
        return pluginName;
    }

    public final String getPluginVersion() {
        return pluginVersion;
    }

    public final String getPluginAuthor() {
        return pluginAuthor;
    }

    // 插件方法
    public void onEnable() {}
    public void onShutdown() {}
    public void onCommand(String commandName, ArrayList<String> args) {}
}
