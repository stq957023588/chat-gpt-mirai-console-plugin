package com.fool.mirai.console;

import com.fool.mirai.console.openai.*;

import java.util.List;

public abstract class ChatGPTRunnable implements Runnable {


    public String responseByChatGPT(List<Message> chatHistory) {
        ChatCompletionParameters chatCompletionParameters = new ChatCompletionParameters();
        chatCompletionParameters.setMessages(chatHistory);
        chatCompletionParameters.setModel(ModelConstant.GPT_3_5_TURBO);
        chatCompletionParameters.setTemperature(0.7f);
        ChatCompletionResponse chatCompletionResponse = OpenAi.chatCompletion(chatCompletionParameters);
        if (chatCompletionResponse == null) {
            return null;
        }

        List<Choice> choices = chatCompletionResponse.getChoices();
        StringBuilder openAiResponseContent = new StringBuilder();

        for (Choice choice : choices) {
            openAiResponseContent.append(choice.getMessage().getContent())
                    .append("\n");
        }
        return openAiResponseContent.toString();
    }

}
