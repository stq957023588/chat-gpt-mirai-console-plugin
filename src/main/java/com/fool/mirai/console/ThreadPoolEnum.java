package com.fool.mirai.console;

import java.util.concurrent.*;

/**
 * @author fool
 * @date 2021/10/12 10:13
 */
public enum ThreadPoolEnum {

    NAMED_THREAD_POOL("CHAT-GPT");

    private final ExecutorService executorService;

    ThreadPoolEnum(String name) {
        executorService = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new NamedThreadFactory(name), new ChatGPTRejectedExecutionHandler());
    }

    public void execute(Runnable command) {
        executorService.execute(command);
    }

    public void shutdown() {
        executorService.shutdown();
    }

}
