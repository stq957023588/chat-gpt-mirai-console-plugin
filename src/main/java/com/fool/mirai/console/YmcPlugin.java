package com.fool.mirai.console;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

public class YmcPlugin extends JavaPlugin {

    public static final YmcPlugin INSTANCE = new YmcPlugin();

    public YmcPlugin() {
        super(new JvmPluginDescriptionBuilder("com.fool.ymc", "1.0")
                .name("YMC")
                .author("Fool")
                .build());
        System.getProperties().setProperty("file.encoding", "utf-8");
    }

    @Override
    public void onEnable() {
        // 加载配置文件
        reloadPluginConfig(Config.INSTANCE);
        // 加载插件数据
        reloadPluginData(Data.INSTANCE);

        getLogger().info("YMCDLNSLPSFT");
        // 注册命令
        this.registerCommand();

        // 订阅群消息
        GlobalEventChannel.INSTANCE.parentScope(INSTANCE)
                // 过滤非群消息
                .filter(e -> e instanceof GroupMessageEvent)
                // 过滤无权限群组
                .filter(e -> (Config.INSTANCE.getEnabledGroups().contains(((GroupMessageEvent) e).getSubject().getId())))
                .subscribeAlways(GroupMessageEvent.class, event -> {
                    MessageChain messageChain = event.getMessage();
                    // 判断是否有@机器人
                    boolean atBot = false;
                    for (SingleMessage message : messageChain) {
                        if (message instanceof At) {
                            At atMessage = (At) message;
                            long target = atMessage.getTarget();
                            atBot = atBot || event.getBot().getId() == target;
                        }
                    }
                    if (atBot) {
                        GroupChatGPTRunnableImpl runnable = new GroupChatGPTRunnableImpl(event);
                        ThreadPoolEnum.NAMED_THREAD_POOL.execute(runnable);
                    }
                });

        // 订阅好友消息
        GlobalEventChannel.INSTANCE.parentScope(INSTANCE)
                // 过滤非好友消息
                .filter(e -> e instanceof FriendMessageEvent)
                // 过滤无权限好友
                .filter(e -> (Config.INSTANCE.getEnableFriends().contains(((FriendMessageEvent) e).getSubject().getId())))
                .subscribeAlways(FriendMessageEvent.class, event -> {
                    // 使用线程池异步执行
                    FriendChatGPTRunnableImpl runnable = new FriendChatGPTRunnableImpl(event);
                    ThreadPoolEnum.NAMED_THREAD_POOL.execute(runnable);
                });

    }

    @Override
    public void onDisable() {
        unregisterCommand();
    }

    private void registerCommand() {
        CommandManager.INSTANCE.registerCommand(ChatGPTAddCommand.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(ChatGPTRemoveCommand.INSTANCE, true);
    }

    private void unregisterCommand() {
        CommandManager.INSTANCE.unregisterCommand(ChatGPTAddCommand.INSTANCE);
        CommandManager.INSTANCE.unregisterCommand(ChatGPTRemoveCommand.INSTANCE);
    }

}
