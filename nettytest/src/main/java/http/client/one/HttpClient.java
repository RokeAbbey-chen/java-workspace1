package http.client.one;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    private Bootstrap boot;
    public static void main(String[] args) throws InterruptedException {
        new HttpClient("127.0.0.1", 12345).start();
    }

    public HttpClient(String addr, int port) throws InterruptedException {
        this(new InetSocketAddress(addr, port));
    }

    public HttpClient(InetSocketAddress addr) throws InterruptedException {
        boot = new Bootstrap();
        boot.group(new NioEventLoopGroup())
                .remoteAddress(addr)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpRequestEncoder())
                                .addLast(new HttpResponseDecoder())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object m){
                                        System.out.println(m);
                                        if ( m instanceof HttpResponse ){
                                            HttpResponse msg = (HttpResponse) m;
                                            Iterator<Map.Entry<String, String>> it = msg.headers().iteratorAsString();
                                            while(it.hasNext()){
                                                Map.Entry<String, String> entry = it.next();
                                                System.out.println(entry.getKey() + " : " + entry.getValue());
                                            }
                                        }
                                    }
                                });
                    }
                });

    }

    public void start() throws InterruptedException {
        boot.connect().sync().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                HttpHeaders headers = new DefaultHttpHeaders();
                headers.add("host", "127.0.0.1:12345")
                    .add("Content-type", "multipart/form-data; boundary=------------------------0835545d257490d1")
                    .add("content-length", 100);

                DefaultFullHttpRequest req
                        = new DefaultFullHttpRequest(
                        HttpVersion.HTTP_1_1,
                        HttpMethod.POST,
                        "127.0.0.1:12345/aaaa");
                HttpDataFactory factory = new DefaultHttpDataFactory();
                HttpPostRequestEncoder encoder = new HttpPostRequestEncoder(factory, req, true);
                encoder.addBodyFileUpload("upFile",
                        new File("F:\\MyWorks\\workspace\\javaworkspace\\nettytest\\src\\main\\resources\\niubi.txt"),
                        "multipart/form-data;",
                        true );
                req.headers().add(headers);
                HttpRequest r = encoder.finalizeRequest();
                future.channel().writeAndFlush(r);
            }
        });
    }

}
