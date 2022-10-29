package cn.xiaym.simplemiraibot.plugins;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;

public class JavaPlugin {
    private String pluginName, pluginVersion, pluginAuthor;
    private File pluginFile;

    public final void constructFrom(YamlFile config, File pluginFile) {
        this.pluginName = config.getString("name");
        this.pluginVersion = config.getString("version");
        this.pluginAuthor = config.getString("author");
        this.pluginFile = pluginFile;
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

    public final File getPluginFile() {
        return pluginFile;
    }

    public final File getDataFolder() {
        File dataFolder = new File("plugins", pluginName);

        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new RuntimeException("无法创建插件 \"" + pluginName + "\" 的数据文件夹!");
        }

        if (!dataFolder.isDirectory())
            throw new RuntimeException("插件 \"" + pluginName + "\" 的数据文件夹名称已被其他文件占用!");

        return dataFolder;
    }

    // 插件方法
    public void onEnable() {
    }

    public void onShutdown() {
    }
}
