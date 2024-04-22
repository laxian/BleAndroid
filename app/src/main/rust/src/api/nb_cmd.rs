pub mod msg;
pub use msg::*;

pub mod enc_ffi;
pub use enc_ffi::*;


// 和九号的蓝牙设备握手和通信
trait NbManager {
	async fn handshake(&self) -> anyhow::Result<()>;
	fn send(&self, msg: &Message) -> anyhow::Result<()>;
	fn recv(&self) -> anyhow::Result<Message>;
}
