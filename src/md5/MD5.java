package md5;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author http://www.syboos.jp
 *
 */
public class MD5 {
    /**
     * MD5アルゴリズム
     * 文字列を暗号化する
     *
     * @param str 暗号化される文字列
     * @return 暗号化結果
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(final String str) throws NoSuchAlgorithmException
    {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] hash = md.digest();

        return hashByte2MD5(hash);
    }

    /**
     * MD5アルゴリズム
     * byte[]配列を暗号化する
     *
     * @param bytes 暗号化されるbyte[]配列
     * @return 暗号化結果
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(final byte [] bytes) throws NoSuchAlgorithmException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("bytes to encript cannot be null or zero length");
        }
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(bytes);

        return hashByte2MD5(hash);
    }

    /**
     * MD5アルゴリズム
     * InputStreamを暗号化する（ファイルなどのチェックサムを求む）
     *
     * @param in 暗号化されるInputStreamオブジェクト
     * @return 暗号化結果
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(final InputStream in) throws NoSuchAlgorithmException, IOException {
        if (in == null || in.available() == 0) {
            throw new IllegalArgumentException("InputStream can't be null or zero length.");
        }

        MessageDigest digest = MessageDigest.getInstance("MD5");
        try {
            byte[] buff = new byte[4096];
            int len = 0;
            while ((len = in.read(buff, 0, buff.length)) >= 0) {
                digest.update(buff, 0, len);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        byte[] hash = digest.digest();

        return hashByte2MD5(hash);
    }

    //MD5　ハッシュ関数
    private static String hashByte2MD5(final byte []hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }

        return hexString.toString();
    }
}