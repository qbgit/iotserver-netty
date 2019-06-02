package zhongda.iot.server;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.List;
import redis.clients.jedis.*;

public class Publisher extends Thread{

    private final JedisPool jedisPool;
    Jedis jedis0 = null; // jedisPool.getResource();   //连接池中取出一个连接

    private int number =0;
    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void pushMessage(String message)
    {
         if(jedis0 ==null)
            jedis0 =  jedisPool.getResource();   //连接池中取出一个连接
         jedis0.publish("iotserver", message);
    }
    @Override
    public void run() {
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Jedis jedis = jedisPool.getResource();   //连接池中取出一个连接
        while (true) {
            //String line = null;
            try{
                //心跳信息 2秒钟
                String s = "hearbeat:"+Integer.toString(number++);
                jedis.publish("heartbeat", s);
                sleep(2000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}