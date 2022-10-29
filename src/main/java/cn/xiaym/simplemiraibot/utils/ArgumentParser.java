package cn.xiaym.simplemiraibot.utils;

import java.util.ArrayList;

public class ArgumentParser {
    public static ArrayList<String> parse(String origin) {
        if (origin.isBlank()) return new ArrayList<>();

        boolean shouldMerge = false;

        // 转义空格与引号
        final String processedString = origin.trim()
                .replace("\\\"", "\uEEEE")
                .replace("\\ ", "\uEEEF");

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

                case '\uEEEE' -> temp.append('"'); //引号转义
                case '\uEEEF' -> temp.append(' '); //空格转义

                default -> temp.append(ch);
            }
        }

        if (temp.length() > 0) output.add(temp.toString());

        return output;
    }
}
