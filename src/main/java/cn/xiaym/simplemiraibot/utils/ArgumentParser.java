package cn.xiaym.simplemiraibot.utils;

import java.util.ArrayList;

public class ArgumentParser {
    /* 返回一个解析完的ArrayList
     * 调用方法 argumentParser.parse(String 要解析的内容)
     */
    public static ArrayList<String> parse(String origin) {
        //如果字符串为空则返回一个空的AL
        if (origin.trim().equals("")) return new ArrayList<>();

        //初始化部分
        String[] split = origin.split(" ");
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        boolean merging = false;
        StringBuilder tmp = new StringBuilder();

        //解析部分
        for(String item : split) {
            //如果为 "xxx" (里面不带空格)
            if(item.startsWith("\"") && item.endsWith("\"")) {
                //这里的if是为了防止throw ArrayIndexOutOfBoundsException
                if(item.length() > 1) output.add(item.substring(1, item.length() - 1));
                continue;
            }

            if(item.startsWith("\"") && !item.startsWith("\\\"")) {
                tmp.append(item.substring(1)).append(" ");
                merging = true;
                continue;
            }

            if(item.endsWith("\"") && !item.endsWith("\\\"")) {
                tmp.append(item, 0, item.length() - 1);
                output.add(tmp.toString());
                tmp.delete(0, tmp.length());
                merging = false;
                continue;
            }

            if(merging) {
                tmp.append(item).append(" ");
                continue;
            }

            //不符合上述类型 返回为不带引号的xxx
            output.add(item);
        }

        for (String s : output) {
            result.add(s.replace("\\\"", "\""));
        }

        return result;
    }
}
