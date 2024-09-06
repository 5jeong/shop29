package com.toy2.shop29.security.config;

import com.toy2.shop29.security.handler.UserLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) ->
                        auth.requestMatchers("/", "/user/signup", "/user/findId", "/user/findPassword",
                                        "/user/checkUserId", "/user/checkUserEmail", "/user/checkUserPhoneNumber",
                                        "/product/**", "/cart/**", "/faq/**", "/email/**", "/login", "/logout",
                                        "/board/**", "/css/**", "/error", "/mainPage/**", "/images/**",
                                        "/attachment/**", "/js/**", "/chatbot/**", "/qna/qna-list/**", "http://0.0.0.0:8082/chat").permitAll()
                                .requestMatchers("/admin").hasAuthority("관리자")
                                .anyRequest().authenticated()
                )

                .formLogin((form) ->
                        form.loginPage("/login").permitAll()
                                .usernameParameter("userId")
                                .successHandler(successHandler)
                                .failureHandler(failureHandler)
                )

                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutRequestMatcher(new AntPathRequestMatcher(
                                "/logout")) // 로그아웃 URL 주소 변경 가능 (Controller 처리 X), defaultURL-/logout
                        .logoutSuccessHandler(userLogoutSuccessHandler)
                        .invalidateHttpSession(true)// 로그아웃 후 세션 초기화 설정
                        .deleteCookies("JSESSIONID")// 로그아웃 후 쿠기 삭제 설정
                        .permitAll())

                .authenticationProvider(authenticationProvider)

                .csrf(csrf -> csrf.disable());

        return http.build();
    }


}

