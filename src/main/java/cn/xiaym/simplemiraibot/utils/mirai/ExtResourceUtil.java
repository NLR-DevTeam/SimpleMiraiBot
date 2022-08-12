package cn.xiaym.simplemiraibot.utils.mirai;

import cn.xiaym.simplemiraibot.BotMain;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.*;

public class ExtResourceUtil {
    public static Image uploadImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        if(!imageFile.exists()) throw new IOException("图片文件不存在!");

        try (FileInputStream inputStream = new FileInputStream(imageFile)) {
            ExternalResource ext = ExternalResource.create(inputStream);
            Image image = BotMain.getCurrentGroup().uploadImage(ext);
            ext.close();
            return image;
        }
    }

    public static Audio uploadAudio(String filePath) throws IOException {
        File audioFile = new File(filePath);
        if(!audioFile.exists()) throw new IOException("音频文件不存在!");

        try (FileInputStream inputStream = new FileInputStream(audioFile)) {
            ExternalResource ext = ExternalResource.create(inputStream);
            Audio audio = BotMain.getCurrentGroup().uploadAudio(ext);
            ext.close();
            return audio;
        }
    }
}
