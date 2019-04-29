package test;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import javax.xml.crypto.Data;

public class Test3 {
    public static void main(String[] args) {
        String json1 = "{\"duration\": 1234}";
        json1 = "";
        Gson gson = new Gson();
        DataInfo data = gson.fromJson(json1, DataInfo.class);
        System.out.println(data.duration);
    }
    public static class DataInfo{
        @SerializedName("duration")
        Long duration;
    }
}
