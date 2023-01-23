## 已过时警告
由于代码不完善以及未知原因，在为 SMB 贡献代码或是插件时可能会出现各种问题，因此我们推荐您使用官方的 [Mirai Console Loader](https://github.com/iTXTech/Mirai-Console-Loader)  
如果您只想将 SMB 用作聊天用途，请继续阅读。

## Simple-Mirai-Bot
一个基于 Mirai 框架的多协议轻量级机器人。

*在后文中可能会称 Simple-Mirai-Bot 为 SMB*

### 功能
目前它支持的功能有:

- 发送消息 (支持解析 Mirai Code)
- 基础命令系统 (使用 `/help` 查看全部命令)
- 回复消息 (命令: `/reply`)
- 撤回消息 (命令: `/recall`)
- 发送戳一戳 (命令: `/nudge`)
- 发送语音消息 (命令: `/sendAudio`)
- 图片操作 (命令: `/image`)
- 多聊群切换 (命令: `/changeGroup`)
- 私聊 (命令: `/msg`)
- 切换协议(在配置文件中设置)
- 完整 Tab 补全支持

### 扩展

Simple Mirai Bot 有较为完善的扩展系统 <br>
*由于 JVM 的特性，它只能加载插件，不能卸载插件* <br>
以下为官方扩展:

- [SMBServer - 通过网页使用您的 Simple Mirai Bot](https://github.com/XIAYM-gh/SMB-Server)

### 下载 & 更新

如果您 **第一次使用** Simple-Mirai-Bot，请在 [releases](https://github.com/XIAYM-gh/Simple-Mirai-Bot/releases/latest/)
中下载最新的 Simple-Mirai-Bot.7z <br>
如果您想要 **更新** Simple-Mirai-Bot **本体**
，请在 [releases](https://github.com/XIAYM-gh/Simple-Mirai-Bot/releases/latest/) 中下载最新的 Simple-Mirai-Bot.jar 并 **
覆盖** 在 SMB 工作目录.

### 启动

此项目基于 Java 17 编写，使用了许多 Java 17 的新特性，所以 *请务必使用 **Java 17 或更高版本*** 启动 SMB <br>
解压后在 Shell / CMD 中输入命令启动 Simple-Mirai-Bot.

在 `Windows` 下:
```batch
java -cp mirai-core-all-2.13.2-all.jar;Simple-Mirai-Bot.jar cn.xiaym.simplemiraibot.BotMain
```

在 `Linux / Unix / MacOS` 系统下:
```shell
java -cp mirai-core-all-2.13.2-all.jar:Simple-Mirai-Bot.jar cn.xiaym.simplemiraibot.BotMain
```

*Enjoy it! xwx*

### 协议
此项目继承 Mirai 的 AGPLv3 协议，根据此协议，您对于 SMB 的任何修改或引用都必须开放源代码. <br>
**此项目不鼓励被商业使用，仅供个人娱乐学习所用，如对您的资产或设备造成的任何损失，本项目开发者不承担责任。** <br>
**如使用此项目，则代表您已经阅读并充分理解协议内容。**
