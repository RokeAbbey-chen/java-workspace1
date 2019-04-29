package chapter4;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class List4_2 {

    @Test
    public void test1(){
        PlainNIOServer server = new PlainNIOServer();
        try {
            server.serve(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class PlainNIOServer {
        public void serve(int port) throws IOException {//在对方连接上以后直接ctrl+d会陷入readable的死循环
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while(true){
                if (selector.select() == 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    System.out.println("acc : " + key.isAcceptable() + ", w : " + key.isWritable()
                            + ", r : " + key.isReadable() + ", conn : " + key.isConnectable() + ", v : " + key.isValid());
                    try {
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            if (client != null) {
                                client.configureBlocking(false);
                                client.register(selector, SelectionKey.OP_READ);
                                System.out.println("Accept Connection from " + client);
                            }
                        }
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(5);
                            System.out.println("readable !!!");
                            int count = 0;
                            while (( count = client.read(byteBuffer)) > 0 ) {
                                byteBuffer.flip();
                                byte[] bytes = new byte[1024];
                                while (byteBuffer.hasRemaining()) {
                                    byteBuffer.get(bytes, 0, Math.min(byteBuffer.remaining(), bytes.length));
                                    String sentence;
                                    System.out.println(sentence = new String(bytes, 0, count));
                                    byteBuffer.compact();
                                    byteBuffer.put(sentence.getBytes());
                                    byteBuffer.flip();
                                    client.write(byteBuffer);
                                }
                                byteBuffer.compact();
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        key.channel().close();
                    }
                }
            }
        }
    }
}
