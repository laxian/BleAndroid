package com.example.bleandroid

// 定义 BleDevice 类
data class BleDevice(val id: kotlin.String, val name: kotlin.String)

// 定义回调接口
interface DeviceCallback {
    fun demo()
    fun demo1(string: String)
    fun demo2(string: Array<String>)
    fun onDeviceFound(device: Array<BleDevice>)
}

// JNI 接口类
class RustBridge {
    // 加载动态库
    companion object {
        init {
            System.loadLibrary("btleplug")
            System.loadLibrary("rust_lib")
        }
    }

    // 声明外部函数，与 Rust 中的函数对应
    external fun scanWrapper(filter: Array<kotlin.String>, callback: DeviceCallback)
    external fun initWrapper(): Int
    external fun add(n1: Int, n2: Int): Int

}

// 在 Kotlin 中调用 Rust 的 init_wrapper 函数
fun initializeRust() {
    val result = RustBridge().initWrapper()
    if (result != 0) {
        // 处理初始化失败的情况
    } else {
        // 初始化成功
    }
}

// 在 Kotlin 中调用 Rust 的 scan_wrapper 函数
fun scanDevices() {
    // 过滤器
    val filter = arrayOf("filter1", "filter2")

    // 回调处理方法
    val callback = object : DeviceCallback {
        override fun demo() {
            TODO("Not yet implemented")
        }

        override fun demo1(string: String) {
            TODO("Not yet implemented")
        }

        override fun demo2(string: Array<String>) {
            TODO("Not yet implemented")
        }

        override fun onDeviceFound(device: Array<BleDevice>) {
            // 处理找到的设备
        }
    }

    // 调用 Rust 函数
    RustBridge().scanWrapper(filter, callback)
}
