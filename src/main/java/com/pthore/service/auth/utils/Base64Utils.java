package com.pthore.service.auth.utils;

import java.util.Base64;

public class Base64Utils {
	
	
	public static String encode(byte[] data) {
		String encodedString  = Base64.getEncoder().encodeToString(data);
		return encodedString;
	}
	
	public static byte[] decode(String data) {
		byte[] decodedBytes = Base64.getDecoder().decode(data);
		return decodedBytes;
	}
}
