package com.rz.langchain.demo.server;

import java.util.*;

public class Helper {
    public static Map<String, List<String>> refreshPoker(){
        String[] values = {"3", "2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4"};
        String[] suits = {"黑桃", "红桃", "草花", "方片"};
        String[] jokers = {"王【大】", "王【小】"};

        List<String> deck = new ArrayList<>();

        // 两副牌，每副54张，共108张
        for (int d = 0; d < 2; d++) {
            // 加入大小王
            Collections.addAll(deck, jokers);
            // 加入花色牌（13个点数 × 4种花色 = 52张）
            for (String value : values) {
                for (String suit : suits) {
                    deck.add(value + "【" + suit + "】");
                }
            }
        }

        // 洗牌
        Collections.shuffle(deck);

        // 分成两份，每份54张
        List<String> playerA = new ArrayList<>(deck.subList(0, 54));
        List<String> playerB = new ArrayList<>(deck.subList(54, 108));

        Map<String, List<String>> result = new HashMap<>();
        result.put("A", playerA);
        result.put("B", playerB);

        return result;
    }
}
