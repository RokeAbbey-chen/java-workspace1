package Test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;

public class TestServer {

    public static void main(String[] args) throws InterruptedException, SSLException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new SslHandler(SslContextBuilder.forClient().build().newEngine(ch.alloc())))
                                .addLast(new HttpClientCodec())
                                .addLast(new MyHandler(ch) );
                    }
                }) ;
        bootstrap.bind(new InetSocketAddress("127.0.0.1", 12345))
                .sync()
                .addListener((ChannelFutureListener) future -> System.out.println("start !!! "));
    }

    private static class MyHandler extends ChannelInboundHandlerAdapter{

        private Channel ch;
        public MyHandler(Channel ch){
            this.ch = ch;
        }

        int num = 0;
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg){
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    ctx.alloc().buffer().writeInt(num)
            );
            ctx.writeAndFlush(response);
            num ++;
//            if (num == 100){
//                ch.close();
//            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx){
            System.out.println("active");
            ctx.writeAndFlush(Unpooled.copiedBuffer("first".getBytes()));
        }
    }

}
