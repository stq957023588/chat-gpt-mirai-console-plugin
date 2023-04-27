package com.fool.mirai.console;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

public class ChatGPTRemoveCommand extends JRawCommand {

    public static final ChatGPTRemoveCommand INSTANCE = new ChatGPTRemoveCommand();

    public ChatGPTRemoveCommand() {
        super(YmcPlugin.INSTANCE, "cgremove");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        if (args.size() == 0 && sender instanceof MemberCommandSenderOnMessage) {
            long group = sender.getSubject().getId();
            Config.INSTANCE.getEnabledGroups().remove(group);
            sender.sendMessage(new PlainText("已关闭ChatGPT"));
            return;
        }

        MessageContent messageContent = args.get(PlainText.Key);
        if (messageContent == null) {
            sender.sendMessage(new PlainText("指令错误！cgremove [qq]"));
            return;
        }
        String s = messageContent.contentToString();
        if (!s.matches("[0-9]{5,11}")) {
            sender.sendMessage(new PlainText("指令错误！cgremove [qq]"));
            return;
        }
        long qq = Long.parseLong(s);
        Config.INSTANCE.getEnabledGroups().remove(qq);
        Config.INSTANCE.getEnableFriends().remove(qq);
        sender.sendMessage(new PlainText("已关闭ChatGPT"));
    }
}
