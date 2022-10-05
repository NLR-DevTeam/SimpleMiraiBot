package cn.xiaym.simplemiraibot;

import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandManager;
import cn.xiaym.simplemiraibot.eventListeners.BotEventListener;
import cn.xiaym.simplemiraibot.eventListeners.FriendEventListener;
import cn.xiaym.simplemiraibot.eventListeners.GroupEventListener;
import cn.xiaym.simplemiraibot.plugins.JavaPlugin;
import cn.xiaym.simplemiraibot.plugins.PluginManager;
import cn.xiaym.simplemiraibot.utils.ArgumentParser;
import cn.xiaym.simplemiraibot.utils.Logger;
import cn.xiaym.simplemiraibot.utils.bot.CommandCompleter;
import cn.xiaym.simplemiraibot.utils.bot.ConfigUtil;
import cn.xiaym.simplemiraibot.utils.bot.InputReader;
import cn.xiaym.simplemiraibot.utils.mirai.ProtocolConvertor;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.BotIsBeingMutedException;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Simple Mirai Bot 主类
 */
public class BotMain {
    private static LineReader JLineLineReader;
    private static Thread readingThread;
    private static Bot bot;
    private static boolean acceptAllGroup;
    private static boolean debug;
    private static Long defaultGroup;
    private static int currentMessageID = 0;
    private static final HashMap<Integer, MessageSource> currentMessages = new HashMap<>();
    private final static ArrayList<Long> allowedGroups = new ArrayList<>();
    private final static ArrayList<String> attrList = new ArrayList<>();

    /**
     * SMB 的入口点，包含配置读取、Bot 登录等
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            Terminal JLineTerminal = TerminalBuilder.builder().jansi(true).build();
            JLineLineReader = LineReaderBuilder.builder().terminal(JLineTerminal).completer(new CommandCompleter()).build();
            readingThread = new Thread(new InputReader());
        } catch (IOException JLineEx) {
            System.out.println("无法初始化 JLine!");
            JLineEx.printStackTrace();
            System.exit(1);
        }

        try {
            ConfigUtil.init();
        } catch (Exception ConfigEx) {
            Logger.err("无法初始化配置文件!");
            ConfigEx.printStackTrace();
            System.exit(1);
        }

        Logger.info("""
                ##################################################
                # 正在启动 Simple Mirai Bot
                # 此项目由 Mirai 驱动，遵循 AGPLv3 协议
                # 此项目目前处于开发阶段，遇到问题请反馈
                # 如果登录时遇到验证码弹窗请打开配置文件查看解决方法
                ##################################################
                """);

        if (ConfigUtil.getConfig().getLong("account.qq") == 0L
                || ConfigUtil.getConfig().getString("account.password").equals("")) {
            Logger.warning("""
                    警告: 您尚未填写 QQ号 或 密码，请在 BotConfig.yml 中更改.
                    Simple Mirai Bot 即将关闭.
                    """);
            shutdown();
        }

        debug = ConfigUtil.getConfig().getBoolean("misc.debug");
        if (debug) Logger.info("调试模式已启用.");

        if (new File("mirai").mkdir()) Logger.info("检测到 mirai 文件夹不存在，已自动创建.");
        if (new File("mirai/cache").mkdir()) Logger.info("检测到 mirai 缓存文件夹不存在，已自动创建.");

        // create bot
        bot = BotFactory.INSTANCE.newBot(ConfigUtil.getConfig().getLong("account.qq"),
                ConfigUtil.getConfig().getString("account.password"),
                new BotConfiguration() {{
                    if (!ConfigUtil.getConfig().getBoolean("misc.logBotMessages")) {
                        noBotLog();
                        noNetworkLog();
                    }

                    if (ConfigUtil.getConfig().getBoolean("misc.useCache")) enableContactCache();

                    fileBasedDeviceInfo();
                    setWorkingDir(new File("mirai"));
                    setCacheDir(new File("cache"));

                    setProtocol(ProtocolConvertor.convertToProtocol(
                            ConfigUtil.getConfig().getString("account.protocol")
                    ));

                    Logger.info("正在使用 " + ProtocolConvertor.convertToString(getProtocol()) + " 协议登录 QQ: " + ConfigUtil.getConfig().getLong("account.qq"));
                }});

        bot.getEventChannel().registerListenerHost(new BotEventListener());
        bot.getEventChannel().registerListenerHost(new GroupEventListener());
        bot.getEventChannel().registerListenerHost(new FriendEventListener());

        bot.login();

        if (!bot.isOnline()) {
            Logger.warning("机器人登录失败，请重试。");
            shutdown();
        }

        setAttr("BotLoggedIn", true);

        Logger.info("机器人登录成功!");
        Logger.info("机器人的 QQ 昵称为: " + bot.getNick());

        long defGroup = ConfigUtil.getConfig().getLong("chatting.defaultGroup");
        changeGroup(defGroup);

        PluginManager.init();

        try {
            allowedGroups.clear();

            if (ConfigUtil.getConfig().getString("chatting.listeningGroups").contains("*")) {
                acceptAllGroup = true;
                throw new InterruptedException();
            }

            for (String part : ConfigUtil.getConfig().getString("chatting.listeningGroups").split(",")) {
                allowedGroups.add(Long.parseLong(part.trim()));
            }
        } catch (InterruptedException ignored) {
        } catch (Exception ex) {
            Logger.err("无法转换监听群列表，请检查格式.");
            if (BotMain.useDebug()) ex.printStackTrace();
            shutdown();
        }

        readingThread.start();
    }

    /**
     * 设置 "属性"，可用 hasAttr 方法查看是否包含属性
     *
     * @param key     属性名
     * @param enabled 设置为 true 时添加属性, 为 false 时清除属性
     * @see BotMain#hasAttr(String)
     */
    public static void setAttr(String key, boolean enabled) {
        if (enabled) {
            attrList.add(key);
        } else
            attrList.remove(key);
    }

    /**
     * 检查是否包含某个属性
     *
     * @param key 属性名
     * @return 是否包含指定属性
     * @see BotMain#setAttr(String, boolean)
     */
    public static boolean hasAttr(String key) {
        return attrList.contains(key);
    }

    /**
     * 获取通用 LineReader 对象
     *
     * @return SMB 默认 LineReader 对象
     */
    public static LineReader getLineReader() {
        return JLineLineReader;
    }

    /**
     * 切换机器人聊天的聊群
     *
     * @param groupId 聊群号
     */
    public static void changeGroup(long groupId) {
        if (!bot.getGroups().contains(groupId)) {
            Logger.warning("机器人未在聊群 (ID " + groupId + ") 中，发送消息将受到限制，请使用 /changeGroup <聊群号> 来切换聊群。");
            defaultGroup = null;
        } else {
            Logger.info("当前进入的聊群为: " +
                    Objects.requireNonNull(bot.getGroup(groupId)).getName()
                    + " (" + groupId + ") ."
            );
            defaultGroup = groupId;
        }
    }

    /**
     * LineReader 方法，在输入框之前打印字符串，推荐使用 Logger
     *
     * @param str 要打印的字符串
     * @see Logger
     */
    public static void printAbove(String str) {
        JLineLineReader.printAbove(str);
    }

    /**
     * 关闭 Simple Mirai Bot <br/>
     * 会执行每个插件的 onShutdown 方法，然后正常退出
     */
    public static void shutdown() {
        Logger.info("正在关闭 SimpleMiraiBot ...");
        readingThread.interrupt();

        for (JavaPlugin plugin : PluginManager.getPlugins()) {
            try {
                plugin.onShutdown();
            } catch (Exception e) {
                Logger.err("无法执行插件 " + plugin.getPluginName() + " 的 onShutdown 方法.");
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    /**
     * 很抽象的方法名 <br/>
     * 实际上是处理用户输入，可以模拟运行命令 (以 "/" 开头)
     *
     * @param input 用户输入的内容
     */
    public static void processInput(String input) {
        if (input.isEmpty()) return;

        if (input.startsWith("/")) {
            ArrayList<String> args = ArgumentParser.parse(input.substring(1));

            if (args.size() == 0) {
                Logger.warning("未知命令!");
                return;
            }

            Command cmd = CommandManager.getCommand(args.get(0));

            if (cmd == null) {
                Logger.warning("未知命令!");
                return;
            }

            try {
                cmd.getExecutor().onCommand(input, args);
            } catch (NullPointerException ex) {
                Logger.err("此命令不包含 Executor, 执行失败.");
            }

            return;
        }

        if (defaultGroup == null) return;

        sendMessage(MiraiCode.deserializeMiraiCode(input));
    }

    /**
     * 通用的方法，向机器人主动聊天的聊群发送消息 <br/>
     * 有 try-catch 环绕，可防止机器人被禁言而报错
     *
     * @param message 消息内容
     */
    public static void sendMessage(Message message) {
        try {
            Objects.requireNonNull(getCurrentGroup()).sendMessage(message);
        } catch (BotIsBeingMutedException e) {
            Logger.warning("机器人已经被禁言，无法发言。");
        }
    }

    /**
     * 返回是否使用调试模式
     *
     * @return 是否使用调试模式
     * @see BotMain#main(String[])
     */
    public static boolean useDebug() {
        return debug;
    }

    /**
     * 返回是否允许接收某个聊群的消息
     *
     * @param groupID 聊群号
     * @return 是否允许接收指定聊群的消息
     * @see ConfigUtil#init()
     */
    public static boolean isAllowedGroup(long groupID) {
        if (acceptAllGroup) return true;
        return allowedGroups.contains(groupID);
    }

    /**
     * 请求新的消息 ID
     *
     * @param source 消息源
     * @return 新消息 ID
     */
    public static int requestNewMessageID(MessageSource source) {
        synchronized (BotMain.class) {
            currentMessageID++;
            currentMessages.put(currentMessageID, source);
            return currentMessageID;
        }
    }

    /**
     * 根据消息时间获取消息 ID
     *
     * @param messageTime 消息时间
     * @return 消息 ID
     */
    public static int getMessageIDByMessageTime(int messageTime) {
        for (Integer id : currentMessages.keySet())
            if (currentMessages.get(id).getTime() == messageTime)
                return id;

        return -1;
    }

    /**
     * 根据消息 ID 获取 MessageSource
     *
     * @param id 消息 ID
     * @return 消息源
     */
    public static MessageSource getMessageByMessageID(int id) {
        return currentMessages.get(id);
    }

    /**
     * API方法, 获取所有消息
     *
     * @return Map[消息ID, 消息源]
     */
    public static HashMap<Integer, MessageSource> getMessages() {
        return currentMessages;
    }

    /**
     * 获取机器人对象
     *
     * @return 机器人对象
     */
    public static Bot getBot() {
        return bot;
    }

    /**
     * 获取机器人正在聊天的群对象
     *
     * @return 机器人正在聊天的群对象
     */
    public static Group getCurrentGroup() {
        return bot.getGroup(defaultGroup);
    }
}