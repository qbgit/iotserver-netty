package zhongda.iot.server;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.List;
import redis.clients.jedis.*;



public class Subscriber extends JedisPubSub {

    public Subscriber(){}
    @Override
    public void onMessage(String channel, String message) {       //收到消息会调用
        System.out.println(String.format("receive channel %s, message %s", channel, message));
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        System.out.println(String.format("subscribe ok, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅 会调用
        System.out.println(String.format("unsubscribe ok, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}

