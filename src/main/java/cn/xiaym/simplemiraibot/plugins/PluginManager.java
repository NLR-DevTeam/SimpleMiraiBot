package cn.xiaym.simplemiraibot.plugins;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    private final static ArrayList<SimpleClassLoader> loaders = new ArrayList<>();
    private final static ArrayList<JavaPlugin> plugins = new ArrayList<>();
    private static boolean init;

    public static boolean hasPlugin(@NotNull JavaPlugin plugin) {
        return plugins.contains(plugin);
    }

    @Nullable
    public static JavaPlugin getPlugin(String pluginName) {
        for (JavaPlugin plugin : plugins)
            if (plugin.getPluginName().equals(pluginName))
                return plugin;

        return null;
    }

    public static void init() {
        if (init) return;

        File pluginsDir = new File("plugins");
        if (!pluginsDir.exists() && !pluginsDir.mkdir()) Logger.warning("无法创建 plugins 文件夹，将无法加载插件.");

        for (File file : Objects.requireNonNull(pluginsDir.listFiles()))
            if (file.getName().endsWith(".jar")) {
                JavaPlugin plugin = constructPlugin(file);
                if (plugin != null) plugins.add(plugin);
            }

        for (JavaPlugin javaPlugin : plugins)
            try {
                javaPlugin.onEnable();
            } catch(Exception e) {
                Logger.err("无法执行插件 " + javaPlugin.getPluginName() + " 的 onEnable 方法!");
                if (BotMain.useDebug()) e.printStackTrace();
            }

        if (plugins.size() != 0 && loaders.size() != 0)
            Logger.info("所有插件已经加载完成!");

        init = true;
    }

    private static JavaPlugin constructPlugin(File pluginFile) {
        try {
            JarFile jarFile = new JarFile(pluginFile);
            JarEntry jarEntry = jarFile.getJarEntry("plugin.yml");
            if (jarEntry == null) throw new IOException("无法找到 plugin.yml");

            YamlFile yamlFile = new YamlFile();
            yamlFile.loadFromString(InputStreamToString(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8));

            SimpleClassLoader cl = new SimpleClassLoader(pluginFile, yamlFile.getString("main"), BotMain.class.getClassLoader());
            JavaPlugin p = cl.getDeclaredPlugin();
            p.constructFrom(yamlFile);
            cl.close();

            return p;
        } catch(Exception ex) {
            Logger.err("无法构造插件 " + pluginFile.getName() + ": " + ex.getMessage());
            if (BotMain.useDebug()) ex.printStackTrace();
        }

        return null;
    }

    public static Class<?> findClass(String name) {
        for (SimpleClassLoader loader : loaders) {
            try {
                return loader.findClass(name, false);
            } catch(ClassNotFoundException ignored) {}
        }

        return null;
    }

    public static void addLoader(SimpleClassLoader loader) {
        if (loader != null) loaders.add(loader);
    }

    public static String InputStreamToString(InputStream inputStream, Charset charset) {
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while(result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            return buf.toString(charset);
        } catch(Exception e) {
            return null;
        }
    }
}
