package cn.xiaym.simplemiraibot.commands;

import java.util.ArrayList;

public interface CommandExecutor {
    void onCommand(String input, ArrayList<String> args);
}
