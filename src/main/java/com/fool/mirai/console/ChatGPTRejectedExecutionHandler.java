package com.fool.mirai.console;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class ChatGPTRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof GroupChatGPTRunnableImpl) {
            GroupChatGPTRunnableImpl runnable = (GroupChatGPTRunnableImpl) r;
            replyGroupMember(runnable);
        }
        if (r instanceof FriendChatGPTRunnableImpl) {
            FriendChatGPTRunnableImpl runnable = (FriendChatGPTRunnableImpl) r;
            replyFriend(runnable);
        }
    }

    private void replyGroupMember(GroupChatGPTRunnableImpl runnable) {
        MessageChain messages = new MessageChainBuilder().append(new QuoteReply(runnable.getEvent().getMessage()))
                .append("系统繁忙，请稍后再试。")
                .build();

        runnable.getEvent().getSubject().sendMessage(messages);
    }

    private void replyFriend(FriendChatGPTRunnableImpl runnable) {
        MessageChain messages = new MessageChainBuilder()
                .append("系统繁忙，请稍后再试。")
                .build();

        runnable.getEvent().getSubject().sendMessage(messages);
    }

}
