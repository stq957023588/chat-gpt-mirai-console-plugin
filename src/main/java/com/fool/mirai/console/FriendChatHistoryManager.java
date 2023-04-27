package com.fool.mirai.console;

import com.fool.mirai.console.openai.Message;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendChatHistoryManager {

    private static final String ROLE_USER = "user";

    private static final String ROLE_SYSTEM = "system";

    private static final String ROLE_ASSISTANT = "assistant";

    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Message>> CHAT_HISTORY = new ConcurrentHashMap<>();

    public static List<Message> getCharHistory(long group) {
        return CHAT_HISTORY.get(group);
    }

    public static void addUserContent(long group, long user, String content) {
        Message message = new Message();
        message.setRole(ROLE_USER);
        message.setContent(content);

        addContent(group, message);
    }

    public static void addSystemContent(long group, String content) {
        Message message = new Message();
        message.setRole(ROLE_ASSISTANT);
        message.setContent(content);

        addContent(group, message);
    }

    private static void addContent(long group, Message message) {
        CopyOnWriteArrayList<Message> messages = CHAT_HISTORY.get(group);
        if (messages == null) {
            messages = new CopyOnWriteArrayList<>();
        }

        messages.add(message);
        CHAT_HISTORY.put(group, messages);
    }


}
