package cn.xiaym.simplemiraibot.utils;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.utils.bot.ConfigUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Logger {
    static {
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            public void println(String o) {
                Logger.info(o, "[STDOUT] ");
            }

            public void println(Object o) {
                Logger.info(o, "[STDOUT] ");
            }
        });

        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        if (ConfigUtil.getConfig().getBoolean("misc.debug"))
            System.setErr(new PrintStream(new ByteArrayOutputStream()) {
                public void println(String o) {
                    Logger.err(o, "[STDERR] ");
                }

                public void println(Object o) {
                    Logger.err(o, "[STDERR] ");
                }
            });
    }

    private static void out(Object Printing, String Prefix, Color PrefixColor, Color TextColor) {
        StringBuilder sb = new StringBuilder();
        for (String line : String.valueOf(Printing).split("\n")) {

            String s = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                    " [" + ansi().fgBright(PrefixColor).bold().a(Prefix).reset() + "] " +
                    ansi().fgBright(TextColor).a(line).reset();

            if (sb.length() > 0) sb.append("\n");
            sb.append(s);
        }

        BotMain.printAbove(sb.toString());
        System.out.flush();
    }

    public static void info(Object obj) {
        Logger.out(obj, "I", DEFAULT, DEFAULT);
    }

    public static void warning(Object obj) {
        Logger.out(obj, "W", YELLOW, YELLOW);
    }

    public static void err(Object obj) {
        Logger.out(obj, "E", RED, RED);
    }

    public static void info(Object obj, Object prefix) {
        for (String line : String.valueOf(obj).split("\n"))
            Logger.out(prefix + line, "I", DEFAULT, DEFAULT);
    }

    public static void warning(Object obj, Object prefix) {
        for (String line : String.valueOf(obj).split("\n"))
            Logger.out(prefix + line, "W", YELLOW, YELLOW);
    }

    public static void err(Object obj, Object prefix) {
        for (String line : String.valueOf(obj).split("\n"))
            Logger.out(prefix + line, "E", RED, RED);
    }
}