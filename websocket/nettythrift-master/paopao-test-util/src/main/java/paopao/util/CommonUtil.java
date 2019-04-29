package paopao.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author admin
 */
public class CommonUtil {

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f" };
    private static final String HEX_DIGITS = "0123456789abcdef";

    public static String encrypt(String keyStr, String plainText) {
        byte[] encrypt = null;
        try {
            SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(Constants.AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encoded = Base64.encodeBase64(plainText.getBytes());
            encrypt = cipher.doFinal(encoded);
        } catch (Exception e) {
            encrypt = new byte[0];
        }
        return new String(Base64.encodeBase64(encrypt));
    }

    public static String decrypt(String keyStr, String encryptData) throws Exception {
        byte[] decrypt = null;
        SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(Constants.AES_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
        return new String(Base64.decodeBase64(decrypt));
    }

}
