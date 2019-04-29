package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Test1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        final Server s = new Server(12345);
//        new Thread(){
//            @Override
//            public void run() {
//                for (int i = 0; i < 1000; i ++){
//                    try {
//                        Thread.sleep(3000);
//                        s.selector.wakeup();
//                        s.selector.wakeup();
//                        s.selector.selectNow();
//                        s.selector.wakeup();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
        s.start();
    }

    private static class Server{

        private int port;
        private Selector selector;
        public Server(int port){

            this.port = port;
        }

        public void start() throws IOException, InterruptedException {

            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind( new InetSocketAddress(port), 100);
            ssc.configureBlocking(false);
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                int count = selector.selectNow();
                System.out.println("count = " + count);
                if (count == 0){
                    System.out.println("wakeup by selector.wakeup()");
                    Thread.sleep(1000);
                    continue;
                }

                Iterator<SelectionKey> it =  selector.selectedKeys().iterator();
                while (it.hasNext()){
                    SelectionKey k = it.next();
                    if (k.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) k.channel();
                        SocketChannel socketChannel = server.accept();
                        if (socketChannel != null) {
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                    }

                    if (k.isReadable()){
                        ByteBuffer bb = ByteBuffer.allocate(1024);
                        SocketChannel socketChannel = (SocketChannel) k.channel();
                        socketChannel.read(bb);
                        byte[] b = new byte[1024];
                        bb.flip();
                        bb.get(b, 0, Math.min(b.length, bb.remaining()));
                        System.out.println(new String(b));
                    }
                    it.remove();
                }
                ssc.close();
            }

        }
    }
}


