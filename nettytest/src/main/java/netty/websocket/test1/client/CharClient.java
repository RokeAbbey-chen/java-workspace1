package netty.websocket.test1.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
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
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("connect !!!");
            }
        });
    }

    private static class ClientChannelInitializer extends ChannelInitializer<Channel>{

        private URI uri;

        public ClientChannelInitializer(URI uri){
            this.uri = uri;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline()
                    .addLast(new HttpClientCodec())
                    .addLast(new HttpObjectAggregator(512 * 1024))
                    .addLast(new WebSocketContentHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders())
                    ));
        }
    }

    private static class WebSocketContentHandler extends SimpleChannelInboundHandler<Object>{

        private WebSocketClientHandshaker handshaker;

        public WebSocketContentHandler(WebSocketClientHandshaker handshaker){
            this.handshaker = handshaker;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx){
            handshaker.handshake(ctx.channel());
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
                System.out.println("ping : " );
                printBytes(ping.content());
                return ;
            }

            if (msg instanceof PongWebSocketFrame){
                PongWebSocketFrame pong = (PongWebSocketFrame) msg;
                System.out.println("pong : " );
                printBytes(pong.content());
                return ;
            }

            if (msg instanceof BinaryWebSocketFrame){
                BinaryWebSocketFrame bFrame = (BinaryWebSocketFrame) msg;
                System.out.println("bFrame : " );
                printBytes(bFrame.content());
                return ;
            }

            if (msg instanceof TextWebSocketFrame){
                TextWebSocketFrame tFrame = (TextWebSocketFrame) msg;
                System.out.println("tFrame : " );
                printBytes(tFrame.content());
                return ;
            }

            if (msg instanceof ContinuationWebSocketFrame){
                ContinuationWebSocketFrame cFrame = (ContinuationWebSocketFrame) msg;
                System.out.println("cFrame : ");
                printBytes(cFrame.content());
                return ;
            }

            if (msg instanceof CloseWebSocketFrame){
                CloseWebSocketFrame clFrame = (CloseWebSocketFrame) msg;
                System.out.println("clFrame : ");
                printBytes(clFrame.content());
                return ;
            }

        }

    }

    private static void printBytes(ByteBuf buf){
        byte[] bytes = new byte[buf.readableBytes()];
        System.out.println("to string : \n" + buf.toString(CharsetUtil.UTF_8));
        buf.readBytes(bytes);
        printBytes(bytes);
    }

    private static void printBytes(byte[] bytes){

        for (byte b : bytes){
            System.out.print("0x" + Integer.toHexString(b) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws URISyntaxException {
        CharClient client = new CharClient(new InetSocketAddress(8887),
                new URI("ws://localhost:8887/ws"));
        client.connect();

    }

}
