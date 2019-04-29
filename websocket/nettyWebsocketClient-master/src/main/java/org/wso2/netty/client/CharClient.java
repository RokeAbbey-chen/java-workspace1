package org.wso2.netty.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

public class CharClient {

    private Bootstrap bootstrap;

    public CharClient(InetSocketAddress addr, URI uri){

        init(addr, uri);

    }

    private void init(InetSocketAddress addr, URI uri){

        bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .remoteAddress(addr)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer(uri));

    }

    public ChannelFuture connect(){
        return bootstrap.connect().addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("connect !!!");
            }
        });
    }

    private static class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        private URI uri;

        public ClientChannelInitializer(URI uri){
            this.uri = uri;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline()
                    .addLast(new HttpClientCodec())
                    .addLast(new HttpObjectAggregator(512 * 1024))
                    .addLast(new WebSocketContentHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders())
                    ));
        }
    }

    public static class WebSocketContentHandler extends SimpleChannelInboundHandler<Object> {

        private WebSocketClientHandshaker handshaker;

        public WebSocketContentHandler(WebSocketClientHandshaker handshaker){
            this.handshaker = handshaker;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx){
            handshaker.handshake(ctx.channel());
            System.out.println("headshake !!! ");
            ctx.fireChannelActive();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

            System.out.println(msg);
            Channel channel = ctx.channel();

            if (msg instanceof ByteBuf){
                ByteBuf b = (ByteBuf) msg;
                System.out.println(b.toString(CharsetUtil.UTF_8));
            }

            if (!handshaker.isHandshakeComplete() && msg instanceof FullHttpResponse){
                FullHttpResponse response = (FullHttpResponse) msg;
                handshaker.finishHandshake(channel, response);
                System.out.println("handshake complete!!!");
                return ;
            }

            if (msg instanceof PingWebSocketFrame){
                PingWebSocketFrame ping = (PingWebSocketFrame) msg;
                System.out.println("ping : \n" + ping.content().toString(CharsetUtil.UTF_8));
                return ;
            }

            if (msg instanceof PongWebSocketFrame){
                PongWebSocketFrame pong = (PongWebSocketFrame) msg;
                System.out.println("pong : \n" + pong.content().toString(CharsetUtil.UTF_8));
                return ;
            }

            if (msg instanceof BinaryWebSocketFrame){
                BinaryWebSocketFrame bFrame = (BinaryWebSocketFrame) msg;
                System.out.println("bFrame : " + bFrame.content().toString(CharsetUtil.UTF_8));
                return ;
            }

            if (msg instanceof TextWebSocketFrame){
                TextWebSocketFrame tFrame = (TextWebSocketFrame) msg;
                System.out.println("tFrame : " + tFrame.content().toString(CharsetUtil.UTF_8));
                return ;
            }

            if (msg instanceof ContinuationWebSocketFrame){
                ContinuationWebSocketFrame tFrame = (ContinuationWebSocketFrame) msg;
                System.out.println("tFrame : " + tFrame.content().toString(CharsetUtil.UTF_8));
                return ;
            }

            if (msg instanceof CloseWebSocketFrame){
                CloseWebSocketFrame cFrame = (CloseWebSocketFrame) msg;
                System.out.println("cFrame : " + cFrame.content().toString(CharsetUtil.UTF_8));
                return ;
            }

        }

    }

    public static void main(String[] args) throws URISyntaxException {
        CharClient client = new CharClient(new InetSocketAddress(8887),
                new URI("ws://localhost:8887/ws"));
        client.connect();

    }

}
