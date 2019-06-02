package zhongda.iot.server;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.List;
import redis.clients.jedis.*;
import zhongda.iot.server.Subscriber;
public class Substart extends Thread {

    private final JedisPool jedisPool;
    private final Subscriber subscriber = new Subscriber();

    private final String channel = "mychannel";

    //订阅的名称
    public Substart(JedisPool jedisPool,String channelname) {
        super("Substart");
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();   //取出一个连接
            jedis.subscribe(subscriber, channel);    //通过subscribe 的api去订阅，入参是订阅者和频道名
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}