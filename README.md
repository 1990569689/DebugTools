# 易调试 - 单片机物联网调试集成 APP

![image](https://img.shields.io/badge/License-MIT-blue.svg)

## 一、项目简介

**易调试** 是一款专为单片机物联网开发者设计的集成调试工具 APP，专注于**蓝牙通信**和**MQTT 协议调试**场景，旨在帮助开发者快速搭建、测试和优化物联网设备与云端的交互流程。本工具支持多平台调试功能集成，兼容主流单片机开发环境，适合物联网爱好者、学生及嵌入式开发工程师使用。

## 二、核心功能

### 1. 蓝牙调试模块

- **双向通信**：支持蓝牙串口（BLE/UART）收发数据，实时显示 hex / 文本格式数据
- **设备管理**：自动扫描附近蓝牙设备，支持自定义设备名称过滤
- **数据记录**：支持通信日志导出为 Execel文件，方便问题追溯
- **指令模板**：预设常用 AT 指令模板，支持用户自定义指令集

### 2. MQTT 调试模块

- **多客户端管理**：支持同时连接多个 MQTT broker（服务器）
- **主题订阅 / 发布**：可视化主题管理界面，支持 JSON/XML 格式数据解析
- **QoS 等级控制**：支持 QoS 0/1/2 等级设置，验证消息可靠性
- **遗嘱消息配置**：可设置设备离线时自动发布的遗嘱消息

### 3. 扩展功能

- **数据可视化**：支持实时绘制传感器数据曲线（如温度、湿度变化）

## 三、技术架构

| 模块        | 技术选型              | 说明                     |
| ----------- | --------------------- | ------------------------ |
| 前端框架    | Android               | Android Studio开发       |
| 蓝牙通信    | FastBle               | 基于原生蓝牙模块封装     |
| MQTT 协议栈 | Eclipse Paho MQTT     | 轻量级 MQTT 客户端实现   |
| 数据存储    | AsyncStorage + SQLite | 本地配置存储与日志持久化 |
| 数据曲线    | MPAndroidChart        | 数据图表集成             |

## 四、安装与运行

### 1. 环境要求

- **系统**：Android 6.0+
- 环境：
  - Gradle 8.0
  - Java Version 17.0.6
  - Android Studio Giraffe 2022.3.1
  - Android Sudio Gradle Plugin 8.1.4

### 2. 编译运行

bash

```bash
# 克隆项目
git clone https://github.com/1990569689/DebugTools.git
```

## 五、相关截图

### 1. 蓝牙调试示例

![5ec99ffe10583124c067976f6b5b313f](D:\WeChatMessage\xwechat_files\wxid_dpuigkmv0gud29_679e\temp\2025-05\RWTemp\410dcb347e7b3958a12d8c0c31728a2d\5ec99ffe10583124c067976f6b5b313f.jpg)

![1c8ed9a2b3ab2c2186cd6cc01f9a1dd6](D:\WeChatMessage\xwechat_files\wxid_dpuigkmv0gud29_679e\temp\2025-05\RWTemp\410dcb347e7b3958a12d8c0c31728a2d\1c8ed9a2b3ab2c2186cd6cc01f9a1dd6.jpg)

### 2. MQTT 订阅示例

![99fd40e04178a46e3f3bcc8832790f1f](D:\WeChatMessage\xwechat_files\wxid_dpuigkmv0gud29_679e\temp\2025-05\RWTemp\410dcb347e7b3958a12d8c0c31728a2d\99fd40e04178a46e3f3bcc8832790f1f.jpg)

![a4fad9e171d5c28b58b0ed7aedaddc31](D:\WeChatMessage\xwechat_files\wxid_dpuigkmv0gud29_679e\temp\2025-05\RWTemp\410dcb347e7b3958a12d8c0c31728a2d\a4fad9e171d5c28b58b0ed7aedaddc31.jpg)

## 六、贡献指南

欢迎各位开发者参与项目贡献！我们鼓励以下形式的贡献：

1. **提交 Issue**：反馈使用问题或提出新功能建议
2. **代码贡献**：提交 PR 修复 bug 或实现新功能（需先创建 Issue 讨论方案）
3. **文档完善**：优化 README 或补充开发文档
4. **测试反馈**：参与 Beta 测试并提供测试报告

```plaintext
fork仓库 -> 创建功能分支 -> 开发测试 -> 提交PR -> 等待代码 review -> 合并至主分支
```

## 七、许可证

本项目采用 **MIT 许可证**，允许商业使用、修改和再发布，但需保留原作者声明和许可证文件。具体条款见 LICENSE。

## 八、联系方式

- **项目主页**：https://github.com/1990569689/DebugTools
- **Issue 追踪**：https://github.com/1990569689/DebugTools/issues
- **邮箱**：1990569689@qq.com
- **微信交流群**：添加管理员微信（微信号：Ddonging-002）备注 "易调试" 入群

如果觉得本项目对你有帮助，欢迎点击右上角 **Star** 支持！🌟
