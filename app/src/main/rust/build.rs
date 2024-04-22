

fn main() {
        // 告诉编译器去指定路径下查找共享库文件
        println!("cargo:rustc-link-search=/Users/leochou/Github/flutter/flutter_btleplug3/enc/target/android/arm64-v8a/");
    
        // 告诉编译器链接到指定的共享库文件
        println!("cargo:rustc-link-lib=nbenc_ffi");
}