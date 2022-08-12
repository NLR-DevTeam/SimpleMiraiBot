package cn.xiaym.simplemiraibot.plugins;

import cn.xiaym.simplemiraibot.PluginManager;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class SimpleClassLoader extends URLClassLoader {
    private final File file;
    private final JavaPlugin plugin;
    private final Map<String, Class<?>> classes = new HashMap<>();

    public SimpleClassLoader(File file, String main, ClassLoader parent) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, NoSuchMethodException{
        super(new URL[]{ file.toURI().toURL() }, parent);

        this.file = file;

        try {
            Class<?> jarClass;
            try {
                jarClass = super.loadClass(main);
            } catch(ClassNotFoundException|NullPointerException e) {
                throw new ClassNotFoundException("无法找到插件类: " + main);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch(ClassCastException e) {
                throw new ClassNotFoundException("插件主类 " + main + " 没有继承 JavaPlugin 类!");
            }

            plugin = pluginClass.getDeclaredConstructor().newInstance();
        } catch(IllegalAccessException e) {
            throw new ClassNotFoundException("插件主类 " + main + " 没有公共构造器!");
        } catch(InstantiationException e) {
            throw new ClassNotFoundException("无法实例化插件.");
        }

        PluginManager.addLoader(this);
    }

    public JavaPlugin getDeclaredPlugin() {
        return plugin;
    }

    public File getPluginFile() {
        return file;
    }

    @Override protected Class<?> findClass(String n) throws ClassNotFoundException {
        return findClass(n, true);
    }

    public Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class<?> result = classes.get(name);

        if(checkGlobal && result == null) {
            result = PluginManager.findClass(name);
            classes.put(name, result);
        }

        if(result == null) {
            result = super.findClass(name);
            classes.put(name, result);
        }

        return result;
    }
}
