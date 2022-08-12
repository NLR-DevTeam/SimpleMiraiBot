package cn.xiaym.simplemiraibot.utils;

import cn.xiaym.simplemiraibot.BotMain;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class Logger {
    static {
        System.setOut(new outObserver());
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        if(BotMain.useDebug()) System.setErr(new outObserver());
    }

    private static void out(Object Printing, String Prefix, Color PrefixColor, Color TextColor) {
        for(String line : String.valueOf(Printing).split("\n")) {
            StringBuilder sb = new StringBuilder();

            sb.append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            sb.append(" [").append(ansi().fgBright(PrefixColor).bold().a(Prefix).reset()).append("] ");
            sb.append(ansi().fgBright(TextColor).a(line).reset());

            BotMain.printAbove(sb.toString());
            System.out.flush();
        }
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
}

final class outObserver extends PrintStream {

    public outObserver() {
        super(new ByteArrayOutputStream());
    }

    public void print(String o) { println(o); }
    public void print(Object o) { println(o); }

    public void println(String o) { Logger.info(o); }
    public void println(Object o) { Logger.info(o); }
}
