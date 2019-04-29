package bean;

import com.alibaba.fastjson.JSONObject;
import util.HttpUtil;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Crawler {


    private Pattern crawlPattern ;
    public void crawl() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("appName", "成语");
        map.put("page", 3 + "");
        String jsonStr = HttpUtil.doPost("http://zhishuapi.aldwx.com/Main/action/Search/Search/search", map);
        JSONObject json = (JSONObject) JSONObject.parse(jsonStr);
        System.out.println(json.toString());
    }


    public static void main(String[] args) throws Exception {
        Crawler c = new Crawler();
        c.crawl();
    }
}
