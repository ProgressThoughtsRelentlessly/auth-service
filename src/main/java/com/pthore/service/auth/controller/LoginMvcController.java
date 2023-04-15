package com.pthore.service.auth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.pthore.service.auth.service.UserAuthorizationService;


@Controller
public class LoginMvcController {
	
	private Logger log = LoggerFactory.getLogger(LoginMvcController.class);
	
	@Autowired
	UserAuthorizationService userAuthService;
	
	@GetMapping(value = "/loginGoogle")
	public String OAuth2LoginWithGoogle(OAuth2AuthenticationToken token, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String jwtString = userAuthService.handleLoginAndGenerateJwt(token);
		String email = userAuthService.getEmailFromOAuth2Token(token);
		Cookie c = new Cookie("jwt", jwtString);
		Cookie c2 = new Cookie("email", email);
		
		response.addCookie(c);
		response.addCookie(c2);
		return "redirect:http://localhost:4200/home";
	}
	
	
}
