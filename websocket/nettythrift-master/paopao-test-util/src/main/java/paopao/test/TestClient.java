package paopao.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import io.netty.util.internal.SocketUtils;
import paopao.util.CommonUtil;
import paopao.util.EProgram;
import paopao.util.HttpServiceUtil;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TestClient {


    private Bootstrap bootstrap;

    private String script;

    public TestClient(InetSocketAddress address, URI uri){
        init(address, uri);
    }


    private void init(final InetSocketAddress address, final URI uri) {
        System.out.println(uri);
        bootstrap = new Bootstrap()
                .group(new OioEventLoopGroup())
                .channel(OioSocketChannel.class)
                .remoteAddress(address)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new SslHandler(SslContextBuilder.forClient().build().newEngine(ch.alloc())))
//                                .addLast(new ChannelOutboundHandlerAdapter() {
//                                    @Override
//                                    public void write(ChannelHandlerContext ctx, Object m, ChannelPromise promise) throws Exception {
//                                        ByteBuf msg = (ByteBuf) m;
//                                        System.out.println("-----request");
//                                        System.out.println(msg.toString(CharsetUtil.UTF_8));
//                                        System.out.println("--------------");
//                                        ctx.writeAndFlush(msg);
//                                    }
//                                })
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(512 * 1024))
                                .addLast(new WebSocketContentInHandler(
                                        WebSocketClientHandshakerFactory.newHandshaker(uri,
                                                WebSocketVersion.V13, null, true, null)

                                        , new DefaultChannelPromise(ch).addListener((ChannelFutureListener) future -> {
//                                            resolveAndExecute(ch);
                                                sendLoginCommand(ch);
                                        })
                                ));
                    }
                });
    }


    private void resolveAndExecute(Channel ch){
        resolveScript(ch);
        executeScript(ch);
    }

    private void resolveScript(Channel ch){

    }

    private void executeScript(Channel ch){
        ch.writeAndFlush(Unpooled.copiedBuffer("{user_id:'1000000', command : 'login'}".getBytes(CharsetUtil.UTF_8)));
    }


    private void sendLoginCommand(Channel ch){
        Map<String, Object> command = new HashMap<>();
        command.put("user_id", EProgram.getById(1004).getUserId());
        command.put("command", "login");
        String jsonString = JSON.toJSONString(command);
        System.out.println(jsonString);
        ch.writeAndFlush(new TextWebSocketFrame(jsonString));
    }

    public void connect() throws InterruptedException {
        bootstrap.connect().sync();
    }

    private static class WebSocketContentInHandler extends SimpleChannelInboundHandler<Object>{

        private WebSocketClientHandshaker handshaker;

        private ChannelPromise handshakePromise;

        public WebSocketContentInHandler(WebSocketClientHandshaker handshaker, ChannelPromise handshakePromise) {

            this.handshaker = handshaker;
            this.handshakePromise = handshakePromise;

//            wss://websocket.xingqiu123.com/
        }


        @Override
        public void channelActive(ChannelHandlerContext ctx){
            handshaker.handshake(ctx.channel());
            ctx.fireChannelActive();
        }


        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel ch = ctx.channel();

            System.out.println("msg : " + msg);
            if (!handshaker.isHandshakeComplete()){
                if (msg instanceof FullHttpResponse){
                    FullHttpResponse response = (FullHttpResponse) msg;
                    handshaker.finishHandshake(ch, response);
                    handshakePromise.setSuccess();
                    System.out.println("handshake finish");
                }
                else {
                    System.out.println("handshake should finish but the msg is not a instance of fullhttpresponse");
                }
                return ;
            }

            if (msg instanceof WebSocketFrame) {
                WebSocketFrame frame = (WebSocketFrame) msg;
                System.out.println("string:" + frame.content().toString(CharsetUtil.UTF_8));
                printByteBufToHex(frame.content());
            }

            return ;
        }
    }

    private static void printByteBufToHex(ByteBuf buf){

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        printBytesToHex(bytes);
    }

    private static void printBytesToHex(byte[] bytes){

        for (byte b : bytes){
            System.out.print(Integer.toHexString(b) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {

        TestClient client = new TestClient(
                new InetSocketAddress("lobby.xingqiu123.com", 443),
                new URI("wss://lobby.xingqiu123.com:443/" + EProgram.getById(1004).getPrefix()));
        client.connect();
    }
}
