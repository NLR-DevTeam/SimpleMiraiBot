package cn.xiaym.simplemiraibot.utils.bot;

import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class ConfigUtil {
    private static final YamlFile yml = new YamlFile("BotConfig.yml");

    public static void init() throws IOException {
        yml.createOrLoadWithComments();

        yml.setCommentFormat(YamlCommentFormat.PRETTY);

        yml.options().headerFormatter()
                .prefixFirst("######################")
                .commentPrefix("##  ")
                .suffixLast("\n######################");

        yml.setHeader("""
                Simple Mirai Bot 配置文件
                A lightweight bot powered by Mirai.
                
                如果需要完成验证码，请根据此步骤走:
                1. 把第一个弹窗中的 URL 复制到电脑浏览器，按F12打开DevTools，切换到 "网络"
                2. 完成验证码，找到 cap_union_new_verify 一行，预览内容
                3. 复制 json 中 key 为 ticket 的 value
                4. 粘贴到下面一行按下回车
                5. 如果需要设备验证，同样在浏览器打开（建议使用手机扫码验证）
                6. 成功后关闭窗口，即可登录""");

        // Section: account
        yml.addDefault("account.qq", 0L);
        yml.addDefault("account.password", "");
        yml.addDefault("account.protocol", "AndroidPhone");

        yml.path("account").comment("机器人 QQ 设置");
        yml.path("account.qq").comment("机器人的 QQ 号码");
        yml.path("account.password").comment("机器人 QQ 的密码");
        yml.path("account.protocol").comment("机器人登录时使用的登录协议\n可用值: AndroidPhone, AndroidPad, AndroidWatch, IPad, MacOS");

        // Section: misc
        yml.addDefault("misc.logBotMessages", false);
        yml.addDefault("misc.useCache", false);
        yml.addDefault("misc.debug", false);

        yml.path("misc.logBotMessages").comment("是否输出 Mirai 的 debug 消息（如登录遇到问题可以设置为 true）");
        yml.path("misc.useCache").comment("启用机器人缓存（使得机器人启动更快，但是可能出现联系人列表不同步等情况）");
        yml.path("misc.debug").comment("是否启用调试模式（会详细打印错误日志以及启用 System.err）");

        // Section: chatting
        yml.addDefault("chatting.listeningGroups", "*");
        yml.addDefault("chatting.defaultGroup", 0L);
        yml.addDefault("chatting.listenFriends", true);

        yml.path("chatting.listeningGroups").comment("填写机器人要监听的聊群，如有多个请用英文逗号（,）分割，设置为 * 监听全部聊群");
        yml.path("chatting.defaultGroup").comment("填写机器人发送消息的默认聊群（可在启动后手动切换）");
        yml.path("chatting.listenFriends").comment("是否监听好友私聊消息（如果您使用自己的插件监听请设置为 false）");

        yml.save();
    }

    public static YamlFile getConfig() {
        return yml;
    }
}
