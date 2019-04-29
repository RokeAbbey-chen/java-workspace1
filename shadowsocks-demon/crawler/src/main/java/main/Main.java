package main;

import bean.Crawler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Pattern p = Pattern.compile("\"objURL\":\"(http:.*?)\"");

        Crawler c = new Crawler();
        c.setOutputDir(new File("F:\\MyWorks\\workspace\\javaworkspace\\shadowsocks-demon\\crawler\\img"));
        c.crawlStringURL(Arrays.asList(
//                "https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=%C3%C0%C5%AE&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111"
//                ,
                "https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=%BD%DB%B9%A3&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111"
                     )
                , new Crawler.DefaultFilter(p));

        System.out.println("finish");
    }

}
