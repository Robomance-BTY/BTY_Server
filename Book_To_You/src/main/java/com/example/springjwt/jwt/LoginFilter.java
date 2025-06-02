package com.example.springjwt.jwt;

import com.example.springjwt.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
// API 호출에 사용할 수 있는 형식을 위해 상속받아서 리팩토링 시작
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;



    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("username = {}",username);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        log.info("success");
        //CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        //인증(Authentication) 객체에서 주체(principal)를 가져오는데,
        // 이 주체는 CustomUserDetails 타입이다.
        // CustomUserDetails는 Spring Security에서 제공하는 UserDetails 인터페이스의 구현체로,
        // 사용자의 정보(아이디, 패스워드, 권한 등)를 담고 있음
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 600*600*100L);

        response.addHeader("Authorization", "Bearer " + token);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", customUserDetails.getUserEntity().getId()); // CustomUserDetails에서 UserEntity의 ID를 가져오는 방법을 구현해야 함
        responseBody.put("message", "로그인에 성공했습니다.");

        // ObjectMapper를 사용하여 Map 객체를 JSON 문자열로 변환
        String jsonResponseBody = new ObjectMapper().writeValueAsString(responseBody);

        // 응답 본문에 JSON 문자열 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseBody);
        response.getWriter().flush();
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("fail");
        response.setStatus(401);

    }
}