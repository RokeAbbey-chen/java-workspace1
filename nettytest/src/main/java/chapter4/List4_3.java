package chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class List4_3 {
    public static void main(String[] args) {
        new List4_3().start(12345);
    }

    public void start(int port){
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi, my name is server", CharsetUtil.UTF_8));
        ServerBootstrap boot = new ServerBootstrap();
//        OioServerSocketChannel
        EventLoopGroup group = new NioEventLoopGroup();
        boot.group(group).channel(NioServerSocketChannel.class)
            .localAddress(new InetSocketAddress(port)) //换成bind如何
            .childHandler(new ChannelInitializer(){

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new InboundHandler())
//                            .addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)))
                        .addLast(new InboundHandler())
                        .addLast(new OutboundHandler())
                        .addLast(new OutboundHandler())
                        .addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx){
//                            ctx.read();
                            ctx.writeAndFlush(buf.duplicate()).addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    System.out.println("success : " + future.isSuccess());
                                    System.out.println("done : " + future.isDone());
                                    System.out.println("cancelled : " + future.isCancelled());
                                    System.out.println("cancellable : " + future.isCancellable());
                                    System.out.println("void : " + future.isVoid());
                                }
                            }).addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    System.out.println("line 43");
                                }
                            });//可不可以多加几个listener呢？

                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            System.out.println(buf.toString(CharsetUtil.UTF_8));
                            ctx.writeAndFlush(Unpooled.copiedBuffer("read some thing \n", CharsetUtil.UTF_8))
                                    .addListener(new ChannelFutureListener() {
                                        @Override
                                        public void operationComplete(ChannelFuture future) throws Exception {
                                            System.out.println("line 57");
                                        }
                                    });
                        }
                    });
                }
            });
        try {
//            ThreadLocal
            boot.option(ChannelOption.AUTO_READ, true);
//            boot.attr(AttributeKey.newInstance(""))
            ChannelFuture cf = boot.bind().sync();
            cf.channel()
                    .closeFuture()
                    .sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }
    }

    static class InboundHandler extends ChannelInboundHandlerAdapter{

        private int id;
        private static AtomicInteger autoId = new AtomicInteger(0);

        public InboundHandler(){
            this.id = autoId.incrementAndGet();
        }

        public InboundHandler(int id){
            Integer i = autoId.get();
            if (id <= i){
                this.id = autoId.incrementAndGet();
            }
            else {
                autoId.set(id);
                this.id = id;
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("inbound-channelactive " + id);
            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("inbound-channelread " + id);
            System.out.println(ctx.handler().toString());
            System.out.println(ctx);
            super.channelRead(ctx, msg);
        }

        @Override
        public String toString() {
            return "InboundHandler{" +
                    "id=" + id +
                    '}';
        }
    }

    static class OutboundHandler extends ChannelOutboundHandlerAdapter{

        private int id;
        private static AtomicInteger autoId = new AtomicInteger(0);

        public OutboundHandler(){
            this.id = autoId.incrementAndGet();
        }

        public OutboundHandler(int id){
            Integer i = autoId.get();
            if (id <= i){
                this.id = autoId.incrementAndGet();
            }
            else {
                autoId.set(id);
                this.id = id;
            }
        }

        @Override
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
//            ctx.channel().bind()
            ((ServerSocketChannel)ctx.channel()).bind(new InetSocketAddress(12346));
            super.bind(ctx, localAddress, promise);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

            ByteBuf m = (ByteBuf) msg;
//            System.out.println("outbound-write " + id);
//            System.out.println(m.refCnt());
            ctx.writeAndFlush(msg);
            System.out.println(ctx);
            System.out.println(ctx.handler());
            throw new Exception();
//            ctx.pipeline().channel().deregister();
//            ReferenceCountUtil.release(msg);
//            System.out.println(m.refCnt());
        }

//        @Override
//        public void read(ChannelHandlerContext ctx){
//            System.out.println("lin 151 out-read");
//        }

        @Override
        public String toString() {
            return "OutboundHandler{" +
                    "id=" + id +
                    '}';
        }
    }
}
