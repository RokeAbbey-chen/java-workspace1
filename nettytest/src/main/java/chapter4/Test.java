package chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.NettyRuntime;

public class Test {

    @org.junit.Test
    public void test1(){
        ByteBuf buf = Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8);
        assert buf.refCnt() == 1;
        buf.retain();
        assert buf.refCnt() == 2;
        ByteBuf sliced = buf.slice(1, 3);
        ByteBuf copied = buf.copy();


        assert sliced.refCnt() == 2;
        sliced.release();
        assert sliced.refCnt() == 1;
        assert buf.refCnt() == 1;

        sliced.release();
        assert buf.refCnt() == 0;

        assert copied.refCnt() == 1;
        System.out.println(buf.toString(CharsetUtil.UTF_8));
    }

    @org.junit.Test
    public void test2(){
        new SimpleChannelInboundHandler<ByteBuf>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

            }
        };


        new ChannelOutboundHandlerAdapter();
    }


    @org.junit.Test
    public void test3(){
        System.out.println(NettyRuntime.availableProcessors());
    }
}
