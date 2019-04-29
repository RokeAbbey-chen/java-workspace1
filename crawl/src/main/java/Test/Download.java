package Test;

import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.net.InetSocketAddress;

public class Download {

    public static void main(String[] args) throws InterruptedException, IOException {
        Client c = new Client(new InetSocketAddress("wx.qlogo.cn", 443), "/correct.dat");
        try{
            c.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class Client{

        private Bootstrap bootstrap;
        private BufferedReader reader;
        private String curUser;
        private boolean readerClose = false;

        private Bootstrap uploadToQiniu;
        private Channel uploadChannel;
        private String qiniuAk = "4OgXtlFltHNWD85tRJYOZEDJ2AVdB6KyJdwUMmaI";
        private String qiniuSk = "CqEiXE4xJ8jd0WTpm7P2r62Hg9keMSO8vwMTfO0F";
        private Auth auth = Auth.create(qiniuAk, qiniuSk);
        private String bucketName = "paopao"; // 空间名
        private String storeName = "robot/pic"; // 上传空间的图片名前缀,还有一部分为id号
        private String qiniuUrl = "http://upload.qiniup.com/putb64/";// + l+"/key/"+ UrlSafeBase64.encodeToString(key);
        private InetSocketAddress qiniuU = new InetSocketAddress("upload.qiniup.com", 80);


        public Client(InetSocketAddress address, String fileName){
            this.reader = new BufferedReader(new InputStreamReader(Download.class.getResourceAsStream(fileName)));
            init(address);
        }

        private void init(InetSocketAddress address){
            bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(address)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new SslHandler(SslContextBuilder.forClient().build().newEngine(ch.alloc())))
                            .addLast(new HttpClientCodec())
                            .addLast(new HttpObjectAggregator(1024 * 1024))
                            .addLast(new ChannelInboundHandlerAdapter(){

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object message) throws IOException, InterruptedException {
                                    if (message instanceof FullHttpResponse){
                                        Channel ch = ctx.channel();
                                        FullHttpResponse msg = (FullHttpResponse) message;

                                        {
                                            // 将图片写出到文件
                                           byte[] bytes = new byte[msg.content().readableBytes()];
                                           msg.content().readBytes(bytes);
                                           try(BufferedOutputStream output =
                                                       new BufferedOutputStream(
                                                               new FileOutputStream(
                                                                       new File("F:\\MyWorks\\company\\robotpic\\" + curUser)))){
                                               output.write(bytes);
                                           }

                                        }

                                        doRequest(ch); // 先注释 暂时只访问一张看看
                                        msg.release();
                                    }
                                }
                            });

                    }
                });


            uploadToQiniu = new Bootstrap().
                    group(new NioEventLoopGroup()).
                    remoteAddress(qiniuU).
                    channel(NioSocketChannel.class).
                    handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                            ch.pipeline().addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024))
                                .addLast(new ChannelInboundHandlerAdapter(){

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object m){

                                        if (m instanceof FullHttpResponse){
                                            FullHttpResponse response = (FullHttpResponse) m;
                                            System.out.println("response from qiniu:****************************************");
                                            System.out.println(response.content().toString(CharsetUtil.UTF_8));
                                            response.release();
                                        }

                                        else {
                                            System.out.println(" upload msg :*********************\n" + m);
                                        }

                                    }
                                });

                        }
                    });
        }

        private String getUpToken(){
            return auth.uploadToken(bucketName, null,
                    3600, new StringMap().put("insertOnly", 1));
        }

        public void start() throws InterruptedException {
            startDownload();
            startUpload();
        }

        public void restartDownload() throws InterruptedException {
            System.out.println("restartDownload");
            startDownload();
        }

        public void startDownload() throws InterruptedException {
            bootstrap.connect()
                    .sync().addListener((ChannelFutureListener) future -> {
                        doRequest(future.channel());
                        future.channel().closeFuture().addListener((ChannelFutureListener) future1 -> {
                            restartDownload();
                        });
                    });
        }

        public void restartUpload() throws InterruptedException {
            System.out.println("restartUpload");
            startUpload();
        }
        public void startUpload() throws InterruptedException {
            uploadToQiniu.connect()
                    .sync().addListener((ChannelFutureListener) futurn -> {
                        uploadChannel = futurn.channel();
            });
        }

        private void doRequest(Channel ch) throws IOException, InterruptedException {
            String line = reader.readLine();
            if (line != null){
                curUser = line.substring(0, 5);
                String uri = line.substring(6);
//                while (uploadChannel == null) {
//                    System.out.println("uploadChannel " + null + ", sleep one second");
//                    Thread.sleep(1000);
//                }
                doRequest(uri, ch);
            }
            else {
                reader.close();
                readerClose = true;
            }
        }

        private void doRequest(String uri, Channel ch){
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.set(HttpHeaderNames.HOST, "wx.qlogo.cn");
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, uri,
                    Unpooled.EMPTY_BUFFER, headers, headers);
            ch.writeAndFlush(request);
        }

        private void doUpload(String url, byte[] bytes, Channel ch) throws HttpPostRequestEncoder.ErrorDataEncoderException {
            HttpHeaders headers = new DefaultHttpHeaders();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
            headers.set(HttpHeaderNames.AUTHORIZATION, "UpToken " + getUpToken());

            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1,  HttpMethod.POST, url);
            HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
            HttpPostRequestEncoder bodyRequestEncoder = new HttpPostRequestEncoder(factory, request, false);
//            bodyRequestEncoder.add



//            FullHttpRequest request = new DefaultFullHttpRequest(
//                    HttpVersion.HTTP_1_1,  HttpMethod.POST, url ,
//                    Unpooled.wrappedBuffer(bytes), headers, headers);
            System.out.println("*************************upload headers **************");
            System.out.println();
            System.out.println("*****************************************************");
            ch.writeAndFlush(request);
        }
    }


}
