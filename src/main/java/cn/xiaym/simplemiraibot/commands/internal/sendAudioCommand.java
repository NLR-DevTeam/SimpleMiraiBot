package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import cn.xiaym.simplemiraibot.utils.mirai.ExtResourceUtil;
import net.mamoe.mirai.message.data.Audio;

import java.util.ArrayList;

public class sendAudioCommand extends Command implements CommandExecutor {
    public sendAudioCommand() {
        super("sendAudio", "上传一个音频文件并作为语音发送.", "/sendAudio <字符串: 文件路径>");
        setExecutor(this);
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        try {
            Audio audio = ExtResourceUtil.uploadAudio(input.substring(11));
            BotMain.getCurrentGroup().sendMessage(audio);
            Logger.info("语音发送成功!");
        } catch (Exception e) {
            Logger.warning("语音上传失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
