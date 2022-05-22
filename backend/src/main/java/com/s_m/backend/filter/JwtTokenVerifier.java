package com.s_m.backend.filter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.s_m.backend.service.TokenService;
import com.s_m.backend.utility.CookieUtil;
import com.s_m.backend.utility.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JwtTokenVerifier extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	@Autowired
	private TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		// String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		//
		// if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer
		// ")) {
		// filterChain.doFilter(request, response);
		// return;
		// }
		//
		// String token = authorizationHeader.replace("Bearer ", "");}
		
		
		String path = request.getServletPath();
		if(path.equals("/auth/login") || path.equals("/auth/register") || path.equals("/auth/forgot-password-email") || path.equals("/auth/validate-forgot-password-otp") || path.equals("/auth/forgot-password")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = cookieUtil.getAccessTokenFromCookie(request.getCookies());

		if (token == null || token.equals("")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if(tokenService.isTokenInBlacklist(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {

			Claims body = jwtUtil.getAllClaimsFromToken(token);

			String username = body.getSubject();

			@SuppressWarnings("unchecked")
			List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
					.map(m -> new SimpleGrantedAuthority(m.get("authority")))
					.collect(Collectors.toSet());

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
		}

		filterChain.doFilter(request, response);
	}

}
