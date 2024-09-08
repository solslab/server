package kr.sols.config;

import com.amazonaws.HttpMethod;
import kr.sols.auth.handler.CustomAccessDeniedHandler;
import kr.sols.auth.handler.CustomAuthenticationEntryPoint;
import kr.sols.auth.handler.OAuth2FailureHandler;
import kr.sols.auth.handler.OAuth2SuccessHandler;
import kr.sols.auth.jwt.TokenAuthenticationFilter;
import kr.sols.auth.jwt.TokenExceptionFilter;
import kr.sols.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // 서비스 주입
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                // error endpoint와 favicon.ico를 무시
                .requestMatchers("/error", "/favicon.ico", "/index.html");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ⭐️ CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API 설정
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
                .headers(c -> c.frameOptions(FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함

                // 요청 인증 및 인가 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                new AntPathRequestMatcher("/api"),
                                new AntPathRequestMatcher("/api/auth/success"),
                                new AntPathRequestMatcher("/api/auth/check"),
                                new AntPathRequestMatcher("/api/company/**", "GET"),
                                new AntPathRequestMatcher("/api/admin/login"),
                                new AntPathRequestMatcher("/api/feedback", "POST"),
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/auth/success"),
                                new AntPathRequestMatcher("/auth/check"),
                                new AntPathRequestMatcher("/company/**", "GET"),
                                new AntPathRequestMatcher("/admin/login"),
                                new AntPathRequestMatcher("/feedback", "POST")
                        ).permitAll()
                        .anyRequest().authenticated()
                )


                // OAuth2 로그인 설정
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService)) // 사용자 정보 가져오는 설정
                                .successHandler(oAuth2SuccessHandler) // 로그인 성공 핸들러 설정
                                .failureHandler(new OAuth2FailureHandler())
                )

                // JWT 필터 설정
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 인증 필터 추가
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링 필터 추가

                // 인증 예외 핸들링
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 예외 처리
                        .accessDeniedHandler(new CustomAccessDeniedHandler())); // 접근 거부 예외 처리

        return http.build();
    }
}