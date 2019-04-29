import org.junit.Test;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class KeyStoreTest {

    private String toHex(byte[] nums, String blank){
        StringBuilder sb = new StringBuilder();
        for (short b : nums){
            sb.append(Integer.toHexString(b < 0 ? b + 128 : b));
            sb.append(blank);
        }
        return sb.toString();
    }

    private byte[] sign(PrivateKey prk, String plainText) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(prk);
        sig.update(plainText.getBytes());
        return sig.sign();
    }

    private boolean verify(PublicKey puk, byte[] sigText, String plainText) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(puk);
        sig.update(plainText.getBytes());
        return sig.verify(sigText);
    }

    @Test
    public void test1(){
        try {
            String storePass = "123456";
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("F:/MyWorks/秘钥/roke.keystore"), storePass.toCharArray());
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("roke", new KeyStore.PasswordProtection("rokeabbey".toCharArray()));
            Certificate crt = entry.getCertificate();
            PrivateKey pK = entry.getPrivateKey();
            PublicKey puK = crt.getPublicKey();
            byte[] signedBytes = sign(pK, "123456");
            System.out.println(new String(signedBytes) + " line 49");

            System.out.println(verify(puK, signedBytes, "123456"));
            SSLContext ss = null;
//            ss.init();
//            ss.createSSLEngine("", 1);

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }
}
