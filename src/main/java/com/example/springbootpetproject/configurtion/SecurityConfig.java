package com.example.springbootpetproject.configurtion;

import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.security.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Без этой аннотации все @PreAuthorize в сервисах — просто декорация: Spring их не
// применяет, и проверки вида "#username == authentication.principal.username" не работают.
// В Spring Security 6 на смену @EnableGlobalMethodSecurity(prePostEnabled = true).
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsServiceImp userDetailsServiceImp;
    private final CustomBCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, CustomBCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * В Spring Security 6 WebSecurityConfigurerAdapter удалён, конфигурация задаётся
     * бином SecurityFilterChain. Правила доступа те же, что и до миграции:
     * authorizeRequests/antMatchers заменены на authorizeHttpRequests/requestMatchers.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF включён. Формы Thymeleaf с th:action получают скрытый токен
                // автоматически; logout обязан быть POST, иначе Spring его не примет.
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/order/**", "/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureUrl("/login/error")
                        .permitAll())
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/")
                        .permitAll())
                // maximumSessions(1) убрано намеренно. Повторный вход того же
                // пользователя вытеснял предыдущую сессию, а та затем получала пустой
                // ответ 200 (expiredUrl не задан) — в браузере это выглядело как
                // «выбросило на главную без авторизации». Вдобавок HttpSessionEventPublisher
                // отсутствует, поэтому выход не освобождал слот и ограничение залипало
                // до перезапуска приложения. Для нескольких задач ECS оно всё равно
                // не работает: SessionRegistry хранится в памяти одного экземпляра.
                .sessionManagement(session -> session
                        .invalidSessionUrl("/"));
        return http.build();
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImp);
        return daoAuthenticationProvider;
    }
}
