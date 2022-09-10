package cn.xiaym.simplemiraibot.utils.bot;

import cn.xiaym.simplemiraibot.BotMain;
import cn.xiaym.simplemiraibot.utils.Logger;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

public class InputReader implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                BotMain.processInput(BotMain.getLineReader().readLine("> "));
            } catch (UserInterruptException | EndOfFileException CancelEx) {
                BotMain.shutdown();
                break;
            } catch (Exception e) {
                Logger.err("在 InputReader 中发生了未预期的错误!");
                e.printStackTrace();
                BotMain.shutdown();
                break;
            }
        }
    }
}
