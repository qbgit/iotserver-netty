package zhongda.iot.server;

//import java.io.Console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zhongda.iot.server.Publisher;
import redis.clients.jedis.*;
@SpringBootApplication
public class DemoApplication {

	public static void main1(String[] args) throws InterruptedException {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("启动");
		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.1.222", 6379);
		//Jedis jedis = new Jedis("192.168.1.222");
		Publisher publish = new Publisher(jedisPool);
		publish.start();
		publish.join();
	}

}
