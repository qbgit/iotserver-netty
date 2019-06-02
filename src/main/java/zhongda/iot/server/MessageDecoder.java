package zhongda.iot.server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


 
/**
 * @author qianbo 以前的8051端口协议重写成java
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {
 
    //头部 0x69 1字节 设备ID 4字节 cmd 1字节 长度1字节 
    //。。。content   
    //crc 1 crc 2 
    //end 1 字节 0x16
    private static final int hsize = 7;
 
    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
 
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
 
        if (in.readableBytes() < hsize) {
            return null;
        }
 
        in.markReaderIndex();
 
        byte magic = in.readByte(); //头部字节0x69
        int deviceid = in.readInt(); //四字节大端ID号码
        byte cmd = in.readByte(); //命令
        byte dataLength = in.readByte();
 
        
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return null;
        }
        //钱波 ：加上两个crc校验和一个结尾
        byte[] data = new byte[dataLength + 3]; 
        in.readBytes(data);
 
        String body = new String(data, "UTF-8");
        Message msg = new Message(magic,  deviceid, cmd, body);
        return msg;
    }
}