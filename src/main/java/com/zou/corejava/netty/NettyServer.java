package com.zou.corejava.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

/**
 * Netty 入门：https://mp.weixin.qq.com/s/RPTETiULRAkOS-ZTd6xM2A
 */
public class NettyServer {

    private static int HEADER_LENGTH = 4;

    public void bind(int port) throws Exception {

        // 1. ServerBootstrap 是启动服务的辅助类，有关socket的参数可以通过ServerBootstrap进行设置
        /**
         * 2. NioServerSocketChannelFactory构造方法定义了两个线程池
         * NioServerSocketChannelFactory(Executor bossExecutor, Executor workerExecutor)
         * bossExecutor用来accept客户端连接，workerExecutor处理客户端数据的读写操作
         */
        ServerBootstrap b = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        // 3. 构造对应的pipeline，设置Handler
        b.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipelines = Channels.pipeline();
                pipelines.addLast(MessageHandler.class.getName(), new MessageHandler());
                return pipelines;
            }
        });
        // 4. 开启端口号
        b.bind(new InetSocketAddress(port));
    }

    // 处理消息
    static class MessageHandler extends SimpleChannelHandler {

        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            // 接收客户端请求
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            String message = new String(buffer.readBytes(buffer.readableBytes()).array(), "UTF-8");
            System.out.println("<服务端>收到内容=" + message);

            // 给客户端发送回执
            byte[] body = "服务端已收到".getBytes();
            byte[] header = ByteBuffer.allocate(HEADER_LENGTH).order(ByteOrder.BIG_ENDIAN).putInt(body.length).array();
            Channels.write(ctx.getChannel(), ChannelBuffers.wrappedBuffer(header, body));
            System.out.println("<服务端>发送回执,time=" + System.currentTimeMillis());

        }
    }

    public static void main(String[] args) {
        try {
            new NettyServer().bind(1088);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
