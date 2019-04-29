package test;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Calendar;

public class Upload {

    public static void main(String[] args) throws InterruptedException {
        Client c = new Client("upload-z0.qiniup.com", 443);
        c.start();
    }

    private static class Client{

        private Bootstrap bootstrap;
        String ak = "4OgXtlFltHNWD85tRJYOZEDJ2AVdB6KyJdwUMmaI";
        String sk = "CqEiXE4xJ8jd0WTpm7P2r62Hg9keMSO8vwMTfO0F";    // 密钥配置
        Auth auth = Auth.create(ak, sk);    // TODO Auto-generated constructor stub
        String bucketname = "paopao";    //空间名

        public Client(String host, int port){
            this.init(host, port);
        }

        public String getUpToken() {
            return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
        }

        private void init(String host, int port){
            InetSocketAddress addr = new InetSocketAddress(host, port);
            bootstrap = new Bootstrap()
                    .remoteAddress(addr)
                    .channel(NioSocketChannel.class)
                    .group(new NioEventLoopGroup())
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new SslHandler(SslContextBuilder.forClient().build().newEngine(ch.alloc())))
                                .addLast(new HttpResponseDecoder())
                                .addLast(new HttpObjectAggregator(1024 * 1024))
                                .addLast(new MyHttpUploadFileResultHandler());

//                            HttpRequestEncoder h;
//                            FullHttpRequest f;
//                            DefaultFullHttpRequest r;
//                            HttpObjectAggregator;
//                            HttpRequestDecoder


                        }
                    });
        }

        private void start() throws InterruptedException {
            bootstrap.connect()
                    .sync().addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Channel ch = future.channel();
                    StringBuilder head = new StringBuilder();
                    StringBuilder body1 = new StringBuilder();
                    StringBuilder body2 = new StringBuilder();
                    head.append(
                            "POST / HTTP/1.1\r\n" +
                            "Host: upload-z0.qiniup.com\r\n" +
                            "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0\r\n" +
                            "Accept: */*\r\n" +
                            "Accept-Language: en-US,en;q=0.5\r\n" +
                            "Accept-Encoding: gzip, deflate, br\r\n" +
                            "Referer: https://portal.qiniu.com/bucket/paopao/resource\r\n" +
                            "Content-Type: multipart/form-data; boundary=---------------------------265001916915724\r\n" +
                            "Content-Length: %d\r\n" +
                            "Origin: https://portal.qiniu.com\r\n" +
                            "Connection: keep-alive\r\n");

                    body1.append("-----------------------------265001916915724\r\n" +
                            "Content-Disposition: form-data; name=\"file\"; filename=\"niubi.txt\"\r\n" +
                            "Content-Type: application/octet-stream\r\n");

                    body1.append("\r\n");

                    body2.append("-----------------------------265001916915724\r\n" +
                            "Content-Disposition: form-data; name=\"token\"\r\n" +
                            "\r\n" +
                            getUpToken() +
                            "\r\n" +
                            "-----------------------------265001916915724\r\n" +
                            "Content-Disposition: form-data; name=\"key\"\r\n" +
                            "\r\n" +
                            "niubi.txt\r\n" +
                            "-----------------------------265001916915724\r\n" +
                            "Content-Disposition: form-data; name=\"fname\"\r\n" +
                            "\r\n" +
                            "niubi.txt\r\n" +
                            "-----------------------------265001916915724--\r\n");

                    ByteBuf byteBuf = ch.alloc().buffer();
                    String h;
                    try(InputStream input = Upload.class.getResourceAsStream("/niubi.txt")) {

                        byteBuf.writeBytes(
                                (h = String.format(head.toString() , body1.length() + body2.length() + input.available()
                                )).getBytes());
                        byteBuf.writeBytes(head.toString().getBytes());
                        byteBuf.writeBytes(body1.toString().getBytes());
                        byteBuf.writeBytes(input, input.available());
                        byteBuf.writeBytes(body2.toString().getBytes());
                    }
                    System.out.println(h + body1.toString() + body2.toString());
                    ch.writeAndFlush(byteBuf);
                }
            });
        }

        private static class MyHttpUploadFileResultHandler extends ChannelInboundHandlerAdapter {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object m){
                System.out.println("--------- response ---------- ");
                if (m instanceof FullHttpResponse){
                    System.out.println(((FullHttpResponse) m).content().toString(CharsetUtil.UTF_8));
                }
                else {
                    System.out.println("unknown message : " + m);
                }
            }

        }
    }
}
