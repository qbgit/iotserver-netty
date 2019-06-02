var redis = require("redis");  
var client = redis.createClient(6379, "192.168.1.222");  
var client1 = redis.createClient(6379, "192.168.1.222");

client1.on('connect', function () {
    client1.subscribe("draw");
});
client1.on("message", function (channel, message) {
    console.log(typeof (message));
    var obj = JSON.parse(message);
    console.log("普通订阅接收到来自" + channel + "的信息:" + obj.session);
    if (message == "quit") {
        client1.unsubscribe("draw");
        console.log("普通订阅操作已经取消");
        //client2.quite();
    }
});
/*
var client2 = redis.createClient("6379", "192.168.1.222");
client2.on('connect', function () {
    client2.psubscribe("draw*");
});
client2.on("pmessage", function (p, channel, message) {
    console.log("批量订阅接收到来自" + channel + "的信息:" + message);
    if (message == "quit") {
        client2.punsubscribe("draw*");
        console.log("批量订阅操作已经取消");
        //client2.quite();
    }
});
*/




/*
function getRedisData() {  
    //客户端连接redis成功后执行回调
    client.on("ready", function () {
        //订阅消息
        client.subscribe("chat");
        client.subscribe("chat1");
        console.log("订阅成功。。。");
    });

    client.on("error", function (error) {
        console.log("Redis Error " + error);
    });

    //监听订阅成功事件
    client.on("subscribe", function (channel, count) {
        console.log("client subscribed to " + channel + "," + count + "total subscriptions");
    });

    //收到消息后执行回调，message是redis发布的消息
    client.on("message", function (channel, message) {
        console.log("我接收到信息了" + message);
        //dealWithMsg(message);
    });

    //监听取消订阅事件
    client.on("unsubscribe", function (channel, count) {
        console.log("client unsubscribed from" + channel + ", " + count + " total subscriptions")
    });
}

function dealWithMsg(message) {  
    //按照message查询内容
    client1.zscore("key", message, function (err, reply) {
        console.log(message + "的内容是：" + reply);
    });
}
getRedisData();  
*/