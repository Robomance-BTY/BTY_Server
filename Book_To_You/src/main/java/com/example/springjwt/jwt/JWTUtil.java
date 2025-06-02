package com.example.springjwt.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    // Value 어노테이션을 이용해서 secret 변수에 값을 넣는다.
    // 기본적으로 ("${spring.jwt.secret:기본시크릿키}")
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

    }

    public String getUsername(String token){
        // jwt.parser() : 토큰을 파싱하고 검증하는 객체 생성
        // verifyWith() : 토큰의 변조 여부 확인
        // build() : 빌더 타입으로 리턴
        // parseSignedClaims : 서명된 JWT 파싱하여 클레임(토큰에 담긴 정보)을 갖고옴
        // getPayload().get() : 페이로드 컨테이너에 해당 정보를 갖고온다.
        // getExpiration().before(new Date()) : 페이로드에 담긴 만료시간을 들고와서, 비교후 true or false 반환

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

// Jwts.builder(): JwtBuilder 인스턴스를 생성합니다.
// claim("username", username): "username"이라는 키에 username 변수의 값을 저장합니다.//
// issuedAt(new Date(System.currentTimeMillis())): 토큰의 발행 시간을 현재 시간으로 설정합니다.//
// expiration(new Date(System.currentTimeMillis() + expiredMs)): 토큰의 만료 시간을 현재 시간으로부터 expiredMs 밀리초 후로 설정합니다.
// signWith(secretKey): 제공된 secretKey로 토큰에 서명합니다. 이 서명은 토큰의 무결성을 보장하는 데 사용됩니다.
// compact(): JWT를 생성하고 문자열로 압축하여 반환합니다.

    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
