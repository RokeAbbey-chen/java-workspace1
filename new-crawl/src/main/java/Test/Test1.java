package Test;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;

public class Test1 {

    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        SslContext context = SslContextBuilder.forClient().build();
                        SSLEngine engine = context.newEngine(ch.alloc());
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new SslHandler(engine));
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg){
                                FullHttpMessage m = (FullHttpMessage) msg;
                                System.out.println(m.content().toString(CharsetUtil.UTF_8));
                            }
                        });
                    }
                });
        bootstrap.connect(new InetSocketAddress("wx.qlogo.cn", 443))
                .sync()
                .addListener((ChannelFutureListener) future -> {
                    DefaultFullHttpRequest request;
                    request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                            HttpMethod.GET,
                            "https://wx.qlogo.cn/mmopen/vi_32/gNZDib8QObG3fVPicxXYy0h1YNmHhl0xN3F221oFuXrP9CtqrHCnCYYJ1m5SqRIOd8fsVWtmgiceqicO5uHXmpiaoibw/0"));
                    request.
                    future.channel().writeAndFlush(
                });

    }
}
