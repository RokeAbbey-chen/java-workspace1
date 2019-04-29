package Test;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLEngine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test1 {

    private static class Result{
        String user;
        String uri;
        int length;


        public Result(String user, String uri) {
            this.user = user;
            this.uri = uri;
        }

        public Result(String user, String uri, int length) {
            this.user = user;
            this.uri = uri;
            this.length = length;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "user='" + user + '\'' +
                    ", uri='" + uri + '\'' +
                    ", length=" + length +
                    '}';
        }
    }
    private static int index = 0;
    private static long lastTime = 0;
    private static long curTime = 0;
    private static Channel ch = null;
    private static final ArrayList<Result> results = new ArrayList<>();
    public static void main(String[] args) throws InterruptedException, IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Test1.class.getResourceAsStream("/test.txt")));
        System.out.println("reader = " + reader);
        final Bootstrap bootstrap = new Bootstrap();
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
                        pipeline.addLast(new HttpObjectAggregator(512 * 1024 * 1024));
                        pipeline.addLast(new SimpleChannelInboundHandler() {

                            @Override
                            public void channelRead0(ChannelHandlerContext ctx, Object msg) throws InterruptedException, IOException {
                                FullHttpMessage m = (FullHttpMessage) msg;
                                int len;
                                len = m.content().toString(CharsetUtil.UTF_8).length();
                                results.get(index).length = len;
                                System.out.println(results.get(index));
                                if ("58224".equals(results.get(index).user)){
                                    System.out.println("line 80");
                                }
                                index ++;
                            }
                        });
                    }
                });
        startBoot(bootstrap, reader);
        ch.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("restart!!");
            }
        });
        String line;//= reader.readLine();
        while((line = reader.readLine() )!= null) {
            String user = line.substring(0, 5);
            String uri = line.substring(6);
            results.add(new Result(user, uri));
//            Thread.sleep(100);
            if (curTime - lastTime <= 100) {
                Thread.sleep( Math.max(10, 100 - ( curTime - lastTime)));
            }
            lastTime = curTime;
            doConnect(ch, uri);
            curTime = System.currentTimeMillis();
        }

        System.out.println("finish");
        for (Result r : results){
            System.out.println(r);
        }
        reader.close();

    }

    private static void startBoot(Bootstrap bootstrap, BufferedReader reader) throws InterruptedException {
        bootstrap.connect(new InetSocketAddress("www.baidu.com", 443))
                .sync()
                .addListener((ChannelFutureListener) future -> {
                    String line = reader.readLine();
                    String user = line.substring(0, 5);
                    String uri = line.substring(6);
                    results.add(new Result(user, uri));
                    doConnect(ch = future.channel(), uri);
                });

    }
    public static void doConnect(Channel channel, String uri){
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.set(HttpHeaderNames.HOST, "wx.qlogo.cn");
        DefaultFullHttpRequest request;
        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                uri,
//                "https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoiciablzbKt3PYCl2fwKJlMJt3bqT78FN8aIChUrrbvW0kf2wKfnQ4nRUHyYoDlrECvJj5pq0wV8Sw/0",
//                            "https://wx.qlogo.cn/mmopen/vi_32/gNZDib8QObG3fVPicxXYy0h1YNmHhl0xN3F221oFuXrP9CtqrHCnCYYJ1m5SqRIOd8fsVWtmgiceqicO5uHXmpiaoibw/0",
                Unpooled.EMPTY_BUFFER, headers, headers);
        channel.writeAndFlush(request);
    }
}
