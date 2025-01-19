# CatSpy

English | [简体中文](README_CN.md)

[![Build status](https://github.com/Gegenbauer/CatSpy/workflows/Build%20Artifacts/badge.svg)](https://github.com/Gegenbauer/CatSpy/actions/workflows/build_artifacts.yml?query=workflow%3ABuild)
![GitHub contributors](https://img.shields.io/github/contributors/Gegenbauer/CatSpy)
![GitHub all releases](https://img.shields.io/github/downloads/Gegenbauer/CatSpy/total)
![GitHub release (latest by SemVer)](https://img.shields.io/github/downloads/Gegenbauer/CatSpy/latest/total)
[![Latest release](https://img.shields.io/github/release/Gegenbauer/CatSpy.svg)](https://github.com/Gegenbauer/CatSpy/releases/latest)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

**CatSpy** - A tool designed for log browsing.

**Key Features:**
- Filter various parts of the log, such as message, tag, pid, tid, log level, etc.
- Customize log types, log parsers, and log color schemes
- Import local file logs and view real-time logs from connected Android devices
- Real-time filtering as you type
- Multiple tabs to view multiple logs simultaneously
- Record recently viewed file logs for easy access
- Paginated log display to improve rendering speed
- Add bookmarks to log lines for easy viewing and filtering
- Integrate with FlatLaf to provide multiple themes for switching
- Support multiple languages (Korean, Simplified Chinese, English)

## Advantages Compared to Other Android Log Viewing Tools
| Advantage                              | CatSpy                                                       | Android Studio                               | Text Readers (Notepad++, sublime, fleet) |
|----------------------------------------| ------------------------------------------------------------ |----------------------------------------------| --------------------------------------- |
| Memory Usage                           | Lighter than Android Studio, consuming less memory. However, due to JVM characteristics, it may still consume more memory compared to some pure text readers. | \                                            | \                                       |
| Filtering Method                       | Supports individual filtering for message, tag, pid, tid, log level, etc. Text filters support both forward and reverse matching, with conditions combinable using OR logic. | Unique filtering syntax in Android Studio provides greater flexibility in filter creation.                       | Only supports text matching search                      |
| Real-time Device Log Reading Support   | Enables real-time device log reading and supports package name filtering.                         | Supported                                           | \                                       |
| The validity period of Real-time logs. | Captured real-time logs persist until manually cleared, allowing filtering at any time. | Real-time logs often disappear after device switching or due to other unknown reasons, often requiring log reproduction. | \                                       |
| Real-time Log Export                   | Allows exporting real-time logs, with exported file size dependent on the device's memory. | Logs can be exported, but the exported files can only be viewed by importing into Android Studio, resulting in reduced readability.  | \                                       |
| File Log Import                        | Supports importing file logs, with size limitations based on the device's memory. | File logs can be imported, but only those exported from Android Studio in real-time are supported.                            | File logs can be imported                          |

## Interface
### Home Page
![home_page.png](pic%2Fhome_page.png)

### File Log Page
![file_log_main_interface.png](pic%2Ffile_log_main_interface.png)

### Device Log Page
![device_log_main_interface.png](pic%2Fdevice_log_main_interface.png)


### Log Customization Panel
![log_customization_dialog.png](pic%2Flog_customization_dialog.png)

### Theme Settings
![theme_configuration.png](pic%2Ftheme_configuration.png)

## Download
Latest Release: [![Latest release](https://img.shields.io/github/release/Gegenbauer/CatSpy.svg)](https://github.com/Gegenbauer/CatSpy/releases/latest)

## Launching the Application
### Running After Installation from Packages
#### macOS
On macOS, due to the application being unsigned, you may encounter an error indicating it cannot be opened. In such cases, follow these steps to open it:

1. Open the dmg file and drag the application to the Applications folder.
2. Find the application in the launchpad and open it.
3. Dismiss the warning box that appears.
   
   ![macOS_open_warning.png](pic%2FmacOS_open_warning.png)

4. Navigate to System Preferences -> Privacy & Security -> Security and click `Open`.
   
   ![macOS_grant_open_permission.png](pic%2FmacOS_grant_open_permission.png)

5. Attempt to open the application again.

#### Linux
After installing the .deb package, you can find the application icon in the application menu. Click on it to open the application.

#### Windows
After installing the .msi package, you can find the application icon in the application menu. Click on it to open the application. 

However, please note that it's advisable not to install the application in the C drive as it may encounter permission issues.

### Running with JAR File
```bash
java -jar CatSpy-${version}.jar
```
## Usage
### Load File Log
On the homepage, click `Open File`, select the log file, and it will open the file log page and load the selected log file.  
You can also load files by directly dragging the file into the homepage or file log page.

![operation_open_file](pic%2Foperation_open_file.png)

### Switch Log Parser for File Logs
On the file log page, you can switch parsers using the drop-down menu in the upper right corner. The default parser is `DefaultRawLog`, which treats each line of the log as a whole, and filtering can only be done on the entire line.  
If you are viewing a standard Android Logcat log, switch to `StandardLogcatFileLog`, which divides the log into `time`, `message`, `tag`, `pid`, `tid`, and `log level`, allowing you to filter each part individually.  
You can also go to `Menu -> Settings -> Log Customization` to create your own log parser(although the current custom parser functionality is still relatively simple, it may be made into a plugin in the future).

![operation_choose_log_metadata](pic%2Foperation_choose_log_metadata.png)

### Read Device Logs
On the homepage, click `Open Android Device Log Panel` to open the device log page. By default, it will search for the `adb` path. If the `adb` path is not found, a prompt will ask you to set it.

![warning_configure_adb_path](pic%2Fwarning_configure_adb_path.png)

Click the `Set` button, choose the `adb` path, and click the `Start ADB Server` button. After successfully starting the Adb service, click Save. The next time you open the device log page, if the Adb service is not running, it will start automatically.

![operation_configure_adb_path](pic%2Foperation_configure_adb_path.png)

After successfully starting the Adb service, the drop-down menu will display connected devices. Select a device and click the `Start` button to read the device's logcat log.

![operation_device_list](pic%2Foperation_device_list.png)

### Filter Tags
When the filter group contains content, click the `Save` button, enter the tag name, and click the `Confirm` button to save the filter group.  
Click the saved tag to apply it as the current filter group.

![operation_save_filter](pic%2Foperation_save_filter.png)

### Log Filtering
There are content filters, level filters, and case sensitivity filters.  
Filters can be enabled or disabled; disabled filters will not take effect.  
Content filters apply to corresponding columns and support regex matching. A single filter supports multiple conditions, which are in an OR relationship. Different filters are in an AND relationship. Both positive and negative matching are supported.

![operation_filter](pic%2Foperation_filter.png)

The size and position of filters can be adjusted in `Menu -> Settings -> Log Customization -> Corresponding Parser -> Filter`.

![operation_configure_filter_ui](pic%2Foperation_configure_filter_ui.png)

### View Full Content of Selected Logs
The logs are displayed in a table format. If the content is too long, it may not fit in the table. You can double-click on the corresponding log line or 
select some logs and press `ENTER` to open the log detail dialog.
After opening the log detail dialog, you can press `ESC` to close it.

![operation_open_log_detail_dialog](pic%2Foperation_open_log_detail_dialog.png)

### Add Bookmarks to Logs
Double-click on the corresponding log line or select some logs and press `Ctrl + B` (or right-click the menu item) to add a bookmark to the selected logs. 
After adding a bookmark, the background color of the log line will change.
You can check the `Bookmark` filter above the log table to only display logs with bookmarks. In this case, the filter will be disabled.
For logs with bookmarks, you can press `DELETE` (or right-click the menu item) to remove the bookmark.

![operation_log_bookmark](pic%2Foperation_log_bookmark.png)

### Log Search
Press `Ctrl + F` to open the search panel, enter the search content, and click `Previous` or `Next` to locate the corresponding log.  
Log search supports regex matching, condition stacking in OR form, and both positive and negative matching.

![operation_search_log](pic%2Foperation_search_log.png)

### Common Shortcuts
#### Log Search
- `Ctrl + F`: Open the search panel, or focus the search editor if already open
- `Esc`: If the search panel is open and focused, close the search panel
- `Enter`: If the search panel is open and focused, search for the next match
- `Shift + Enter`: If the search panel is open and focused, search for the previous match
- `Ctrl + G`: Open the `Go to Line` dialog and jump to the first log entry with a line number greater than or equal to the input

#### Log Bookmarks
- `Ctrl + B`: Add a bookmark to the selected logs
- `Delete`: Remove the bookmark from the selected logs

## Build and Run
### Environment
- JDK17 or higher
```bash
git clone git@github.com:Gegenbauer/CatSpy.git
cd CatSpy
./gradlew :app:packageDeb # Build deb artifact
./gradlew :app:packageMsi # Build msi artifact
./gradlew :app:packageDmg # Build dmg artifact
./gradlew :app:run # Run
```

## Support
The project was initially based on [lognote](https://github.com/cdcsgit/lognote) and underwent complete refactoring, with optimizations made to the log loading and rendering processes. It introduced the ability to build artifacts for various platforms, software update functionality, and paginated log display. The project structure is divided into multiple modules according to hierarchy, including cache, context, service, data binding, and logs modules.

Additionally, the project referenced the following:

[darklaf](https://github.com/weisJ/darklaf)

[jadx](https://github.com/skylot/jadx)

![JetBrains Logo (Main) logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)