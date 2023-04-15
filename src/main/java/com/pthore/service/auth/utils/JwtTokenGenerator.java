package com.pthore.service.auth.utils;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.StandardCharset;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTokenGenerator {
	
	
	
	private static String encryptData (String input) {
		
		SyncEncryptionAES AESEncryptor = new SyncEncryptionAES();
		SecretKey secretKey = AESEncryptor.generateSecretKeyFromPassword(AppConstants.AES_PASSWORD, AppConstants.AES_SALT, 256);
		IvParameterSpec iv = new IvParameterSpec(Base64Utils.decode(AppConstants.AES_IV) );
		String jwtString = AESEncryptor.encryptData(AppConstants.AES_TRANSFORMATION_STRING, input, secretKey, iv);
		return jwtString;
	}
	
	public static String generateToken(Map<String, String> jwtClaims) {
		// you need key and  algorithm 
		SecretKey key = Keys.hmacShaKeyFor(AppConstants.JWT_SECRET_KEY.getBytes(StandardCharset.UTF_8));
		String jwtString = Jwts.builder()
					.setIssuer("pthore-auth-server")
					.setSubject("jwt token for login")
					.setClaims(jwtClaims)
					.setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + 30000000))
					.signWith(key)
					.compact();
		return jwtString;
	}
	
	public static String generateToken(String claim) {
		// you need key and  algorithm 
		SecretKey key = Keys.hmacShaKeyFor(AppConstants.JWT_SECRET_KEY.getBytes(StandardCharset.UTF_8));
		String jwtString = Jwts.builder()
					.setIssuer("pthore-auth-server")
					.setSubject("jwt token for login")
					.claim("jwtBody", claim)
					.setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + 30000000))
					.signWith(key)
					.compact();
		return jwtString;
	}
	
	public static String encryptAndGenerateToken(Map<String, String> jwtClaims) {
		
		String token  = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json  = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jwtClaims);
			String encryptedData = encryptData(json);
			token = generateToken(encryptedData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return token;
		
	}
	
}
