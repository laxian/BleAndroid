const MIN_LEN: usize = 7;

pub struct Message {
    header: [u8; 2],
    len: u8,
    s_id: u8,
    r_id: u8,
    cmd: u8,
    index: u8,
    data: Option<Vec<u8>>,
}

impl Message {
    fn new(len: u8, r_id: u8, cmd: u8, s_id: u8, index: u8, data: Option<Vec<u8>>) -> Self {
        Message {
            header: [0x5a, 0xa5],
            len,
            s_id,
            r_id,
            cmd,
            index,
            data,
        }
    }

    fn parse(frame: &[u8]) -> Result<Self, &'static str> {
        if frame.len() < MIN_LEN {
            return Err("Invalid message length");
        }
        let header = [frame[0], frame[1]];
        let len = frame[2];
        let s_id = frame[3];
        let r_id = frame[4];
        let cmd = frame[5];
        let index = frame[6];
        let data = if len > 0 {
            Some(frame[7..].to_vec())
        } else {
            None
        };
        if header != [0x5a, 0xa5] {
            return Err("Invalid header element");
        }
        Ok(Message::new(len, r_id, cmd, s_id, index, data))
    }

    fn to_bytes(&self) -> Vec<u8> {
        let mut bytes = Vec::new();
        bytes.extend_from_slice(&self.header);
        bytes.push(self.len);
        bytes.push(self.s_id);
        bytes.push(self.r_id);
        bytes.push(self.cmd);
        bytes.push(self.index);
        if let Some(ref data) = self.data {
            bytes.extend_from_slice(data);
        }
        bytes
    }
}

impl std::fmt::Display for Message {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(
            f,
            "Message{{header: {:?}, len: {}, s_id: {}, r_id: {}, cmd: {}, index: {}, data: {:?}}}",
            self.header,
            self.len,
            self.s_id,
            self.r_id,
            self.cmd,
            self.index,
            &self.data.as_ref().map(|d| &d[..])
        )
    }
}
