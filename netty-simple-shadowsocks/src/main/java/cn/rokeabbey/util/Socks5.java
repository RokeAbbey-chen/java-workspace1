package cn.rokeabbey.util;

public class Socks5 {

    /*
    command
     */
    public static final byte CONNECT = 0x01;
    public static final byte BIND = 0x02;
    public static final byte UDP_ASSOCIATE = 0x03;

    /*
    method
     */
    public static final byte NO_AUTHENTICATION_REQUIRED = 0x00;
    public static final byte GSSAPI = 0x01;
    public static final byte USERNAME_PASSWORD = 0x02;
    public static final byte to_X7F_IANA_ASSIGNED = 0x03;
    public static final byte to_XFE_RESERVED_FOR_PRIVATE_METHODS = (byte)0x80;
    public static final byte NO_ACCEPTABLE_METHODS = (byte)0xFF;


    /*
    * version
    * */


    /*
    *
    * requestType
    *
    * */

    public static final byte IPV4 = 0x01;
    public static final byte DOMAIN = 0x03;
    public static final byte IPV6 = 0x04;

    public static boolean checkVersion(byte version){
        return version == 5;
    }
}
