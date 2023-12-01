package com.example.locker14;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration // пометка, что методы класса являются источниками конфигурационных @Bean
@EnableWebSecurity // внутри класса настраивается доступ к url
public class SecurityConfiguration {

    // NoOpPasswordEncoder - пароль не шифруется
    @Bean
    public static NoOpPasswordEncoder getEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    private UserDetailSource source;

    // AuthenticationProvider связывает между собой
    // источник UserDetail о пользователях и
    // механизм проверки их паролей
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getEncoder());
        provider.setUserDetailsService(source);
        return provider;
    }

    // SecurityFilterChain используется для настройки url которые защищаются
    @Bean
    public SecurityFilterChain getChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(toH2Console()).permitAll()
                                .requestMatchers(antMatcher(HttpMethod.GET, "/index.html")).permitAll()
                                .requestMatchers(antMatcher(HttpMethod.GET, "/open")).permitAll()
                                .requestMatchers(antMatcher(HttpMethod.GET, "/")).permitAll()
                                .requestMatchers(antMatcher(HttpMethod.GET, "/admin/**")).hasAnyRole("ADMIN")
                                .anyRequest().authenticated() // все остальные только требуют аутентификации
                )
                .formLogin() // стандартная форма для аутентификации
                .and()
                .csrf() // выключается csrt
                .disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic(Customizer.withDefaults()) // basic - добавляется заголовок
                .logout() // /logout
                .invalidateHttpSession(true)// при логауте удаляется сессия
                .deleteCookies("JSESSIONID") // и куки
        ;

        return http.build();
    }
}
