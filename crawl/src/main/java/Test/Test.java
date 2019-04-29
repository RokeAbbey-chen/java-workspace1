package Test;

import java.io.File;
import java.io.FileInputStream;

import com.qiniu.util.*;
import okhttp3.*;
public class Test {
    String ak = "4OgXtlFltHNWD85tRJYOZEDJ2AVdB6KyJdwUMmaI";
    String sk = "CqEiXE4xJ8jd0WTpm7P2r62Hg9keMSO8vwMTfO0F";    // 密钥配置
    Auth auth = Auth.create(ak, sk);    // TODO Auto-generated constructor stub 
    String bucketname = "bocai";    //空间名

    public String getUpToken() {        
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }    
    public void put64image() throws Exception {
        File dir = new File("F:\\MyWorks\\company\\robotpic");
        for (File f : dir.listFiles()) {
            String file = f.getCanonicalPath();//图片路径
            String key = "robot/" + f.getName();    //上传的图片名
            System.out.println("file : " + file + ",  key : " + key);
            FileInputStream fis = null;
            int l = (int) (new File(file).length());
            byte[] src = new byte[l];
            fis = new FileInputStream(new File(file));
            fis.read(src);
            String file64 = Base64.encodeToString(src, 0);
            String url = "http://upload.qiniup.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);
            //非华东空间需要根据注意事项 1 修改上传域名
            RequestBody rb = RequestBody.create(null, file64);
            Request request = new Request.Builder().
                    url(url).
                    addHeader("Content-Type", "application/octet-stream")
                    .addHeader("Authorization", "UpToken " + getUpToken())
                    .post(rb).build();
            System.out.println(request.headers());
            System.out.println(url);
            OkHttpClient client = new OkHttpClient();
            okhttp3.Response response = client.newCall(request).execute();
            System.out.println(response);
        }
    }   
     public static void main(String[] args) throws Exception {   
          new Test().put64image();
    }
}