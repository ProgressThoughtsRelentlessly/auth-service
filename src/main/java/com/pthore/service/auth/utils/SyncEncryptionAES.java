package com.pthore.service.auth.utils;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*
reference:
	https://www.baeldung.com/java-aes-encryption-decryption
	
	steps:
		. generate a SecretKey for AES algorithm using either (password + salt) or randomKey.
		. generate a Initialization Vector.
		. generate a cipher object
		. Encrypt / decrypt the data. / also Base64 encode the string if needed.
	AES mechanism :
		. lookup the reference or read Spring-Security-Notes-4.
		
*/
public class SyncEncryptionAES {
	
	private SecretKey generateSecretKey(int n) {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(n); // 256 bits
			SecretKey secretKey = keyGen.generateKey();
			return secretKey;
		} catch(Exception ex) {
			System.err.println(ex.getLocalizedMessage());
		}
		
		return null;
	}
	
	public SecretKey generateSecretKeyFromPassword(String password, String salt, int n) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, n);
			
			SecretKey secretKey = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");
			return secretKey;
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	public IvParameterSpec generateIv() {
		byte[] iv = new byte[16]; // 16 bytes  == 256bits
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}
	
	public String encryptData(String algorithm, String input, SecretKey secretKey, IvParameterSpec iv) {
			
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] cipherText = cipher.doFinal(input.getBytes());
			return Base64Utils.encode(cipherText);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String decryptData(String algorithm, String input, SecretKey secretKey, IvParameterSpec iv) {
		
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] deCipheredText = cipher.doFinal(Base64Utils.decode(input));
			
			return new String(deCipheredText);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException 
				| InvalidKeyException | InvalidAlgorithmParameterException 
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private SealedObject encryptObject(String algorithm, Serializable object, 
			SecretKey secretKey, IvParameterSpec iv) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			SealedObject sealedObject = new SealedObject(object, cipher);
			return sealedObject;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException 
				| NoSuchAlgorithmException | NoSuchPaddingException 
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Serializable decryptObject(String algorithm, SealedObject obj,
			SecretKey secretKey, IvParameterSpec iv) {

		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			Serializable unSealedObject = (Serializable) obj.getObject(cipher);
			return unSealedObject;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException 
				| NoSuchPaddingException | ClassNotFoundException | IllegalBlockSizeException 
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}
		
		return secretKey;
	}
	
	public void demo() {
		String transformation = "AES/CBC/PKCS5Padding";
		String password = "``Naruto``";
		String salt = "hookage";
		SecretKey secret = this.generateSecretKeyFromPassword(password, salt, 256);
				// this.generateSecretKey(256);
		IvParameterSpec iv = this.generateIv();
		
		String text = "Naruto will become a Hookage one day! Believe it!";
		String cipherText = this.encryptData(transformation, text, secret, iv);
		String deCipheredText = this.decryptData(transformation, cipherText, secret, iv);
		
		System.out.printf("cipherText = %s \n"
				+ "deCipheredText = %s", cipherText, deCipheredText);
	}
	
	public static void main(String[] args) {
		SyncEncryptionAES aes = new SyncEncryptionAES();
		aes.demo();
//		String iv = Base64Utils.encode(aes.generateIv().getIV());
//		System.out.println(iv);
		
	}
}
