package com.pthore.service.auth.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.nimbusds.jose.util.StandardCharset;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTokenValidator {
	

	
	public static Claims getJwtClaims(String jwtString) {

		SecretKey key = Keys.hmacShaKeyFor(AppConstants.JWT_SECRET_KEY.getBytes(StandardCharset.UTF_8));
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
		
		Claims claims = jwtParser.parseClaimsJws(jwtString).getBody();
		return claims;
	}
	private static String decryptData(String jwtString) {
		
		SyncEncryptionAES AESEncryptor = new SyncEncryptionAES();
		SecretKey secretKey = AESEncryptor.generateSecretKeyFromPassword(AppConstants.AES_PASSWORD, AppConstants.AES_SALT, 256);
		IvParameterSpec iv = new IvParameterSpec(Base64Utils.decode(AppConstants.AES_IV) );
		String plainText = AESEncryptor.decryptData(AppConstants.AES_TRANSFORMATION_STRING, jwtString, secretKey, iv);
		return plainText;
	}
	
	public static Map<String, String> getDecryptedJwtBodyAsMap(String jwtString) {
		
		Claims claims = getJwtClaims(jwtString);
		
		Map<String, String> map = new HashMap<>();
		
//		for(Entry<String, Object> claim: claims.entrySet()) {
//			System.out.println("jwt claim = " + claim);
//			map.put(claim.getKey(), (String) claim.getValue());
//		}
		
		String encryptedText = (String)claims.get("jwtBody"); // map.get("jwtBody");
		String decryptedText = decryptData(encryptedText);
		ObjectMapper mapper  = new ObjectMapper().registerModule(new ParameterNamesModule())
				   .registerModule(new Jdk8Module())
				   .registerModule(new JavaTimeModule());
		try {
			map = mapper.readValue(decryptedText, new TypeReference<HashMap<String, String>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return map;
	}
}
