package chapter1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

public class List1_4 {

    @Test
    public void test1(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try (
                ReadableByteChannel src = Channels.newChannel(System.in);
                WritableByteChannel dest = Channels.newChannel(System.out);
                ){
            int len = -1;
            while ((len = src.read(byteBuffer)) != -1){
                byteBuffer.flip();
                dest.write(byteBuffer);
                byteBuffer.compact();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2(){
            Channel channel = new NioSocketChannel();
            final ChannelFuture channelFuture = channel.connect(new InetSocketAddress("192.168.100.252", 8080));
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        ByteBuf buf = Unpooled.copiedBuffer("hello", Charset.defaultCharset());
                        ChannelFuture wf = f.channel().writeAndFlush(buf);
                        System.out.println("wf == " + wf + " , f = " + f + ", channelFuture = " + channelFuture);
                        System.out.println("wf == f : " + (wf == f) + ",wf == channelFuture : " + (wf == channelFuture)
                                + "f == channelFuture : " + (f == channelFuture));
                    } else {
                        Throwable cause = f.cause();
                        cause.printStackTrace();
                    }
                }
            });
            channel.close();
    }

    @Test
    public void test3(){
        new ChannelHandler() {
            @Override
            public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

            }
        };

        new ChannelInboundHandler() {
            @Override
            public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

            }

            @Override
            public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

            }

            @Override
            public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

            }
        };
    }
}
