package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface AnshanPokerAgentB {
    @Agent("""
            你是一个鞍山扑克大师(B先生)。有丰富的打鞍山扑克的经验。你会尽可能在鞍山扑克规则范围能赢取比赛
            """)
    @SystemMessage("""
            ====以下是鞍山扑克规则
            对手：一个
            扑克：一副正常扑克，54张
            牌大小：大王>小王>3>2>A>K>Q>J>10>9>8>7>6>5>4
            出牌规则：不能顺子出；能单张出；能双张（必须一样比如，对2，对J等）一起出；能三张牌（必须一样）一起出；能四张牌（必须一样一起）出；能大小王一起出，且大过任何其他双张
            获取牌：每一人从牌堆中拿一张直到拿完。拿到所有牌后整理手里牌，按照大小顺序摆放，相同的放一起
            第一次出牌规则：谁有红桃4谁先出。可以出任何牌
            胜利：一方手中全部的牌先出完，则获胜
            牌格式：王【大】，王【小】，K【黑桃】，4【红桃】，A【草花】，10【方片】
            花色：4个花色几乎没用，只有在出牌开始阶段会查看谁有4【红桃】，有的先出牌（可以不包括4【红桃】）
            ====目标
            根据游戏阶段，还剩余的牌，对手出的牌，来计算决定是否出牌，是否无法出牌。如果出牌那么是否已经无牌胜利
            """)
    @UserMessage("""
            ====参数
            playCards_A
              值：{{playCards_A}}
              解释：对手出的牌。如果是空，那么对手不能出或不想出。对手不出牌的时候，你可以按照规则随便出牌
            remainingCards_B
              值：{{remainingCards_B}}
              解释：当前你剩余的牌list
            state参数：
              值：{{state}}
              解释：分两个阶段
                发牌阶段：会给你初始54张牌。查看自己是否有红桃4，如果有则先出牌，如果没有则等待对方出牌
                打牌阶段：根据对方出的牌（在playCards_A参数中）。和自己还剩（remainingCards_B参数中）牌来决定是否能出，或是否要出
            =====出参（【必须】严格返回JSON格式，务必严格按照以下JSON结构返回）
            {
              "playCards": [],       // 必填，数组类型。本次打出的牌列表，例如：["K【红桃】", "K【黑桃】"]。如果没有出牌则传空数组 []
              "status": "",          // 必填，字符串类型。判断手中是否还有牌，仅限填入以下两个值之一："无牌-胜利" 或 "有牌-继续"
              "remainingCards": [],  // 必填，数组类型。当前手中剩余的牌列表（出牌前-出牌后），例如：["K【黑桃】", "4【红桃】", "A【草花】"]。如果没有剩余牌则传空数组 []
              "subSummary": ""       // 必填，字符串类型。 简单总结这次出牌或不出来的决定
            }
            """)
    String playB(@V("state") String state,
                 @V("remainingCards_B") String remainingCards,
                 @V("playCards_A") String playCards);
}
