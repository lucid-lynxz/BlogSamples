> 在学习 `Binder` 机制, 需要 `AIDL` 进行 `IPC`;
> 我工作中几乎没用过 `AIDL` ,趁此机会复习一下, 留个demo好了 ╰(￣▽￣)╭
> IDE: Android Studio 3.0+

[AIDL官方文档](https://developer.android.com/guide/components/aidl.html)

本程序创建的是server端, [参考文章](http://blog.csdn.net/qian520ao/article/details/78072250)

## 1. server 端创建
1. 新建项目 `AidlServerDemo`
2. 在 `app/src/main/` 下创建目录 `aidl` (可以右键 `main` 目录, 选择 `new - folder - AIDL folder`);
3. 在 `app/build.gradle` 中添加源码目录(不做也可以):
    ```gradle
    // app/build.gradle
    android{
        sourceSets {
            main {
                java.srcDirs = ['src/main/java', 'src/main/aidl']
            }
        }
    }
    ```
4. 在 `app/src/main/aidl/` 目录下创建Parcelable类 `MessageBean` ,实现对应的方法;
5. 在 `app/src/main/aidl/` 目录下创建文件 `IDemandManager.aidl` (右键 `aidl/` 目录, 选择 `new - AIDL - AIDL file` , 文件内容会自动生成,可删除,并添加自己需要的方法);
    > 方法参数可能需要添加表明数据走向的方向标记: in out inout
6. 为了client可以调用manager的功能, 需要创建一个 service:
    * 在 `app/src/main/aidl/` 中创建 `DemandService.kt`, 在 `onBind()` 中返回 `IDemandManager.Stub` 实例;
    * 在 `AndroidManifest.xml` 中注册该service,并按需指定action等过滤信息,如:
        ```xml
        <service
            android:name=".DemandService"
            android:exported="true">
            <intent-filter>
                <action android:name="org.lynxz.aidl"/>
            </intent-filter>
        </service>
        ```
7. 选择菜单 `Build - Rebuild project`, 就会发现生成了一个同名java文件: `app/build/generated/source/aidl/debug/{packageName}/IDemandManager.java` 

## 2. client 端调用
1. 新建项目 `AidlClientDemo`
2. 将刚才server端项目中的 `aidl` 目录复制到 client项目的 `app/src/main` 目录(`DemandService.kt` 不需要,也不用在manifest中注册)中
3. rebuild一下项目
4. 隐式使用service:
    ```kotlin
   //MainActivity.kt
   fun bindService(){
        val intent = Intent()
        intent.action = "org.lynxz.aidl" //service的action
        intent.`package` = "org.lynxz.aidldemo" //aidl文件夹里面aidl文件的包名
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private var demandManager: IDemandManager? = null
 
    val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            demandManager?.unregisterListener(listener)
            demandManager = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // 获取该对象后即可进行通讯
            Log.d("clientAidl", "onServiceConnected $name")
            demandManager = IDemandManager.Stub.asInterface(service)
            demandManager?.registerListener(listener)
        }
    }
    ```
5. 发送消息给server端:
    ```kotlin
    demandManager?.setDemandIn(MessageBean("client: $input", 38))
    ```
    

## 2. 实现AIDL接口的原则
* 将服务正确地编译为线程安全服务
* 默认情况下，RPC 调用是同步调用,通常应该从客户端内的单独线程调用服务
* 任何异常(除了 `DeadObjectException` )都不会回传给调用方