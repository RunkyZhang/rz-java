package com.rz.langchain.demo.server;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.getenv;

public class Helper {
//    public static final String GEMINI_KEY = Optional.ofNullable(getenv("GOOGLE_AI_GEMINI_API_KEY"))
//            .orElse(getenv("GEMINI_API_KEY"));
//    static final String MODEL_NAME = Optional.ofNullable(getenv("MODEL"))
//            .orElse("gemini-3-flash-preview");

    public static final String RESET = "\033[0m", BOLD = "\033[1m", DIM = "\033[2m", ITALIC = "\033[3m";
    public static final String BLUE = "\033[34m", CYAN = "\033[36m", GREEN = "\033[32m", RED = "\033[31m", YELLOW = "\033[93m";
    public static final String MAGENTA = "\033[35m", UNDERLINE = "\033[4m", STRIKE = "\033[9m", CODE_BG = "\033[37;40m";

    public static String preview(String s, int max) {
        if (s == null || s.isEmpty()) return "";
        var lines = s.split("\n");
        var p = lines[0].substring(0, Math.min(lines[0].length(), max));
        return lines.length > 1 ? p + " ... +" + (lines.length - 1) + " lines" : (lines[0].length() > max ? p + "..." : p);
    }


    public static String markdown(String md) {
        if (md == null || md.isBlank()) return "";
        var blocks = new ArrayList<String>();
        var m = Pattern.compile("(?s)```(\\w+)?\\n(.*?)\\n```").matcher(md);
        var sb = new StringBuilder();
        while (m.find()) {
            var lang = m.group(1) == null ? "" : m.group(1);
            var content = m.group(2);
            var formatted = (lang.isEmpty() ? "" : ITALIC + BOLD + lang + RESET + "\n") + content.replaceAll("(?m)^", CODE_BG) + RESET + "\n";
            m.appendReplacement(sb, Matcher.quoteReplacement("%%BLOCK_CODE_" + blocks.size() + "%%"));
            blocks.add(formatted);
        }
        m.appendTail(sb);
        var res = sb.toString()
                .replaceAll("\\*\\*(.*?)\\*\\*", BOLD + "$1" + RESET)
                .replaceAll("\\*(.*?)\\*", ITALIC + "$1" + RESET)
                .replaceAll("__(.*?)__", UNDERLINE + "$1" + RESET)
                .replaceAll("~~(.*?)~~", STRIKE + "$1" + RESET)
                .replaceAll("(?m)^> ?(.*)", ITALIC + BLUE + BOLD + "> $1" + RESET)
                .replaceAll("(?m)^([\\d]+\\.|-|\\*) (.*)", MAGENTA + BOLD + "$1" + RESET + " $2")
                .replaceAll("(?m)^(#{1,6}) (.*)", CYAN + BOLD + "$1 $2" + RESET)
                .replaceAll("(?m)^(.*?\n={2,}\n)", CYAN + BOLD + "$1" + RESET)
                .replaceAll("(?m)^(.*?\n-{2,}\n)", CYAN + BOLD + "$1" + RESET)
                .replaceAll("!?\\[(.*?)]\\((.*?)\\)", BLUE + "$1" + RESET + " (" + BLUE + UNDERLINE + "$2" + RESET + ")");
        for (int i = 0; i < blocks.size(); i++) res = res.replace("%%BLOCK_CODE_" + i + "%%", blocks.get(i));
        return res;
    }

    public static String sep() {
        try {
            var p = new ProcessBuilder("tput", "cols").redirectErrorStream(true).start();
            return DIM + "─".repeat(Math.min(Integer.parseInt(new String(p.getInputStream().readAllBytes()).trim()), 80)) + RESET;
        } catch (Exception e) {
            return DIM + "─".repeat(80) + RESET;
        }
    }

    public static Map<String, List<String>> refreshPoker(){
        String[] values = {"3", "2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4"};
        String[] suits = {"黑桃", "红桃", "草花", "方片"};
        String[] jokers = {"王【大】", "王【小】"};

        List<String> deck = new ArrayList<>();

        // 一副牌，共54张
        // 加入大小王
        Collections.addAll(deck, jokers);
        // 加入花色牌（13个点数 × 4种花色 = 52张）
        for (String value : values) {
            for (String suit : suits) {
                deck.add(value + "【" + suit + "】");
            }
        }

        // 洗牌
        Collections.shuffle(deck);

        // 分成两份，每份27张
        List<String> playerA = new ArrayList<>(deck.subList(0, 27));
        List<String> playerB = new ArrayList<>(deck.subList(27, 54));

        Map<String, List<String>> result = new HashMap<>();
        result.put("A", playerA);
        result.put("B", playerB);

        return result;
    }
}
