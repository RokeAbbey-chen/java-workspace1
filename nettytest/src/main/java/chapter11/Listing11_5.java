package chapter11;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import sun.security.ssl.SSLContextImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Listing11_5 {

    public static void main(String[] args) throws SSLException, InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(
                new ChannelInitializer<Channel>() {
                    SslContext context = SslContextBuilder.forClient().build();
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new SslHandler(context.newEngine(ch.alloc())))
                                .addLast(new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println("------------");
                                        System.out.println(buf.toString(CharsetUtil.UTF_8));
                                        super.write(ctx, msg, promise);
                                    }
                                })
                            .addLast(new HttpRequestEncoder())
                            .addLast(new HttpResponseDecoder())
                            .addLast(new HttpObjectAggregator(512 * 1024))
                            .addLast(new MyHandler());
                    }
                }
        );
        bootstrap.connect(new InetSocketAddress("www.baidu.com", 443))
                .sync()
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("connected finished !!");
                        FullHttpMessage request =
                                new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=%CD%BC%C6%AC&fr=ala&ala=1&alatpl=others&pos=0");
                        future.channel().writeAndFlush(request);
                    }
                });
    }

    private static class MyHandler extends SimpleChannelInboundHandler<FullHttpResponse> implements ChannelOutboundHandler{

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
            System.out.println(msg.content().toString(CharsetUtil.UTF_8));
        }

        @Override
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            ctx.bind(localAddress, promise);
        }

        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            ctx.connect( remoteAddress, localAddress, promise);
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            ctx.disconnect(promise);
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            ctx.close(promise);
        }

        @Override
        public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            ctx.deregister(promise);
        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            ctx.read();
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            ctx.writeAndFlush(msg, promise);
        }

        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }
    }
}
