package com.example.bleandroid

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bleandroid.databinding.ActivityMainBinding
import com.example.bleandroid.databinding.ItemDeviceBinding
import java.util.Arrays

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val bluetoothPermissions = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out kotlin.String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 用户已授权所有请求的权限，可以进行蓝牙操作
            } else {
                // 用户拒绝了部分或全部权限
                showPermissionExplanationDialog()
            }
        }
    }

    private fun hasAllBluetoothPermissions(): Boolean {
        return bluetoothPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun showPermissionExplanationDialog() {
        // 显示对话框，解释为何需要这些权限，并提供前往设置页的选项
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 检查是否有全部所需权限
        if (hasAllBluetoothPermissions()) {
            // 已有全部权限，可以进行蓝牙操作
        } else {
            // 请求缺少的权限
            ActivityCompat.requestPermissions(
                this,
                bluetoothPermissions,
                REQUEST_BLUETOOTH_PERMISSIONS_CODE
            )
        }

        // Example of a call to a native method
        binding.sampleText.text = RustBridge().add(1, 3).toString()
        var result = RustBridge().initWrapper()
        Log.d("ZWX ---> ", "result = $result");
        // 开启异步线程
        Thread {
            RustBridge().scanWrapper(arrayOf(

            ), object : DeviceCallback {
                override fun demo() {
                    Log.d("ZWX ---> ", "demo CALLED")
                }

                override fun demo1(string: String) {
                    Log.d("ZWX ---> ", "demo1 CALLED " + string)
                }

                override fun demo2(string: Array<String>) {
                    Log.d("ZWX ---> ", "demo2 CALLED " + Arrays.toString(string))
                }

                override fun onDeviceFound(devices: Array<BleDevice>) {
                    Log.d("ZWX ---> ", "onDeviceFound = " + devices.size)
                    runOnUiThread {
                        binding.sampleText.text =
                            "onDeviceFound = " + devices.size
                        updateRecyclerView(devices);
                    }
                }
            });
        }.start();
    }

    private fun updateRecyclerView(devices: Array<BleDevice>) {
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.adapter = DeviceAdapter(devices)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            binding.recyclerView.adapter?.notifyDataSetChanged()
        } else {
            (binding.recyclerView.adapter as DeviceAdapter).updateDevices(devices)
        }
    }

    class DeviceAdapter(devices: Array<BleDevice>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var devices: Array<BleDevice>

        init {
            this.devices = devices
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
            ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false).let {
            }
            return object : RecyclerView.ViewHolder(view) {
            }
        }

        override fun getItemCount(): Int {
            return devices.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            devices.get(position).let {
                ItemDeviceBinding.bind(holder.itemView).let {
                    it.tvId.text = devices.get(position).id
                    it.tvName.text = devices.get(position).name
                }
            }
        }

        fun updateDevices(devices: Array<BleDevice>) {
            this.devices = devices
            notifyDataSetChanged()
        }

    }
/**
     * A native method that is implemented by the 'bleandroid' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): kotlin.String

    companion object {
        const val REQUEST_BLUETOOTH_PERMISSIONS_CODE = 1001

        // Used to load the 'bleandroid' library on application startup.
        init {
            System.loadLibrary("bleandroid")
            System.loadLibrary("btleplug")
            System.loadLibrary("rust_lib")
        }
    }
}