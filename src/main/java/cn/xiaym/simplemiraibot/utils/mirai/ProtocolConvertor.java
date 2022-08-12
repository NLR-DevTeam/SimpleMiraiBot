package cn.xiaym.simplemiraibot.utils.mirai;

import static net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.*;
import net.mamoe.mirai.utils.BotConfiguration;

public class ProtocolConvertor {
    public static BotConfiguration.MiraiProtocol convertToProtocol(String converting) {
        return switch (converting.trim()) {
            case "AndroidWatch" -> ANDROID_WATCH;
            case "AndroidPad" -> ANDROID_PAD;
            case "IPad" -> IPAD;
            case "MacOS" -> MACOS;
            default -> ANDROID_PHONE;
        };
    }

    public static String convertToString(BotConfiguration.MiraiProtocol converting) {
        return switch (converting) {
            case ANDROID_PHONE -> "AndroidPhone";
            case ANDROID_PAD -> "AndroidPad";
            case ANDROID_WATCH -> "AndroidWatch";
            case IPAD -> "IPad";
            case MACOS -> "MacOS";
            default -> null;
        };
    }
}
