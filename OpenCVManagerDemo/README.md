学习 OpenCV for android 的使用
本项目要求手机上先安装独立的 OpenCV 管理器apk,若未安装,则请从 `OpenCVManagerAPKs/` 目录中安装对应的apk;

集成过程:
1. 到 [OpenCV官网](https://opencv.org/releases.html) 下载相应版本(本项目使用3.4.0)的Android压缩包并解压(假设解压目录为: `openCvSdkHome/`);
2. 创建基本的 `Android Studio` (以下简称 `AS`) 项目;
3. 通过 `AS` 的 `file - new - import module` 菜单导入 `openCvSdkHome/sdk/java` 目录,得到模块 `openCVLibrary340`;
4. 修改 `openCVLibrary340/build.gradle` 中的相关参数(如 `compileSdkVersion`), 保持与 `app` 模块中的设定一致;
5. 修改 `app/build.gradle` 添加 opencv模块依赖: `compile project(":openCVLibrary340")`
6. `build - rebuild project`