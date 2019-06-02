package zhongda.iot.server;
import java.nio.charset.Charset;
 
/**
 * @author 钱波 第一个传感器的8051端口
 */
public class Message {
 
    private final Charset charset = Charset.forName("utf-8");
 
    private byte magicType;
    private long deviceid; //设备
    private byte cmd;//命令
    private byte length;
    private String body;
 
    public Message(){
 
    }
 
    public Message(byte magicType,long deviceid, byte cmd, byte[] data) {
        this.magicType = magicType; //0x69
        this.deviceid = deviceid;
        this.cmd =cmd;
        this.length = (byte)data.length;
        this.body = new String(data, charset);
    }
 
    public Message(byte magicType, long deviceid, byte cmd, String body) {
        this.magicType = magicType;
        this.cmd = cmd;
        this.deviceid = deviceid;
        this.length = (byte)body.getBytes(charset).length;
        this.body = body;
    }
    public String GetBody()
    {
        return this.body;
    }
    public byte GetCmd()
    {
        return this.cmd;
    }
}
   
