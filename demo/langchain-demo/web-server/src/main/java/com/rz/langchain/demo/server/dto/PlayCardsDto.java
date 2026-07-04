package com.rz.langchain.demo.server.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlayCardsDto implements Serializable {
    /**
     * 本次打出的牌列表，例如：["K【红桃】", "K【黑桃】"]。如果没有出牌则传空数组 []
     */
    private List<String> playCards;

    /**
     * 判断手中是否还有牌，仅限填入以下两个值之一："无牌-胜利" 或 "有牌-继续"
     */
    private String gameStatus;

    /**
     * 当前手中剩余的牌列表，例如：["K【黑桃】", "4【红桃】", "A【草花】"]。如果没有剩余牌则传空数组 []
     */
    private List<String> remainingCards;

    /**
     * 当 gameStatus 为 "无牌-胜利" 时，此处必须填入整场比赛的完整过程总结；
     * 当 gameStatus 为 "有牌-继续" 时，此处填入当前这一轮的动作描述
     * （例如："我出【XXX】～对方出【YYY】～我不要-对方出【ZZZ】"）
     */
    private String summary;
}
