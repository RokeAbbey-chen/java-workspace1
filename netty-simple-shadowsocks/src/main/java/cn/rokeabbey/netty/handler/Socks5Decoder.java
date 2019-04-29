package cn.rokeabbey.netty.handler;

import cn.rokeabbey.bean.Config;
import cn.rokeabbey.bean.Socks5Channel;
import cn.rokeabbey.main.Server;
import cn.rokeabbey.util.Socks5;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.*;

public class Socks5Decoder extends ByteToMessageDecoder{




    private Server server;
    private Config config;

    public Socks5Decoder(Server server){
        this.server = server;

    }

    private Set<Object> supportedMethod = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(0x00))
    );

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!(ctx.channel() instanceof  Socks5Channel)){ return; }

        Socks5Channel channel = (Socks5Channel) ctx.channel();
        int stage = channel.getStage();
        switch (stage){
            case Socks5Channel.STAGE_1: resolveHeader(ctx, in);break;
            case Socks5Channel.STAGE_2: resolveCommand(ctx, in);break;
            case Socks5Channel.STAGE_3: resolveConnect(ctx, in);break;
            default: break;
        }
    }

    private void resolveHeader(ChannelHandlerContext ctx, ByteBuf in){

        int length = in.readableBytes();
        if (length >= 3 && supportedMethod.size() > 0) {

            byte socksVersion = in.readByte();
            byte methodCount = in.readByte();

            if (socksVersion != 5 && methodCount <= 0){ return ; }

            byte[] methods = new byte[length - 2];
            in.readBytes(methods);
            for (byte m : methods){
                if (supportedMethod.contains(m)){
                    ctx.writeAndFlush((short)((0x05 << 8) + m));
                    return ;
                }
            }
        }
    }

    private void resolveCommand(ChannelHandlerContext ctx, ByteBuf in){
        if (in.readableBytes() <= 6
                || ctx.channel() instanceof Socks5Channel == false){ return ; }

        Socks5Channel channel = (Socks5Channel) ctx.channel();

        byte version = in.readByte();

        if (!Socks5.checkVersion(version)){ return ; }

        byte command = in.readByte();
        in.readByte();

        switch (command){
            case Socks5.CONNECT: resolveConnect(ctx, in);break;
            case Socks5.BIND:
            case Socks5.UDP_ASSOCIATE:
            default: break;
        }


    }


    private void resolveConnect(final ChannelHandlerContext ctx, ByteBuf in){
        byte type = in.readByte();
        InetSocketAddress address = null;
        if (ctx.channel() instanceof Socks5Channel == false){
            return ;
        }
        Socks5Channel channel = (Socks5Channel) ctx.channel();
        switch (type){
            case Socks5.IPV4:
                ByteBuf outBuf = in.slice();
                byte[] ip = new byte[4];
                in.readBytes(ip);

                short port = 0;
                port = in.readByte();
                port <<=8;
                port += in.readByte();

                address = new InetSocketAddress(ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3], port);
                System.out.println("connect address = " + address.toString());

                ByteBuf buf = channel.alloc().buffer();
                buf.writeBytes(new byte[]{0x05, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x10, 0x10});
                ctx.writeAndFlush(buf);
                break;

            case Socks5.DOMAIN:
            case Socks5.IPV6:
            default:
                break;
        }

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
            .remoteAddress(server.getRemoteAddress())
            .handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {


                }
            });

        channel.setRemoteChannel(bootstrap.connect().channel());
        System.out.println("start connect to " + address);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2)).contains(2));
    }

}
