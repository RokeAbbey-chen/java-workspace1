package cn.rokeabbey.main;

import cn.rokeabbey.bean.Config;
import cn.rokeabbey.bean.Socks5Channel;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class LocalServer implements Server{

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    private ServerBootstrap bootstrap;

    private Config config;

    public LocalServer(Config config){
        init(config);
        this.config = config;
    }

    private void init(Config config){

        this.localAddress = config.getLocalAddress();

        this.remoteAddress = config.getRemoteAddress();


    }

    private void initBootstrap(){
        this.bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup())
                .childHandler(new ChannelInitializer<Socks5Channel>() {
                    @Override
                    protected void initChannel(Socks5Channel ch) throws Exception {

                    }
                });
    }

    public void bind(){
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }
}
