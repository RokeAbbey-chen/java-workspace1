package cn.rokeabbey.bean;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

public class Config {

    InetSocketAddress localAddress;

    InetSocketAddress remoteAddress;

    byte[] password;

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {

        return localAddress;
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public byte[] getPassword() throws UnsupportedEncodingException {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
