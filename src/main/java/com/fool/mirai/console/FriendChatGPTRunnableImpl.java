package com.fool.mirai.console;

import com.fool.mirai.console.openai.Message;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendChatGPTRunnableImpl extends ChatGPTRunnable {


    private final FriendMessageEvent event;

    public FriendChatGPTRunnableImpl(@NotNull FriendMessageEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        // 获取文本消息
        String plainTextContent = MessageChainUtils.getPlainTextContent(event.getMessage());

        // 记录用户发送的消息
        FriendChatHistoryManager.addUserContent(event.getSender().getId(), event.getSender().getId(), plainTextContent);
        List<Message> chatHistory = FriendChatHistoryManager.getCharHistory(event.getSender().getId());

        // 对话ChatGPT
        String chatGPTResponseContent = responseByChatGPT(chatHistory);
        if (chatGPTResponseContent == null) {
            YmcPlugin.INSTANCE.getLogger().error("Error");
            return;
        }

        // 将ChatGPT返回的文本添加到对话记录中
        FriendChatHistoryManager.addSystemContent(event.getSender().getId(), chatGPTResponseContent);

        // 组装机器人返回的消息
        MessageChain reply = new MessageChainBuilder().append(new QuoteReply(event.getMessage()))
                .append(new At(event.getSender().getId()))
                .append(new PlainText(chatGPTResponseContent))
                .build();

        // 发送消息
        event.getSender().sendMessage(reply);
    }


    public FriendMessageEvent getEvent() {
        return event;
    }

}
