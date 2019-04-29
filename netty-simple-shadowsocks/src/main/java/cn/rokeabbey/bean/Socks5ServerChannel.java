package cn.rokeabbey.bean;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SocketUtils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Socks5ServerChannel extends NioServerSocketChannel{

    @Override
    public int doReadMessages(List<Object> buf) throws IOException {
        SocketChannel channel = SocketUtils.accept(javaChannel());
        try {
            buf.add(new Socks5Channel(this, channel));
            return 1;
        } catch (Throwable t) {
            System.out.println("Failed to create a new channel from an accepted socket." + t);

            try {
                channel.close();
            } catch (Throwable t2) {
                System.out.println("Failed to close a socket." + t2);
            }
        }
        return 0;
    }
}
