package cn.xiaym.simplemiraibot.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

        if (checkGlobal && result == null) {
            result = PluginManager.findClass(name);
            classes.put(name, result);
        }

        if (result == null)
            try {
                result = super.findClass(name);
            } catch (Exception e) {
                // Failed to find class from fw classloader, I manually define it from the jar file
                try {
                    JarFile jarFile = new JarFile(getPluginFile());
                    JarEntry jarEntry = jarFile.getJarEntry(name.replace(".", "/") + ".class");
                    if (jarEntry == null) throw new ClassNotFoundException("Class file not exists.");

                    InputStream is = jarFile.getInputStream(jarEntry);
                    byte[] target = new byte[is.available()];
                    if (is.read(target) < 0) throw new ClassNotFoundException("Unable to read class file.");

                    jarFile.close();
                    is.close();

                    result = defineClass(name, target, 0, target.length);
                } catch (IOException ex) {
                    throw new ClassNotFoundException("Jar file not exists.");
                }
            }

        if (result != null) classes.put(name, result);
        return result;
    }
}
