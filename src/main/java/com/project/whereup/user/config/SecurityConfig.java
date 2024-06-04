package com.project.whereup.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /**경로별 인가작업*/
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/home", "/login","/signup","/**").permitAll()
                                .anyRequest().authenticated()
                )
                //커스텀 로그인 페이지
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/",true)
                                .failureUrl("/login")
                                .permitAll()
                )
                .logout((logout)-> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) //로그아웃 시 생성된 사용자 세션도 삭제
                );
        // csrf : 사이트 위변조 방지 설정 (스프링 시큐리티에는 자동으로 설정됨)
        // csrf기능 켜져있으면 post 요청을 보낼때 csrf 토큰도 보내줘야 로그인 진행됨 ! -개발단계에서만 csrf 잠시 꺼두기
        http
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}