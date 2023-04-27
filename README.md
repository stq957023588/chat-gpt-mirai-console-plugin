# ChatGPT机器人
一个简易的自动回复插件

基于[ mamoe/mirai: 高效率 QQ 机器人支持库](https://github.com/mamoe/mirai) 开发的插件

拥有简易的自动回复功能，代码简单，适合上手mirai插件开发时学习

## 使用方法
请按照mirai中的 [文档](https://docs.mirai.mamoe.net/console/ConfiguringProjects.html) 来确保mirai环境安装完成，然后将release中打包好的插件放入plugins文件夹启动mirai就可以了

## 指令

| 指令名称         | 用法        | 备注                                             |
| ---------------- | ----------- | ------------------------------------------------ |
| 添加群或好友权限 | /cgadd [qq] | 在Q群直接使用cgadd可以将当前群加入权限，好友不行 |

## 配置

```yaml
# OpenAi的ApiKey
apiKey: ''
# 如果需要代理，不需要请忽略
proxy: ''
port: 0
```

