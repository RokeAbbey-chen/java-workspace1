package test;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class Test1{

    public static void main(String[] args) {
        Thread t = new Thread(){
            @Override
            public void run(){
                new Server(12345).start();
            }
        };
        t.start();
        System.out.println("server start");
        try {
            Thread.sleep(1000);
            Thread t2 = new Thread(){
                @Override
                public void run(){
                    new Client(12345).start();
                }
            };
            t.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static class Server{

        private int port;
        public Server(int port){

            this.port = port;
        }
        public void start(){
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup group = new NioEventLoopGroup();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.AUTO_READ, true)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch){
                            ch.pipeline().addLast(new MyInboundHandler())
                                    .addLast(new MyOutboundHandler())
                                    .addLast(new MyInboundHandler())
                                    .addLast(new MyOutboundHandler())
                                    .addLast(new MyInboundHandler(true))
                                    .addLast(new MyOutboundHandler(true));
                        }
                    });
            try {
                bootstrap.bind(new InetSocketAddress(port)).sync()
                        .channel().closeFuture().sync();
//                group.shutdownGracefully().sync();
//                System.out.println("shutdown !");
//                Future f = null;
//                f.syncUninterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private static class MyInboundHandler extends ChannelInboundHandlerAdapter{

            private static AtomicInteger newestId = new AtomicInteger();
            private int id;
            private boolean last = false;

            private static int newId(){
                return newestId.incrementAndGet();
            }

            public MyInboundHandler(){
                this.id = newId();
            }

            public MyInboundHandler(boolean last){
                this();
                this.last = last;
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ByteBuf m = (ByteBuf) msg;
                ByteBuf nm = null;
                byte[] note = ("process by " + whoAmI()).getBytes();
                boolean flag = true;
                if (m.isWritable()){
                    System.out.println(whoAmI() + ": m.isWritable, and writableBytes = " + m.writableBytes());
                    if (m.writableBytes() >= note.length){
                        nm = m;
                        nm.writeBytes(note);
                        System.out.println(whoAmI() + ": writeInOriginMsg");
                        flag = false;
                    }
                }

                if(flag){
                    nm = ctx.channel().alloc().buffer();
                    nm.writeBytes(m);
                    nm.writeBytes(note);
                }

                if (isLast()){
                    System.out.println("message : " + nm.toString(CharsetUtil.UTF_8));
                    ctx.channel().writeAndFlush(nm);
                }
                super.channelRead(ctx, nm);
            }

            @Override
            public void channelReadComplete(ChannelHandlerContext ctx){

            }

            private String whoAmI(){
                return "inbound-" + id;
            }

            public boolean isLast() {
                return last;
            }

        }

        public static class MyOutboundHandler extends ChannelOutboundHandlerAdapter{

            private static AtomicInteger newestId = new AtomicInteger();

            private int id;
            private boolean last = false;

            public MyOutboundHandler(){
                this.id = newId();
            }

            public MyOutboundHandler(boolean last){
                this();
                this.last = last;
            }

            @Override
            public void read(ChannelHandlerContext ctx) throws Exception {
                System.out.println(whoAmI() + ": read begin" );
                super.read(ctx);
                System.out.println(whoAmI() + ": read end" );
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws InterruptedException {

                if (msg instanceof ByteBuf) {
                    ByteBuf m = (ByteBuf) msg;
                    byte[] b = whoAmI().getBytes();
                    byte[] bytes = new byte[b.length + m.readableBytes()];
                    m.getBytes(0, bytes, b.length, m.readableBytes());
                    ReferenceCountUtil.release(m);
//                ReferenceCountUtil.retain()
                    m = ctx.channel().alloc().buffer(bytes.length).writeBytes(bytes);
                    ctx.writeAndFlush(m, promise).sync();
                }
//                ctx.writeAndFlush(1, promise).sync();
            }


            private int newId(){
                return newestId.incrementAndGet();
            }

            public boolean isLast() {
                return last;
            }

            private String whoAmI(){
                return "outbound-" + id;
            }
        }
    }


    private static class Client{

        InetSocketAddress ipAddress;

        public Client(int port){
            this(new InetSocketAddress(port));
        }

        public Client(String host, int port){
            this(new InetSocketAddress(host, port));
        }

        public Client(InetSocketAddress ipAddress){
            this.ipAddress = ipAddress;
        }

        public void start(){
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new ThreadPerChannelEventLoop(null));
//            AttributeKey.newInstance();
        }

    }


}
