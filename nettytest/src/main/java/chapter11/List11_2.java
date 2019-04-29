package chapter11;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class List11_2 {

    public static void main(String[] args) throws InterruptedException {
        new Server(12345).start();
    }

    private static class Server{
        private int port;

        public Server(int port){
            this.port = port;
        }

        public void start() throws InterruptedException {
            ServerBootstrap server = new ServerBootstrap();
            server.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpResponseEncoder())
                                .addLast(new HttpRequestDecoder())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
                                        System.out.println(msg);
                                        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                HttpResponseStatus.OK,
                                                Unpooled.wrappedBuffer("你好呀 hahahaha".getBytes("UTF-8"))
                                                );
                                        resp.headers().set(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes() + 3);
                                        ctx.writeAndFlush(resp);
                                    }
                                });
                    }
                });

            server.option(ChannelOption.SO_BACKLOG, 1000);
            server.option(ChannelOption.TCP_NODELAY, true);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);

            server.bind().sync()
                    .channel().closeFuture().sync();
            ByteBuf buf = null;
            buf.markReaderIndex();
        }

    }
}
