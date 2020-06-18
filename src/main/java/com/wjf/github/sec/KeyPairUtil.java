package com.wjf.github.sec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * 用于加密、解密
 * 主要使用RSA算法
 *
 * @author wjf
 */
public class KeyPairUtil {

	/**
	 * 获取密钥对
	 * 只支持RSA
	 *
	 * @param keyPairGeneratorKind 加密种类
	 * @return 返回密钥对
	 * @throws NoSuchAlgorithmException 请参照 {@link java.security.KeyPairGenerator#getInstance(String)}
	 */
	public static KeyPair getKeys(KeyPairGeneratorKind keyPairGeneratorKind) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyPairGeneratorKind.getName());
		keyPairGenerator.initialize(1024);
		return keyPairGenerator.generateKeyPair();
	}

	/**
	 * 根据公钥指数和模数获取公钥信息
	 *
	 * @param modulus              公钥模数
	 * @param exponent             公钥指数
	 * @param keyPairGeneratorKind 密钥对类型（只支持RSA）
	 * @return 返回公钥信息
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchMethodException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(BigInteger modulus, BigInteger exponent, KeyPairGeneratorKind keyPairGeneratorKind) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchAlgorithmException, NoSuchMethodException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance(keyPairGeneratorKind.getName());
		Constructor<? extends KeySpec> constructor = keyPairGeneratorKind.getPublicClazz().getConstructor(BigInteger.class, BigInteger.class);
		KeySpec keySpec = constructor.newInstance(modulus, exponent);
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * 更具私钥模数和指数获取私钥信息
	 *
	 * @param modulus              私钥模数
	 * @param exponent             私钥指数
	 * @param keyPairGeneratorKind 密钥对类型（只支持RSA）
	 * @return 私钥信息
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent, KeyPairGeneratorKind keyPairGeneratorKind) throws NoSuchAlgorithmException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance(keyPairGeneratorKind.getName());
		Constructor<? extends KeySpec> constructor = keyPairGeneratorKind.getPrivateClazz().getConstructor(BigInteger.class, BigInteger.class);
		KeySpec keySpec = constructor.newInstance(modulus, exponent);
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 通过公钥加密
	 *
	 * @param cipherKind
	 * @param publicKey
	 * @param info
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	public static String encryptByPublicKey(CipherKind cipherKind, RSAPublicKey publicKey, String info) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
		Cipher cipher = Cipher.getInstance(cipherKind.getName());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] infos = info.getBytes();
		byte[] bytes = getBytes(cipher, publicKey.getModulus().bitLength() / 8 - 11, infos);
		return encryptionByBase64(new String(bytes));
//		return new String(bytes);
	}

	private static byte[] getBytes(Cipher cipher, int key_len, byte[] infos) throws IOException, BadPaddingException, IllegalBlockSizeException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] cache;
		int i = 1;
		int offset = 0;
		while (offset < infos.length) {
			if (infos.length - offset > key_len) {
				cache = cipher.doFinal(infos, 0, key_len);
			} else {
				cache = cipher.doFinal(infos, 0, infos.length - offset);
			}
			baos.write(cache);
			offset += i * key_len;
			i++;
		}
		return baos.toByteArray();
	}

	/**
	 * 通过私钥解密
	 *
	 * @param cipherKind
	 * @param privateKey
	 * @param info
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String decryptByPriateKey(CipherKind cipherKind, RSAPrivateKey privateKey, String info) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
		Cipher cipher = Cipher.getInstance(cipherKind.getName());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		info = decryptByBase64(info);
		byte[] infos = info.getBytes();
		byte[] bytes = getBytes(cipher, privateKey.getModulus().bitLength() / 8, infos);
		return new String(bytes);
	}

	public static String decryptByBase64(String info) {
		return new String(Base64.getDecoder().decode(info));
	}

	public static String encryptionByBase64(String info) {
		return Base64.getEncoder().encodeToString(info.getBytes());
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, InvocationTargetException, NoSuchMethodException, InstantiationException, InvalidKeySpecException, IllegalAccessException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException {
		String test = "hello world!aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		KeyPair keys = getKeys(KeyPairGeneratorKind.RSA);
		RSAPublicKey publicKey = (RSAPublicKey) keys.getPublic();
		PublicKey publicKey1 = getPublicKey(publicKey.getModulus(), publicKey.getPublicExponent(), KeyPairGeneratorKind.RSA);
		String s = encryptByPublicKey(CipherKind.RSA_ECB_NOPADDING, (RSAPublicKey) publicKey1, test);
		System.out.println("加密后的--->" + s);

		RSAPrivateKey privateKey = (RSAPrivateKey) keys.getPrivate();
		PrivateKey privateKey1 = getPrivateKey(privateKey.getModulus(), privateKey.getPrivateExponent(), KeyPairGeneratorKind.RSA);
		String s1 = decryptByPriateKey(CipherKind.RSA_ECB_NOPADDING, privateKey, s);
		System.out.println("解密后的--->" + s1);

		String s3 = s.replaceAll("N", "a");



//		String s2 = decryptByPriateKey(CipherKind.RSA_ECB_NOPADDING, (RSAPrivateKey) privateKey1, s3);
//		System.out.println(s2);
	}

}
