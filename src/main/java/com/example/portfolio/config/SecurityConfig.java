package com.example.portfolio.config;

import com.example.portfolio.service.UserService;
import com.example.portfolio.util.SpringSecurity;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final int TOKEN_VALIDITY_TIME = 604800;

  @NonNull
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  @NonNull
  private final UserService userService;

  @Bean
  public FilterRegistrationBean<Filter> getSpringSecurityFilterChainBindedToError(
      @Qualifier("springSecurityFilterChain") Filter springSecurityFilterChain) {
    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    registration.setFilter(springSecurityFilterChain);
    registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
    return registration;
  }

  @Bean
  public AuthenticationFailureHandler customAuthenticationFailureHandler() {
    return new CustomAuthenticationHandler();
  }

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return new CustomAuthenticationHandler();
  }

  @Bean
  public LoginUrlAuthenticationEntryPoint ajaxAwareAuthenticationEntryPoint() {
    return new AjaxAwareAuthenticationEntryPoint(SpringSecurity.LOGIN_URL);
  }

  @Bean
  public AccessDeniedHandler customAccessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  /**
   * 유저 DB의 DataSource와 Query 및 Password Encoder 설정.
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }

  /**
   * Spring Security에서 인증받지 않아도 되는 리소스 URL 패턴을 지정해 줍니다.
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/static/**");
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    // setAllowCredentials(true) is important, otherwise:
    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
    configuration.setAllowCredentials(true);
    // setAllowedHeaders is important! Without it, OPTIONS preflight request
    // will fail with 403 Invalid CORS request
    configuration.setAllowedHeaders(Collections.singletonList("Authorization"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Spring Security에 의해 인증받아야 할 URL 또는 패턴을 지정해 줍니다.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.headers().frameOptions().sameOrigin()
        .and().csrf().disable().authorizeRequests()
        .antMatchers("/admin/**","/mypage/**","/comment/**").authenticated()
        .anyRequest().permitAll()
        .and().formLogin().loginPage(SpringSecurity.LOGIN_URL)
        .loginProcessingUrl(SpringSecurity.LOGIN_PROCESS_URL)
        .successHandler(customAuthenticationSuccessHandler())
        .failureHandler(customAuthenticationFailureHandler())
        .usernameParameter(SpringSecurity.PARAM_USERNAME)
        .passwordParameter(SpringSecurity.PARAM_PASSWORD)
        .and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher(SpringSecurity.LOGOUT_URL))
        .logoutSuccessUrl(SpringSecurity.LOGIN_URL)
        .and().exceptionHandling().authenticationEntryPoint(ajaxAwareAuthenticationEntryPoint())
        .accessDeniedHandler(customAccessDeniedHandler())
        .and().rememberMe().disable();
  }
}
