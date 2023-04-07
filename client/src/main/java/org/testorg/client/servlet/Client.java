package org.testorg.client.servlet;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.CompletableFuture;

@WebListener
public class Client implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HapiContext context = new DefaultHapiContext();

        CompletableFuture.runAsync(()->{
            EventLoopGroup group = new NioEventLoopGroup();

            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new ClientChannelHandler(context));
                            }
                        });

                ChannelFuture f = b.connect("localhost", 8081).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                group.shutdownGracefully();
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // код для завершения приложения
        // например, закрытие соединений с базой данных, освобождение ресурсов и т.д.
    }
}