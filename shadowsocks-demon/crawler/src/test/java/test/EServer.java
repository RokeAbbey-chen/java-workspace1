/**
 * 
 */
package test;

/**
 * 用户账号类型
 * 
 * @author Richard
 */
public enum EServer {

    SERVER_1(1, "wss://wsapi1.xingqiu123.com/"),
    SERVER_2(2, "wss://wsapi2.xingqiu123.com/"),
    SERVER_3(3, "wss://wsapi3.xingqiu123.com/"),
    SERVER_4(4, "wss://wsapi4.xingqiu123.com/"),
    SERVER_10(10, "wss://wsapi10.xingqiu123.com/"),

    SERVER__TEST_1(1001, "wss://websocket.xingqiu123.com/"),
    SERVER_TEST_2(1002, "wss://lobby.xingqiu123.com/"),;

    private int code;
    private String url;

    EServer(int code, String url) {
        this.code = code;
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public static EServer getByCode(int code) {
        for (EServer type : EServer.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
