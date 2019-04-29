package cn.rokeabbey.main;

import cn.rokeabbey.bean.Config;

import java.net.InetSocketAddress;

public interface Server {

    Config getConfig();

    InetSocketAddress getRemoteAddress();
}
