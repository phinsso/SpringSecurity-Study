package com.example.TestSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 해당 클래스가 설정 클래스임을 선언
@EnableWebSecurity // 해당 클래스가 스프링 시큐리티에게 관리됨
public class SecurityConfig {

    // 사용자 인증시에 비밀번호에 대해 단방향 해시 암호화를 진행하여 저장되어 있는 비밀번호와 대조하기 때문에 회원가입시 비밀번호 항목에 대해 암호화를 진행해야 한다.
    // BCryptPasswordEncoder 클래스를 리턴하는 메서드를 빈으로 등록하여 사용하기
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 각 경로에 따른 접근 권한 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login").permitAll() // permitAll(): 모든 사용자 접근 허용
                        .requestMatchers("admin").hasRole("ADMIN") // hasRole(): 사용자가 주어진 역할이 있다면 접근 허용
                        // **(와일드카드): 각 유저아이디에 해당하는 마이페이지 등의 여러값이 들어갈 수 있는 주소에 사용
                        // hasAnyRole(): 사용자가 주어진 역할이 있다면 접근 허용 (여러 역할)
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // anyRequest(): 위에서 설정하지 않은 모든 경로에 대한 접근
                        // authenticated(): 로그인한 사용자만 접근을 허용
                        .anyRequest().authenticated()
                );

        http
                // loginPage(): 로그인 페이지의 경로를 설정해주면, 특정 역할이나 권한이 필요한 페이지에 접근했을 때 오류 페이지가 발생하지 않고 스프링이 자동으로 로그인 페이지로 리다이렉션을 해줌
                .formLogin((auth) -> auth.loginPage("/login")
                        // loginProcessingUrl(): 해당 url로 요청시 스프링 시큐리티가 로그인 과정을 진행해줌
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }
}
