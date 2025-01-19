# CatSpy

[English](README.md) | 简体中文

[![Build status](https://github.com/Gegenbauer/CatSpy/workflows/Build%20Artifacts/badge.svg)](https://github.com/Gegenbauer/CatSpy/actions/workflows/build_artifacts.yml?query=workflow%3ABuild)
![GitHub contributors](https://img.shields.io/github/contributors/Gegenbauer/CatSpy)
![GitHub all releases](https://img.shields.io/github/downloads/Gegenbauer/CatSpy/total)
![GitHub release (latest by SemVer)](https://img.shields.io/github/downloads/Gegenbauer/CatSpy/latest/total)
[![Latest release](https://img.shields.io/github/release/Gegenbauer/CatSpy.svg)](https://github.com/Gegenbauer/CatSpy/releases/latest)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

**CatSpy** - 一个用于浏览日志的工具。

**主要特性：**
- 针对日志的各个部分进行过滤，例如 message，tag，pid，tid，log level 等
- 可自定义日志类型，自定义日志解析器，自定义日志配色方案
- 导入本地文件日志和查看已连接 Android 设备实时日志
- 随输入内容变化的实时过滤
- 多标签页，同时查看多个日志
- 记录最近查看的文件日志，方便查看
- 日志分页显示，提高渲染速度
- 可以为日志行添加书签，方便查看和过滤
- 接入 FlatLaf，提供多种主题供切换
- 支持多语言（韩语、简体中文、英语）

## 相对于其他 Android 日志查看工具的优势
| 优势                     | CatSpy                                                       | Android Studio                               | 文本阅读器（Notepad++, sublime, fleet） |
| ------------------------ | ------------------------------------------------------------ |----------------------------------------------| --------------------------------------- |
| 内存占用                 | 相比 Android Studio 更轻量，占用内存更小，但由于 JVM 的特性，相对于一些纯粹的文本阅读器，占用内存还是更大的 | \                                            | \                                       |
| 过滤方式                 | 支持 message，tag，pid，tid，log level 等单独过滤搜索。文本类过滤器支持正向匹配和反向匹配，支持条件以或的形式叠加 | 特有的过滤语法，创建过滤器的自由度更高                          | 只支持文本匹配搜索                      |
| 是否支持读取设备实时日志 | 支持读取实时设备日志，并支持包名过滤                         | 支持                                           | \                                       |
| 实时日志有效期           | 抓取到的实时日志只要不手动清空会一直保留，随时可以对抓取到的所有日志进行过滤 | 实时日志经常在切换设备后消失，或者由于其他不知名原因消失，经常导致需要重复复现来抓取日志 | \                                       |
| 实时日志是否支持导出     | 可导出实时日志，且支持导出的日志大小取决于运行该软件的设备内存。 | 可导出日志，但导出的文件只能通过导入到 Android Studio 查看，可读性较低  | \                                       |
| 导入文件日志             | 支持导入，而且文件日志大小的限制也取决于运行该软件的设备内存。 | 可导入文件日志，只支持导入通过 Android Studio 导出的实时日志文件     | 可导入文件日志                          |

## 界面
### 主页
![home_page.png](pic%2Fhome_page.png)

### 文件日志页面
![file_log_main_interface.png](pic%2Ffile_log_main_interface.png)

### 设备日志页面
![device_log_main_interface.png](pic%2Fdevice_log_main_interface.png)

### 日志定制面板
![log_customization_dialog.png](pic%2Flog_customization_dialog.png)

### 主题设置
![theme_configuration.png](pic%2Ftheme_configuration.png)

## 下载
最新版本: [![Latest release](https://img.shields.io/github/release/Gegenbauer/CatSpy.svg)](https://github.com/Gegenbauer/CatSpy/releases/latest)

## 启动应用
### 使用安装包安装后打开
#### macOS
在 macOS 上，由于应用未签名，可能会提示无法打开，此时按如下操作打开

1. 打开 dmg 文件，将应用拖到应用程序文件夹
2. launchpad 中找到应用，打开应用
3. 关闭弹出的警告框
  
  ![macOS_open_warning.png](pic%2FmacOS_open_warning.png)

4. 打开系统设置 -> 隐私与安全 -> 找到安全 -> 点击打开
  
  ![macOS_grant_open_permission.png](pic%2FmacOS_grant_open_permission.png)

5. 再次打开应用

#### Linux
安装好 deb 后，可以在应用菜单中找到应用图标，点击打开应用

#### Windows
安装好 msi 后，可以在应用菜单中找到应用图标，点击打开应用
但需要注意，安装目录最好不要放在 C 盘，因为 C 盘有可能会有权限问题

### 使用 jar 包运行
```bash
java -jar CatSpy-${version}.jar
```

## 使用
### 加载文件日志
在主页点击“打开文件”，选择日志文件后会打开文件日志页并加载选择的日志文件。
也可以通过直接将文件拖入首页或者文件日志页来进行加载。

![operation_open_file](pic%2Foperation_open_file.png)
### 文件日志切换解析器
在文件日志页，右上角下拉框，可以切换解析器，默认为`DefaultRawLog`，它将每行日志作为一个整体，过滤时也只能对整行内容进行匹配。
如果查看标准`Android Logcat`日志，请切换到`StandardLogcatFileLog`，它将日志划分为`time`，`message`，`tag`，`pid`，`tid`，`log level`，并且可以对每个部分单独进行过滤。
你也可以前往 菜单->设置->日志定制 创建自己的日志解析器（目前自定义解析器的功能还比较简陋，后面可能会做成插件形式）。

![operation_choose_log_metadata](pic%2Foperation_choose_log_metadata.png)
### 读取设备日志
在首页点击“打开 Android 设备日志面板”，会打开设备日志页，默认会查找 adb 路径，如果没有找到 adb 路径，会提示设置 adb 路径。

![warning_configure_adb_path](pic%2Fwarning_configure_adb_path.png)
点击`设置`按钮，选择 adb 路径，点击`启动 ADB 服务器`按钮，成功启动 adb 服务后，点击保存，下次打开设备日志页，如果 adb 服务未启动则会自动启动。

![operation_configure_adb_path](pic%2Foperation_configure_adb_path.png)
成功启动 Adb 服务后，下拉框会展示已连接的设备，选择设备后，点击“启动”按钮，会读取设备 logcat 日志。

![operation_device_list](pic%2Foperation_device_list.png)
### 过滤器标签
过滤器组内容不为空时，点击“存储”按钮，输入标签名，点击“确定”按钮，即可存储过滤器组。
点击存储的标签，则可将其应用为当前过滤器组。

![operation_save_filter](pic%2Foperation_save_filter.png)
### 日志过滤
过滤器有内容过滤器，级别过滤器，和匹配大小写过滤器。
可启用过滤器和禁用过滤器，禁用的过滤器不生效。
内容过滤器对相应的列进行过滤，支持正则匹配，单个过滤器支持多个条件，条件之间是或的关系，不同过滤器之间是与的关系。支持正向匹配和反向匹配。
![operation_filter](pic%2Foperation_filter.png)

过滤器的大小和位置可以调整，在 菜单->设置->日志定制->对应解析器->过滤器 中进行设置。

![operation_configure_filter_ui](pic%2Foperation_configure_filter_ui.png)

### 查看选中日志的完整内容
日志以表格的形式展示，如果内容太长可能会显示不下，可以双击对应日志行，或者选中一些日志后按下 `ENTER` 键，就会打开日志详情弹框。
打开日志详情弹框后，可以通过按下 ESC 键来关闭。

![operation_open_log_detail_dialog](pic%2Foperation_open_log_detail_dialog.png)

### 为日志添加书签
选中一些日志后按下 `Ctrl + B`（或者右键菜单项），即可为选中的日志添加书签，添加书签后，日志行的背景颜色会发生变化。
可以在日志表格上方勾选“书签”，则日志过滤面板只展示添加过书签的日志，此时过滤器将会失效。
对于添加了书签的日志，可以按下 `DELETE`（或者右键菜单项）取消书签。

![operation_log_bookmark](pic%2Foperation_log_bookmark.png)

### 日志搜索
按下 `Ctrl + F` 打开搜索面板，输入搜索内容，点击上一项和下一项，即可查找到对应的日志。
日志搜索支持正则匹配，支持条件以或的形式叠加，支持正向匹配和反向匹配。

![operation_search_log](pic%2Foperation_search_log.png)

### 常用快捷键
#### 日志搜索
- `Ctrl + F`: 打开搜索面板，如果搜索面板已打开，则搜索编辑器获取焦点
- `Esc`: 如果搜索面板已打开且获取焦点，则关闭搜索面板
- `Enter`: 如果搜索面板已打开且获取焦点，则搜索下一个匹配项
- `Shift + Enter`: 如果搜索面板已打开且获取焦点，则搜索上一个匹配项
- `Ctrl + G`: 打开跳转到指定行号弹框，跳转到第一个行号大于等于输入行号的日志
#### 日志书签
- `Ctrl + B`: 为选中的日志添加书签
- `Delete`: 取消选中的日志的书签
## 构建和运行
### 环境
- JDK17 及以上
```bash
git clone git@github.com:Gegenbauer/CatSpy.git
cd CatSpy
./gradlew :app:packageDeb # 构建 deb 包
./gradlew :app:packageMsi # 构建 msi 包
./gradlew :app:packageDmg # 构建 dmg 包
./gradlew :app:run #运行
```

## 支持
项目最初基于[lognote](https://github.com/cdcsgit/lognote)，对其进行了完全重构，对日志加载和渲染流程进行了优化，
功能上增加了构建各平台产物的能力，增加了软件更新功能，增加了日志分页显示功能。
项目结构上根据层级划分为了多个模块，并增加了缓存、上下文、服务、数据绑定，日志等模块

另外还参考了以下项目

[darklaf](https://github.com/weisJ/darklaf) 

[jadx](https://github.com/skylot/jadx)

![JetBrains Logo (Main) logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)
