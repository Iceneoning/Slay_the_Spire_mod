# 甘雨&椿MOD - Slay the Spire 角色扩展

## 项目简介

这是一个为《杀戮尖塔》制作的MOD，添加了来自热门游戏《原神》的角色——甘雨(Gan Yu)，《鸣潮》的角色——椿（camellya）作为可玩角色。

## 特色内容

### 角色已实现功能
- ✅ 角色基础设定
  - 独立的角色美术资源
  - 自定义角色参数（生命值、能量等）
  - 中文本地化支持
- ✅ 基础卡牌系统
  - 起始卡牌"打击"
  - 卡牌升级机制
  - 完整的本地化文本
- ✅ 游戏集成
  - 角色选择界面
  - 卡牌颜色主题定制
  - 能量球和UI元素

### 角色参数
- 初始生命值：80
- 起始能量：3
- 每回合抽牌数：5
- 起始金币：99
- 高阶生命损失：5

## 核心架构
- 基础框架: 基于 BaseMod 框架开发
- 游戏集成: 使用 ModTheSpire 进行游戏集成
- 监听模型: 采用订阅/取消订阅机制监听游戏事件

## 接口说明

本MOD基于BaseMod框架开发，完全使用游戏本身提供的接口和扩展点，未创建任何自定义接口。通过BaseMod提供的标准接口实现了角色、卡牌等核心功能的集成。

**补充**：接口对应着游戏中的各种钩子。模组通过实现这些接口，并将自身订阅到 BaseMod，从而将代码"挂"到这些钩子上，实现对游戏事件的监听和响应。

# 用于向游戏中添加新内容的核心接口：

- addCard: 添加新的卡牌
- addRelic: 添加新的遗物
- addPotion: 添加新的药水
- addEvent: 添加新的事件
- addCharacter: 添加新的可玩角色

# 钩子系统 (Hook System)

# 卡牌相关钩子

- PostDrawSubscriber: 监听卡牌被抽取事件
- PostExhaustSubscriber: 监听卡牌被消耗事件
- OnCardUseSubscriber: 监听卡牌使用事件

# 能量相关钩子

- PostEnergyRechargeSubscriber: 监听玩家能量回复事件

# 游戏流程钩子

- PostInitializeSubscriber: 监听游戏初始化完成事件
- StartGameSubscriber: 监听游戏开始事件
- PostBattleSubscriber: 监听战斗结束事件
- PostDungeonInitializeSubscriber: 监听地牢初始化事件

# 渲染相关钩子

- PreRenderSubscriber: 在游戏渲染前执行自定义绘制
- PostRenderSubscriber: 在游戏渲染后执行自定义绘制

# 玩家与怪物相关钩子

- PrePlayerUpdateSubscriber: 玩家状态更新前监听
- PostPlayerUpdateSubscriber: 玩家状态更新后监听
- MaxHPChangeSubscriber: 监听最大生命值变化事件

## 开发团队

本MOD由五人团队开发：
- ganyudedog
- Zicheng Lin
- Songjie Guan
- Binyang Ye
- Iceneoning

## 安装说明

1. 确保已安装《杀戮尖塔》和ModTheSpire
2. 下载最新版本的MOD文件
3. 将jar文件放入游戏目录下的mods文件夹
4. 通过ModTheSpire启用本MOD

## 当前版本

v0.0.1 - 初步开发版本

## 开发计划

- [ ] 添加更多甘雨主题卡牌
- [ ] 设计专属遗物系统
- [ ] 完善角色动画和特效
- [ ] 平衡性调整和测试
- [ ] 更多本地化语言支持

## 技术架构

- 基于BaseMod框架开发
- 使用ModTheSpire进行游戏集成
- 支持简体中文本地化
- Maven项目结构管理

## 注意事项

本MOD目前仍在开发阶段，功能有限。我们欢迎任何反馈和建议，以帮助我们改进游戏体验。

## 版权声明

本MOD为免费的同人创作，不用于商业用途。《杀戮尖塔》为Mega Crit Games所有，《原神》为miHoYo所有。《鸣潮》为库洛所有。本MOD仅用于学习和娱乐目的。
