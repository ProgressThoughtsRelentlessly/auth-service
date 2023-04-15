package com.pthore.service.auth.utils;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.nimbusds.jose.util.StandardCharset;

public class AsyncEncryptionRSA {
	
	private final String ALGORITHM = "RSA";
	
	private KeyPair createKeyPair(String algorithm) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
			keyPairGen.initialize(2048);
			
			KeyPair pair = keyPairGen.generateKeyPair();
			return pair;
			
		} catch(NoSuchAlgorithmException ex) {
			System.err.println(ex.getLocalizedMessage());
		}
		return null;		
	}
	
	private void  displayPublicAndPrivateKeys(KeyPair pair) {
		
		PublicKey publicKey = pair.getPublic();
		PrivateKey privateKey = pair.getPrivate();
		System.out.println(String.format("[ Public key = %s \n"
				+ "Private Key = %s]", publicKey.toString(), privateKey.toString())
				);
	}
	
	private String encryptData(String data, PublicKey publicKey) {
		
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] input = data.getBytes();
			cipher.update(input);
			byte[] cipherBytes = cipher.doFinal();
			
			String cipherText = Base64Utils.encode(cipherBytes);
			return cipherText;
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String decryptData(String data, PrivateKey privateKey) {

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] encryptedBytes = Base64Utils.decode(data);
			
			byte[] deCipheredBytes = cipher.doFinal(encryptedBytes);
			
			String plainText = new String(deCipheredBytes, StandardCharset.UTF_8);
			return plainText;
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void demo() {
		
		String text = "Some Random text";
		
		KeyPair pair = this.createKeyPair(ALGORITHM);
		this.displayPublicAndPrivateKeys(pair);
		
		String cipherText = this.encryptData(text, pair.getPublic());
		String deCipheredText = this.decryptData(cipherText, pair.getPrivate());
		
		System.out.printf("cipherText = %s \n"
				+ "deCipheredText = %s", cipherText, deCipheredText);
		
	}
	public static void main(String[] args) throws Exception {
		AsyncEncryptionRSA rsa = new AsyncEncryptionRSA();
		 rsa.demo();
	}
}
