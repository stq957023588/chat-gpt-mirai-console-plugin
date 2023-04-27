package com.fool.mirai.console

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    val enabledGroups: MutableSet<Long> by value()

    val enableFriends: MutableSet<Long> by value()
}