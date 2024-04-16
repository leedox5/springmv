package kr.leedox;

import kr.leedox.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserSecurityService userSecurityService;
    private final DataSource dataSource;

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/club/**").authorizeHttpRequests().antMatchers("/**").permitAll()
                .and().csrf().ignoringAntMatchers("/club/**")
                .and()
                      .formLogin()
                      .loginPage("/club/login")
                      .defaultSuccessUrl("/club/stat")
                .and().logout()
                      .logoutRequestMatcher(new AntPathRequestMatcher("/club/logout"))
                      .logoutSuccessUrl("/club/intro")
                      .invalidateHttpSession(true)
                .and().rememberMe()
                      .userDetailsService(userSecurityService)
                      .tokenRepository(tokenRepository());
                ;
        return http.build();
    }

    @Bean
    public SecurityFilterChain clubFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().antMatchers("/**").permitAll()
                .and().csrf().ignoringAntMatchers("/book/**")
                .and()
                .formLogin()
                .loginPage("/book/login")
                .defaultSuccessUrl("/book/")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/book/logout"))
                .logoutSuccessUrl("/book/")
                .invalidateHttpSession(true)
                .and().rememberMe()
                .userDetailsService(userSecurityService)
                .tokenRepository(tokenRepository());
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
