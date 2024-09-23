package com.example.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    비밀번호 해시화

    @Bean           //    @Bean 은 클래스 인스턴스이다
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request)->request
                        .requestMatchers("/css/**","/js/**","/images/**").permitAll()
//                        ** 하위에 있는 것들을 모두 포함
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/**").permitAll())
//                        .anyRequest().authenticated())

                .formLogin(form->form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login") //security 가 처리하는 곳
                        .usernameParameter("userId") //userId로 세팅되어있는 걸로도 로그인을 시도하겠다.
                        .defaultSuccessUrl("/cinema",true)) //로그인 성공했을 때 들어갈 url

                .logout((out)->out
                        .logoutSuccessUrl("/cinema")
                        .logoutUrl("/logout"))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
