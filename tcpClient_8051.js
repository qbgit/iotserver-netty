var net = require('net');
var HOST = '127.0.0.1';
var PORT = 9002;
var client = new net.Socket();


function test_8051()
{
    const buftemp = Buffer.allocUnsafe(14);
    buftemp[0] = 0x69;
    buftemp.writeInt32BE(0x0101, 1); //四字節
    buftemp[5] = 0x03;//cmd
    buftemp[6] = 0x04; //content len 
    buftemp[7] = 0x00;
    buftemp[8] = 0x09;
    buftemp[9] = 0x10;
    buftemp[10] = 0x08;
    buftemp[11] = 0x00; //crc 1
    buftemp[12] = 0x00; //crc 2;
    buftemp[13] = 0x16; // end 22 

    client.write(buftemp);
    setTimeout(test_8051, 5000);
}
function test_8052() {
    const buftemp = Buffer.allocUnsafe(29 + 2);
    buftemp.writeInt16BE(29 + 2, 0); //长度
    buftemp.writeInt16BE(0x0202, 2); //
    buftemp.writeInt32BE(0x0101, 4)
    buftemp[8] = 17; //6字节时间戳
    buftemp[9] = 12;
    buftemp[10] = 14;
    buftemp[11] = 12;
    buftemp[12] = 5;
    buftemp[13] = 6;
    buftemp[14] = 1;  //版本
    buftemp[15] = 0; //后继
    buftemp[16] = 2; //采集周期
    buftemp[17] = 1; //信号强度
    buftemp[18] = 8; //电池电量
    buftemp[19] = 8; //通道1种类
    buftemp[20] = 8; //通道2种类
    buftemp[21] = 8; //通道3种类
    buftemp[22] = 8; //通道4种类

    buftemp[23] = 2; //两个传感器
    var x = 0;
    x = (2 << 4);  //2个字节
    x += (2 << 1); //小数位数
    x += 0;        //正数

    buftemp[24] = x; //36 //0010 0100 2个字节 2位小数 正数
    buftemp.writeInt16BE(0x10, 25);
    buftemp.writeInt16BE(0x11, 27);
    buftemp[29] = 0;//校验数据
    buftemp[30] = 0; //校验数据 2字节
    client.write(buftemp);
    setTimeout(test_8052, 5000);
}

var step = 0.01;
function test_8053() {
    const buftemp = Buffer.allocUnsafe(4 + 2 + 6 + 12);
    buftemp[0] = 0; //
    buftemp[1] = 1; //
    buftemp[2] = 0; //
    buftemp[3] = 1; //设备编号
    buftemp[4] = 1;//命令字
    buftemp[5] = 24;  //包体长度
    buftemp[6] = 0;// 正常待机，正常下锤探测，第一次首盘探测，过程反复探测，顶部到位反复探测，故障停机
    buftemp[7] = 0; //故障字
    buftemp[8] = 1; //警示字1
    buftemp[9] = 1;//警示字2
    buftemp[10] = 10; //电池电量
    buftemp[11] = 0;//备用
    step += 0.01;
    buftemp.writeFloatBE(12.90 +step , 12);
    buftemp.writeFloatBE(13.4 + step, 16);
    buftemp.writeFloatBE(34.5 +step, 20);
    client.write(buftemp);

    setTimeout(test_8053,5000);
}
function closesocket() {
    // client.end();
    console.log("please close me");
}
function test_9002() {
    client.write("this is a test");
    setTimeout(test_9002, 1000);
}

client.connect(PORT, HOST, function () {

    console.log('CONNECTED TO: ' + HOST + ':' + PORT);
    // Write a message to the socket as soon as the client is connected, the server will receive it as message from the client 
    //client.write('I am Chuck Norris!');
    if(PORT == 8051)
        test_8051();

    if(PORT == 8052)
        test_8052();
    else if(PORT == 9002)
        test_8051();
    //setTimeout(test_8053, 1000);
});

// Add a 'data' event handler for the client socket
// data is what the server sent to this socket
client.on('data', function (data) {

    console.log(data);
    //client.destroy();
});

// Add a 'close' event handler for the client socket
client.on('close', function () {
    console.log('Connection closed');
});