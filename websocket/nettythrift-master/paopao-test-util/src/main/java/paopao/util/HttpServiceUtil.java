/**
* 
*/
package paopao.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.ConnectException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Richard
 */
public abstract class HttpServiceUtil {

    private static final RequestConfig requestConfig;

    static {
        requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000).build();
    }

    public static String doGet(String url, Map<String, String> para) throws Exception {
        return doGet(url, para, null);
    }

    public static String doGet(String url, Map<String, String> para, String orgLoc) throws Exception {
        String responseContent = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;

        try {
            client = HttpClientBuilder.create().build();
            httpGet = getGetMethod(url, para);
            httpGet.setConfig(requestConfig);

            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
            } catch (Exception e) {
            }
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
            }
        }
        return responseContent;
    }


    public static String doPost(String url, Map<String, String> para) throws ConnectException, Exception {
        return doPost(url, para, null);
    }

    public static String doPost(String url, Map<String, String> para, String orgLoc) throws Exception {
        CloseableHttpClient client = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String responseContent = null;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : para.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, para.get(key)));
        }
        try {
            client = HttpClientBuilder.create().build();
            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpPost.setConfig(requestConfig);

            response = client.execute(httpPost);
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (HttpHostConnectException e) {
            throw new HttpHostConnectException(null, e);
        } catch (Exception e) {
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
            } catch (Exception e) {
            }
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
            }
        }

        return responseContent;
    }


    public static String sha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data.getBytes());
        StringBuffer buf = new StringBuffer();
        byte[] bits = md.digest();
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) {
                a += 256;
            }
            if (a < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(a));
        }
        return buf.toString();
    }

    public static class Certificate {

        public enum KeyStoreType {
            PKCS12("PKCS12");// 微信支付使用

            private String type;

            KeyStoreType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }
        }

        String certPath;
        KeyStoreType keyStoreType;
        String password;

        public Certificate(String certPath, KeyStoreType keyStoreType, String password) {
            this.certPath = certPath;
            this.keyStoreType = keyStoreType;
            this.password = password;
        }

        public String getCertPath() {
            return certPath;
        }

        public void setCertPath(String certPath) {
            this.certPath = certPath;
        }

        public KeyStoreType getKeyStoreType() {
            return keyStoreType;
        }

        public void setKeyStoreType(KeyStoreType keyStoreType) {
            this.keyStoreType = keyStoreType;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    protected static HttpGet getGetMethod(String url, Map<String, String> params) {
        if (params != null) {
            StringBuffer queryString = new StringBuffer();
            if (!url.contains("?")) {
                queryString.append("?");
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (queryString.length() != 1) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += queryString.toString();
        }
        return new HttpGet(url);
    }



}
