package cn.rokeabbey.netty.handler;

import cn.rokeabbey.bean.Config;
import cn.rokeabbey.main.LocalServer;
import cn.rokeabbey.main.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class EnCryptor extends ChannelOutboundHandlerAdapter{

    private KeyGenerator keyg = KeyGenerator.getInstance("AES");

    private Server server;

    private byte[] password;

    private Cipher cipher;

    public EnCryptor(Server server) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, NoSuchProviderException {

        this.server = server;
        this.password = server.getConfig().getPassword();
        keyg.init(256, new SecureRandom(this.password));
        SecretKey skey = keyg.generateKey();
        byte[] skeyBytes = skey.getEncoded();
        System.out.println(new String(skeyBytes));
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,skey);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object out, ChannelPromise promise) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        byte[] content = null;
        if (out instanceof String){
            content = ((String) out).getBytes("utf-8");
        }

        if (out instanceof byte[]){
            content = (byte[]) out;
        }

        if (out instanceof ByteBuf){
            ByteBuf buf = (ByteBuf) out;
            buf.readBytes(content = new byte[buf.readableBytes()]);
        }

        ctx.writeAndFlush(Unpooled.copiedBuffer(cipher.doFinal(content)));

    }


    public byte[] encrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        return cipher.doFinal(bytes);
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        EnCryptor cryptor = new EnCryptor(new LocalServer(new Config(){

            @Override
            public byte[] getPassword() throws UnsupportedEncodingException {
                return "123456789".getBytes("utf-8");
            }
        }));

        StringBuilder sb = new StringBuilder();
        for (byte b : cryptor.encrypt("1234567890".getBytes("utf-8"))){
            sb.append(Integer.toHexString(b));
        }
        System.out.println(sb.toString());
    }
}
