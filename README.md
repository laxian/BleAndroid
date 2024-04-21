# rust 蓝牙扫描kotlin jni demo

## 前置步骤

- 将编译好的librust_lib和libbtleplug.so复制到jniLibs/对应架构目录下
- 配置AndroidManifest.xml，蓝牙权限
- 声明kotlin端jni方法，调用前调用加载静态库方法

```KOTLIN
class RustBridge {
    // 加载动态库
    companion object {
        init {
            System.loadLibrary("btleplug")
            System.loadLibrary("rust_lib")
        }
    }

    // 声明外部函数，与 Rust 中的函数对应
    // 先调用initWrapper
    external fun initWrapper(): Int
    // 再调用scanWrapper，会阻塞调用线程， 需要另起线程调用；回调线程不在主线程
    external fun scanWrapper(filter: Array<kotlin.String>, callback: DeviceCallback)
}
```




