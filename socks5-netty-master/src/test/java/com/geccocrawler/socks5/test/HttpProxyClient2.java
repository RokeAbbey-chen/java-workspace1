package com.geccocrawler.socks5.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class HttpProxyClient2 {

    public static void main(String[] args) {

        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1081));
            URL url = new URL("https://www.baidu.com");
            java.net.Authenticator.setDefault(new Authenticator() {

                private PasswordAuthentication pass = new PasswordAuthentication("test", "pghztAkzx3".toCharArray());
                @Override
                public PasswordAuthentication getPasswordAuthentication(){
                    return pass;
                }
            });
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while (( line = r.readLine()) != null){
                System.out.println(line);
            }
            r.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
