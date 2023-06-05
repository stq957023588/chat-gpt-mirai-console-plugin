package com.fool.mirai.console

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Data : AutoSavePluginData("data") {

    val api: String by value("https://api.openai.com/v1/chat/completions")

    val apiKey: String by value()

    val proxy: String by value()

    val port: Int by value()
}