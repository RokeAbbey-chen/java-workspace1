package chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.net.InetSocketAddress;
import java.util.List;

public class List10_1 {

    public static void main(String[] args) {
        new Server(12345).start();
    }

    private static class Server{
        private int port;
        private Server(int port){
            this.port = port;
        }

        public void start(){

            NioEventLoopGroup g = new NioEventLoopGroup();
            ServerBootstrap boot = new ServerBootstrap()
                    .group(g)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ToIntegerDecoder2())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg){
                                        System.out.println(msg);

                                    }
                                });
                        }
                    });
            try {
                boot.bind().sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private static class ToIntegerDecoder extends ByteToMessageDecoder {

            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

                while (in.readableBytes() >= 4){
                    out.add(in.readInt());//out中的每一个元素将会以Integer的形式作为msg(注意并非bytebuf)传入下一个inbound
                }
            }

            @Override
            protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){

            }
        }

        private static class ToIntegerDecoder2 extends ReplayingDecoder<Void>{

            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                for (int i = 0; i < 5; i ++){
                    System.out.println("decoder2 : " + in.readInt());
                }
            }
        }
        private static class ShowIntegerDecoder extends ByteToMessageDecoder{

            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

            }
        }


    }
}
