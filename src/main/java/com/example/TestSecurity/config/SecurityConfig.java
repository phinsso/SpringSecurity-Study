package com.example.TestSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 해당 클래스가 설정 클래스임을 선언
@EnableWebSecurity // 해당 클래스가 스프링 시큐리티에게 관리됨
public class SecurityConfig {

    // 각 경로에 따른 접근 권한 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login").permitAll() // permitAll(): 모든 사용자 접근 허용
                        .requestMatchers("admin").hasRole("ADMIN") // hasRole(): 사용자가 주어진 역할이 있다면 접근 허용
                        // **(와일드카드): 각 유저아이디에 해당하는 마이페이지 등의 여러값이 들어갈 수 있는 주소에 사용
                        // hasAnyRole(): 사용자가 주어진 역할이 있다면 접근 허용 (여러 역할)
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // anyRequest(): 위에서 설정하지 않은 모든 경로에 대한 접근
                        // authenticated(): 로그인한 사용자만 접근을 허용
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
