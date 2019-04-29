package cn.rokeabbey.netty.handler;

import cn.rokeabbey.bean.Socks5Channel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Socks5Encoder extends MessageToByteEncoder<Object>{


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (ctx.channel() instanceof Socks5Channel == false) { return; }

        Socks5Channel channel = (Socks5Channel) ctx.channel();

        if (msg instanceof Short){
            out.writeShort((short)msg);
        }

        channel.updateStage();
    }
}
