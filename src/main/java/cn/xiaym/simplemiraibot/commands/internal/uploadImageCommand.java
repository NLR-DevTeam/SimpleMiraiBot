package cn.xiaym.simplemiraibot.commands.internal;

import cn.xiaym.simplemiraibot.commands.Command;
import cn.xiaym.simplemiraibot.commands.CommandExecutor;
import cn.xiaym.simplemiraibot.utils.Logger;
import cn.xiaym.simplemiraibot.utils.mirai.ExtResourceUtil;
import net.mamoe.mirai.message.data.Image;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;

import java.io.File;
import java.util.ArrayList;

public class uploadImageCommand extends Command implements CommandExecutor {
    public uploadImageCommand() {
        super("uploadImage", "上传一张图片并返回它的 Mirai Code.", "/uploadImage <字符串: 文件路径>");
        setExecutor(this);
    }

    public Completer getCommandCompleter(String label, ArrayList<String> args) {
        return new Completers.FilesCompleter(new File("."));
    }

    public void onCommand(String input, ArrayList<String> args) {
        if (args.size() <= 1) {
            Logger.warning("使用方法: " + getUsage());
            return;
        }

        new Thread(() -> {
            Logger.info("正在上传图片...");

            try {
                Image image = ExtResourceUtil.uploadImage(input.substring(13));
                Logger.info("图片上传成功! 图片的 Mirai 代码为: [mirai:image:" + image.getImageId() + "]");
            } catch (Exception e) {
                Logger.warning("图片上传失败: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
