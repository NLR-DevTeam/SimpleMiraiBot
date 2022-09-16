package cn.xiaym.simplemiraibot.utils;

import java.util.ArrayList;

public class ArgumentParser {
    /* 返回一个解析完的ArrayList
     * 调用方法 argumentParser.parse(String 要解析的内容)
     */
    public static ArrayList<String> parse(String origin) {
        if (origin.isBlank()) return new ArrayList<>();

        boolean shouldMerge = false;
        final char ESCAPE = '\uEEEE';
        final String processedString = origin.trim().replace("\\\"", String.valueOf(ESCAPE));
        final StringBuilder temp = new StringBuilder();
        final ArrayList<String> output = new ArrayList<>();

        for (char ch : processedString.toCharArray()) {
            switch (ch) {
                case '\"' -> {
                    if (!shouldMerge) {
                        shouldMerge = true;
                        continue;
                    }

                    shouldMerge = false;
                    output.add(temp.toString());
                    temp.setLength(0);
                }
                case ' ' -> {
                    if (shouldMerge) {
                        temp.append(ch);
                        continue;
                    }

                    output.add(temp.toString());
                    temp.setLength(0);
                }
                case ESCAPE -> temp.append('"');
                default -> temp.append(ch);
            }
        }

        if (temp.length() > 0) output.add(temp.toString());

        return output;
    }
}
