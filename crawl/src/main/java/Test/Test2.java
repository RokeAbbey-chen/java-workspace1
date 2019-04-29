package Test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class Test2 {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Helper helper = new Helper("/robot.txt");
        helper.start();
    }

    private static class Helper{

        private BufferedReader reader;
        private Bootstrap bootstrap;
        private BufferedWriter writer;

        private boolean readerClosed = false;

        private String[][] results = new String[10000][3];
        private int index = 0;
        private NioEventLoopGroup grp = new NioEventLoopGroup();
        private MessageLoop lp = new MessageLoop();


        public Helper(String fileName) throws FileNotFoundException {
            this.init(fileName);
        }

        private void init(String fileName) throws FileNotFoundException {
            this.reader = new BufferedReader(new InputStreamReader(Test2.class.getResourceAsStream(fileName)));
            this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out.dat")));

//            StringBuilder sb = new StringBuilder();
//            sb.append("56312   https://wx.qlogo.cn/mmopen/vi_32/MF9CGfZeAaEel7SXn2O1nC1nJia14BsPg9g79WMajJCicib4akagyKyb1QL6ibj5f1ic2Y5bTnJibkSTibfLDVgxzvOJQ/0\n");
//            sb.append("56312   https://wx.qlogo.cn/mmopen/vi_32/MF9CGfZeAaEel7SXn2O1nC1nJia14BsPg9g79WMajJCicib4akagyKyb1QL6ibj5f1ic2Y5bTnJibkSTibfLDVgxzvOJQ/0\n");
//            this.reader = new BufferedReader(new StringReader(sb.toString()));
            this.bootstrap = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                .addLast(
                                    new SslHandler(
                                            SslContextBuilder.forClient().build()
                                                    .newEngine(ch.alloc())))
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024))
                                .addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(results[index][0]);
                                        sb.append(" ");
                                        sb.append(results[index][1]);
                                        sb.append(" ");
                                        sb.append(msg.content().readableBytes());
                                        sb.append("\n");
                                        String s;
                                        writer.write(s = sb.toString());
                                        System.out.print(s);
                                        index ++;
                                        doRequest(ctx.channel());
                                    }
                                    @Override
                                    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
                                        if (readerClosed){
                                            writer.close();
                                            System.out.println("writer close !!");
                                        }
                                        ctx.fireChannelReadComplete();
                                    }
                                })
                            ;


                            ch.closeFuture().addListener((ChannelFutureListener) future -> {
                                System.out.println("restart !!!");
                                restart();
                            });
                        }
                    });
        }

        private void restart() throws InterruptedException {
            start();
        }
        private void start() throws InterruptedException {
            if (bootstrap != null){
                ChannelFuture future = bootstrap.connect(new InetSocketAddress("wx.qlogo.cn", 443));
                System.out.println("void : " + future.isVoid() + ", cancelled : " + future.isCancelled()
                        + ", done : " + future.isDone() + ", isSuccess : " + future.isSuccess());
                future.sync().addListener(
                        (ChannelFutureListener) future1 -> {
                            Channel ch = future1.channel();
                            lp.execute( () -> {
                                try {
                                    doRequest(ch);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                );

            }
            else {
                System.out.println("bootstrap is null");
            }
        }

        private void doRequest(Channel ch) throws IOException, InterruptedException {
            long lastTime = 0;
            long curTime;
            String line = null;
            if (ch.isActive() && (line = reader.readLine()) != null){
                curTime = System.currentTimeMillis();
                Thread.sleep(Math.max(200, 200 - (curTime - lastTime)));
                String user = line.substring(0, 5);
                String uri = line.substring(6);
                results[index][0] = user;
                results[index][1] = uri;
                doRequest(uri, ch);
//                System.out.println(" times = " + times + ", isWritable : " + ch.isWritable()
//                        + ", isActive : " + ch.isActive() + ", isOpen : " + ch.isOpen()
//                        + ", isRegistered : " + ch.isRegistered()
//                );
            }
            if (line == null){
                System.out.println("reader close");
                readerClosed = true;
                reader.close();
            }

        }

        private void doRequest(String uri, Channel ch){
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.set(HttpHeaderNames.HOST, "wx.qlogo.cn");
            DefaultFullHttpRequest req = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, uri, Unpooled.EMPTY_BUFFER, headers, EmptyHttpHeaders.INSTANCE);
            ch.writeAndFlush(req);
        }
    }

    private static class MyHandler extends SimpleChannelInboundHandler<FullHttpResponse>{

        public MyHandler(String[][] results, int offset){

        }
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {

            System.out.println(msg.content().readableBytes());
        }
    }

    private static class MyHandler2 extends SimpleChannelInboundHandler<ByteBuf>{

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println(msg.readInt());
//            System.out.println(msg.toString(CharsetUtil.UTF_8));
        }
    }



    public static class MessageLoop extends SingleThreadEventLoop{

        protected MessageLoop() {
            super(null, new ThreadPerTaskExecutor(new DefaultThreadFactory(MessageLoop.class)), false);
        }

        @Override
        protected void run() {

            while (true) {
                runAllTasks();
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
