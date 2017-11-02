//import com.sun.org.apache.xpath.internal.operations.String;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class HttpUrlConnectionUtil {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String name = "你好";
        System.out.println(new String(name.getBytes("utf-8"), "utf-8"));
        String url = "www.baidu.com";
        System.out.println(httpRequest2String(url, null, "get"));
    }
    public static String httpRequest2String(String url, Map<String, String> parameters, String method){
        boolean isPost = method.equalsIgnoreCase("POST");
        String params = "?";
        if(parameters != null) {
            Set<Map.Entry<String, String>> set = parameters.entrySet();
            for (Map.Entry<String, String> entry : set)
                params += entry.getKey() + "=" + entry.getValue() + "&";
        }
        params = params.substring(0, params.length() - 1);
        if(!isPost)
            url += params;
        HttpURLConnection conn = null;
        if(!url.startsWith("http://"))
            url = "http://" + url;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("content-type", "text/plain");
            conn.setRequestMethod((isPost? "post": "get").toUpperCase());
            if(isPost) {
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                BufferedOutputStream outputStream = new BufferedOutputStream(conn.getOutputStream());
                outputStream.write(params.getBytes("utf-8"));
                outputStream.flush();
                outputStream.close();
            }
            conn.setDoInput(true);
            BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
            byte[] bytes = new byte[1024];
            StringBuffer sb = new StringBuffer();
            int size = -1;
            while((size = inputStream.read(bytes)) != -1)
                sb.append(new String(bytes, 0, size, "utf-8"));
            inputStream.close();
            return sb.toString();
//            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }

        return null;
    }
}
