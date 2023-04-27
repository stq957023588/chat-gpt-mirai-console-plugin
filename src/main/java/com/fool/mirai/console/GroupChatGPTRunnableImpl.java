package com.fool.mirai.console;

import com.fool.mirai.console.openai.Message;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GroupChatGPTRunnableImpl extends ChatGPTRunnable {


    private final GroupMessageEvent event;

    public GroupChatGPTRunnableImpl(@NotNull GroupMessageEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        String plainTextContent = MessageChainUtils.getPlainTextContent(event.getMessage());

        GroupChatHistoryManager.addUserContent(event.getGroup().getId(), event.getSender().getId(), plainTextContent);
        List<Message> chatHistory = GroupChatHistoryManager.getGroupCharHistory(event.getGroup().getId());

        String chatGPTResponseContent = responseByChatGPT(chatHistory);
        if (chatGPTResponseContent == null) {
            YmcPlugin.INSTANCE.getLogger().error("Error");
            return;
        }

        GroupChatHistoryManager.addSystemContent(event.getGroup().getId(), chatGPTResponseContent);

        MessageChain reply = new MessageChainBuilder().append(new QuoteReply(event.getMessage()))
                .append(new At(event.getSender().getId()))
                .append(new PlainText(chatGPTResponseContent))
                .build();

        event.getGroup().sendMessage(reply);
    }


    public GroupMessageEvent getEvent() {
        return event;
    }

}
