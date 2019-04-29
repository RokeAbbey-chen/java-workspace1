package cn.rokeabbey.bean;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.channels.SocketChannel;

public class Socks5Channel extends NioSocketChannel{

    public static final int STAGE_1 = 1;
    public static final int STAGE_2 = 2;
    public static final int STAGE_3 = 3;

    private int stage = STAGE_1;
    private Channel remoteChannel;

    public Socks5Channel(Channel parent, SocketChannel socket ) {
        super(parent, socket);
    }

    public int getStage() {
        return stage;
    }

    public void updateStage() {
        this.stage ++;
    }

    public Channel getRemoteChannel() {
        return remoteChannel;
    }

    public void setRemoteChannel(Channel remoteChannel) {
        this.remoteChannel = remoteChannel;
    }
}
