package com.fool.mirai.console;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

public class MessageChainUtils {

    public static String getPlainTextContent(MessageChain messageChain) {
        StringBuilder stringBuilder = new StringBuilder();
        for (SingleMessage singleMessage : messageChain) {
            if (singleMessage instanceof PlainText) {
                String content = ((PlainText) singleMessage).getContent();
                stringBuilder.append(content);
            }
        }

        return stringBuilder.toString();
    }

}
