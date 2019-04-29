package cn.rokeabbey.netty.handler;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EnCryptorTest {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    //加密
    public static byte[] AES_cbc_encrypt(byte[] srcData,byte[] key,byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] encData = cipher.doFinal(srcData);
        return encData;
    }

    //解密
    public static byte[] AES_cbc_decrypt(byte[] encData,byte[] key,byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] decbbdt = cipher.doFinal(encData);
        return decbbdt;
    }


    public static void main(String[] args) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        byte[] key= new byte[32];
        byte[] iv = new byte[16];

        String srcStr = "This is java default pkcs5padding PKCS5 TEST";
        System.out.println(srcStr);

        //设置key 全8，iv，全1，这里测试用
        for (int i = 0; i <32; i++) {
            key[i] = 8;
            if (i < 16) {iv[i] = 1;}
        }


        byte[] encbt = AES_cbc_encrypt(srcStr.getBytes(),key,iv);
        byte[] decbt = AES_cbc_decrypt(encbt,key,iv);
        String decStr = new String(decbt);
        System.out.println(decStr);

        if(srcStr.equals(decStr))
        {
            System.out.println("TEST PASS");
        }else
        {
            System.out.println("TEST NO PASS");
        }

    }
}
