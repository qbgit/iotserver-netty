package zhongda.iot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import redis.clients.jedis.*;
 /**
  * Netty实现的tcp iotserver 程序
  * @author qianbo
  */
 public class TcpServer
 {
     /*端口号*/
    static final int PORT1 = 9002;
    static final int PORT2 = 9003;
     public static void main(String[] args)
     {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.1.222", 6379);
		Publisher publish = new Publisher(jedisPool);
		publish.start();
		//publish.join();
         EventLoopGroup bossGroup = null;
         EventLoopGroup workerGroup = null;
         ServerBootstrap b = null;
         System.out.println("the server start at 9002 9003");
         try{
             //1:第一个线程组是用于接收Client连接的
             bossGroup = new NioEventLoopGroup(); 
             //2:第二个线程组是用于实际的业务处理操作的
             workerGroup = new NioEventLoopGroup();
             //3:创建一个启动NIO服务的辅助启动类ServerBootstrap 就是对我们的Server进行一系列的配置
             b = new ServerBootstrap();
             //4:绑定两个线程组
             b.group(bossGroup, workerGroup)
             //5:需要指定使用NioServerSocketChannel这种类型的通道
             .channel(NioServerSocketChannel.class)//(3) 服务端 -->NioServerSocketChannel
             //6:一定要使用childHandler 去绑定具体的事件处理器
             .childHandler(new ChannelInitializer<SocketChannel>() //(4)   childHandler
             {
                 @Override
                 protected void initChannel(SocketChannel sc) throws Exception
                 {
                     //7:将自定义的serverHandler加入到管道中去（多个）
                     
                     ChannelPipeline p = sc.pipeline();
                     p.addLast(new MessageDecoder(255, 6, 1));
                     //p.addLast(new MessageEncoder());
                     p.addLast(new ServerHandler());//handler中实现真正的业务逻辑
                 }
             })
             //8:设置TCP连接的缓冲区
             .option(ChannelOption.SO_BACKLOG, 200)//(5)
             //设置发送缓冲大小 32K发送缓冲
            // .option(ChannelOption.SO_SNDBUF, 32*1024) 
             //设置接收缓冲大小
             // .option(ChannelOption.SO_RCVBUF, 32*1024) 
             //9:保持连接
             .childOption(ChannelOption.SO_KEEPALIVE, true);//(6)
             //10:绑定指定的端口 进行监听
             ChannelFuture cf2= b.bind(PORT1).sync(); // (7)  
             ChannelFuture cf3= b.bind(PORT2).sync(); // (7)   绑定多个端口 
 

             cf2.channel().closeFuture().sync(); //异步等待关闭 
             cf3.channel().closeFuture().sync(); //异步等待关闭

         }catch(Exception e){
             e.printStackTrace();
         }finally{
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }
}