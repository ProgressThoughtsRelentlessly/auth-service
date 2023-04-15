package com.pthore.service.auth.utils;

public interface AppConstants {
	public final String GOOGLE_CLIENT_ID = "199747861409-jfgvc2v9cld28u41437n4ujn7nqg9tum.apps.googleusercontent.com";
	public final String GOOGLE_CLIENT_SECRET = "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";
	public final String LOGIN_FAILURE_URL = "http://localhost:4200/login";
	
	public final String JWT_SECRET_KEY = "eW91Q2FudEdldE1lSGFja2Vyc0tlZXBUcnlpbmdZb3VyTHVjaw=="; // base64 for "youCantGetMeHackersKeepTryingYourLuck"
	
	// AES SECTION
	public final String AES_TRANSFORMATION_STRING = "AES/CBC/PKCS5Padding";
	public final String AES_PASSWORD = "``Naruto``";
	public final String AES_SALT = "hookage";
	public final String AES_IV = "as1XGJ1ZOdVJEV1KqqN31Q==";
	
}
