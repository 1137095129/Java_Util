package com.wjf.github.sec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class KeyTest {
	/**
	 * 指定加密算法为RSA
	 */
	private static String ALGORITHM = "RSA";
	/**
	 * 指定key的大小
	 */
	private static int KEY_SIZE = 1024;
	/**
	 * 指定公钥存放文件
	 */
	private static String PUBLIC_KEY_FILE = "PublicKey";
	/**
	 * 指定私钥存放文件
	 */
	private static String PRIVATE_KEY_FILE = "PrivateKey";

	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/**
	 * 生成密钥对
	 */
	public static KeyPair generateKeyPair() throws Exception {
/** RSA算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		kpg.initialize(KEY_SIZE, sr);
/** 生成密匙对 */
		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}


	/**
	 * 产生签名
	 *
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return encryptBASE64(signature.sign());
	}


	/**
	 * 验证签名
	 *
	 * @param data
	 * @param publicKey
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {

		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}


	/**
	 * BASE64解密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 加密方法 source： 源数据
	 */
	public static String encrypt(String source,Key key) throws Exception {
//generateKeyPair();
/** 将文件中的公钥对象读出 */
//		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
//		Key key = (Key) ois.readObject();
//		ois.close();
/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		int MaxBlockSize = KEY_SIZE / 8;
		String[] datas = splitString(source, MaxBlockSize - 11);
		String mi = "";
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;

	}

	/**
	 *
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 *
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;


		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');


			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 解密算法 cryptograph:密文
	 */
	public static String decrypt(String cryptograph,Key key) throws Exception {
///** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		int key_len = KEY_SIZE / 8;
		byte[] bytes = cryptograph.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			System.out.println(ming);
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}

	/**
	 *
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;


		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 *
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	public static void main(String[] args) throws Exception {
		KeyPair keyPair = generateKeyPair();
		KeyPair pair = generateKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		System.out.println(Arrays.toString(publicKey.getEncoded()));
		RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		System.out.println(rsaPublicKey.getModulus());
		System.out.println(rsaPublicKey.getPublicExponent());
		System.out.println(Arrays.toString(rsaPublicKey.getEncoded()));
		System.out.println(rsaPublicKey.getFormat());
		RSAPublicKey key = new RSAPublicKeyImpl(rsaPublicKey.getModulus(),rsaPublicKey.getPublicExponent());
		String privateKey = encryptBASE64(rsaPrivateKey.getEncoded());
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		System.out.println(Arrays.toString(key.getEncoded()));

		String source ="啊啊啊啊啊啊啊啊啊";// 要加密的字符串
		String s = Base64.getEncoder().encodeToString(source.getBytes());

		String cryptograph = encrypt(s,key);// 生成的密文
		System.out.println(cryptograph);
		String target = decrypt(cryptograph,priKey);// 解密密文
		System.out.println("解密之后的明文是："+target);
		byte[] decode = Base64.getDecoder().decode(target.getBytes());
		System.out.println("最终"+new String(decode));
		byte[]bytes =cryptograph.getBytes();
		// 产生签名
		String sign = sign(bytes, encryptBASE64(keyPair.getPrivate().getEncoded()));
		System.err.println("签名:" +sign);

		byte[] encoded = key.getEncoded();

		// 验证签名
		boolean status = verify(bytes, encryptBASE64(pair.getPublic().getEncoded()),sign);
		System.err.println("状态:" +status);
	}

}
