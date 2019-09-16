/**
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;


import org.unicorn.framework.util.json.JsonUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RAS签名验签类
 * 非对称加密   RSA,Rabin
 *
 * @author xiebin
 */
public class RSA {
    /**
     * 签名算法
     * MD5withRSA \SHA1WithRSA
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "RSA";


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA签名
     *
     * @param content       待签名数据
     * @param privateKey    私钥
     * @param input_charset 编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String input_charset)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            InvalidKeyException,
            UnsupportedEncodingException,
            SignatureException {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);

        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

        signature.initSign(priKey);
        signature.update(content.getBytes(input_charset));

        byte[] signed = signature.sign();

        return Base64Util.encode(signed);
    }

    /**
     * RSA验签名检查
     *
     * @param content       待签名数据
     * @param sign          签名值
     * @param public_key    公钥
     * @param input_charset 编码格式
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String public_key,
                                 String input_charset) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException,
            UnsupportedEncodingException,
            SignatureException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64Util.decode(public_key);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes(input_charset));

        boolean bverify = signature.verify(Base64Util.decode(sign));
        return bverify;
    }

    /**
     * 解密
     *
     * @param content       密文
     * @param private_key   私钥
     * @param input_charset 编码格式
     * @return 解密后的字符串
     */
    public static String decrypt(String content, String private_key, String input_charset)
            throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64Util.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes;

        keyBytes = Base64Util.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }


    /**
     * 初始化RSA公钥私钥
     */
    public static KeyPair initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //密钥长度位1024位，加密的信息是不能被破解的
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }


    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        Cipher cipher = privateKeyChiper(Cipher.DECRYPT_MODE, privateKey);
        return getData(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        Cipher cipher = publicKeyChiper(Cipher.DECRYPT_MODE, publicKey);
        return getData(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        // 对数据加密
        Cipher cipher = publicKeyChiper(Cipher.ENCRYPT_MODE, publicKey);
        return getData(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        Cipher cipher = privateKeyChiper(Cipher.ENCRYPT_MODE, privateKey);
        return getData(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    private static Cipher publicKeyChiper(int mode, String publicKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, publicK, new SecureRandom());
        return cipher;
    }

    private static Cipher privateKeyChiper(int mode, String privateKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, privateK, new SecureRandom());
        return cipher;
    }

    private static byte[] getData(byte[] data, Cipher cipher, int maxLen) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxLen) {
                cache = cipher.doFinal(data, offSet, maxLen);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxLen;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


    public static void main(String[] args) throws Exception {
        Map<String, String> mm = new HashMap<>();
        mm.put("resCode", "0000");
        mm.put("resInfo", "成功");
        mm.put("data", "null");
        String content = "bearer unicorn:0f8aa4df34e34a249db57980c1c8b0882019-09-12 18:20:38{\"pageNo\": 1,\"pageSize\": 10}";
        //初始化密钥对
        KeyPair keyPair = initKey();
        //私钥
        String privateKey = Base64Util.encode(keyPair.getPrivate().getEncoded());
        //公钥
//        String publicKey = Base64Util.encode(keyPair.getPublic().getEncoded());
//        System.out.println("privateKey==" + privateKey);
//        System.out.println("publicKey==" + publicKey);
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBO0VvYNuIHdWzTFRoUD9CN21L4cmSU10516GfnPpKhMQyXKqXoQzOvptFrvDyMd+koqcy7KuFTdgmhjvjzNYCgAJYNx+2yyPvc4TXiKmjpY6GVGkdTETitIVcW88B+Cc+LBiWbNEm/kA6bGXmYTOb7m9R2d0c6CwZGxp0z/n7BwIDAQAB";

        privateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAME7RW9g24gd1bNMVGhQP0I3bUvhyZJTXTnXoZ+c+kqExDJcqpehDM6+m0Wu8PIx36SipzLsq4VN2CaGO+PM1gKAAlg3H7bLI+9zhNeIqaOljoZUaR1MROK0hVxbzwH4Jz4sGJZs0Sb+QDpsZeZhM5vub1HZ3RzoLBkbGnTP+fsHAgMBAAECgYEAvCeB0hVHTwB2ITPXEQfqwQiFpZkDFTeVlIgyeeB6G2uyO7Pd7O3GMd6KBU12ku8bbQ1wr3ajAZeuPL0Cviurymt4lhXXRDDNNzyUSCSAnaj+wf93p3IGj5Qu7GzMXwj5QGwI8iC3tmjACSDteXcxa8uALxiywJiCrAepUZbKp2kCQQDsUgOnyJ/mctQ9y+P5g4+J1xG4bzpjpF/82P29IguNkmXik49z/uNPSJq83uSYsRS9XlWRO1+KFBUv5KzWzmatAkEA0VKrWmTMAzn5SNwWCSNqCMGSoZPkUnLoi3rEjjugq80BcOVfgl0SuFcxSFzdEw/UHkvSN/XQRe1IGDLgMN3DAwJBAJjPp8F0/8C6e/fBwhb0NXsCcVj7w5vvDIqpndoRC7tt8SgEFv0A0ufPoQ+Eafk6eJjDST1yUSMuPU0M5563NLkCQB60HphSwq4SeeNbDQxoGmyQYD69H7eMTVnwNxaZ3nZ0yqpRqtHHbzCE8aCopnDeWIHdI0e8EcHADVwnUkU4shsCQDYSIJz0iYXsr+NL6cv0Q5HrK14v8aYaa4HlgRAro4a6OkyuGr3b+wHhJCddPmaeczvXNqp2qDp3CFUCs1QoJ7M=";
        //签名
        String signStr = sign(content, privateKey, "utf-8");
        System.out.println("签名=="+signStr);
        //验签
        System.out.println(verify(content, signStr, publicKey, "utf-8"));
        //私钥加密
        String chiper = Base64Util.encode(encryptByPrivateKey(content.getBytes(), privateKey));
        System.out.println("私钥加密==" + chiper);
        //公钥解密
        String mingStr = new String(decryptByPublicKey(Base64Util.decode(chiper), publicKey), "utf-8");
        System.out.println("公钥解密==" + mingStr);
        //公钥加密
        String publicKeychiper = Base64Util.encode(encryptByPublicKey(content.getBytes(), publicKey));
        System.out.println("公钥加密==" + publicKeychiper);
        //私钥解密
        String privateKeymingStr = new String(decryptByPrivateKey(Base64Util.decode(publicKeychiper), privateKey), "utf-8");
        System.out.println("私钥解密==" + privateKeymingStr);

    }
}
