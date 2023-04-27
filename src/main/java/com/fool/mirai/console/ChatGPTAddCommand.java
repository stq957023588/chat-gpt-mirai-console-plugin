package com.fool.mirai.console;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ChatGPTAddCommand extends JRawCommand {

    public static final ChatGPTAddCommand INSTANCE = new ChatGPTAddCommand();

    public ChatGPTAddCommand() {
        super(YmcPlugin.INSTANCE, "cgadd");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        if (args.size() == 0 && sender instanceof MemberCommandSenderOnMessage) {
            long group = sender.getSubject().getId();
            Config.INSTANCE.getEnabledGroups().add(group);
            sender.sendMessage(new PlainText("已开启ChatGPT"));
            return;
        }

        MessageContent messageContent = args.get(PlainText.Key);
        if (messageContent == null) {
            sender.sendMessage(new PlainText("指令错误！cgadd [qq]"));
            return;
        }
        String s = messageContent.contentToString();
        if (!s.matches("[0-9]{5,11}")) {
            sender.sendMessage(new PlainText("指令错误！cgadd [qq]"));
            return;
        }
        long qq = Long.parseLong(s);
        if (Optional.ofNullable(sender.getBot()).map(e -> e.getGroup(qq)).isPresent()) {
            Config.INSTANCE.getEnabledGroups().add(qq);
            sender.sendMessage(new PlainText(String.format("群：%s已开启ChatGPT", qq)));
        }

        if (Optional.ofNullable(sender.getBot()).map(e -> e.getFriend(qq)).isPresent()) {
            Config.INSTANCE.getEnabledGroups().add(qq);
            sender.sendMessage(new PlainText(String.format("好友：%s已开启ChatGPT", qq)));
        }
    }
}
