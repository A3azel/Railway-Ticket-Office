package com.example.springbootpetproject.configurtion;

import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.security.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Без prePostEnabled все @PreAuthorize в сервисах — просто декорация: Spring их не
// применяет, и проверки вида "#username == authentication.principal.username" не работают.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImp userDetailsServiceImp;
    private final CustomBCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, CustomBCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // change configure() method
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return null;
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF включён. Формы Thymeleaf с th:action получают скрытый токен
                // автоматически; logout обязан быть POST, иначе Spring его не примет.
                .authorizeRequests()
                    .antMatchers("/order/**","/user/**").authenticated()/*.hasAnyRole(UserRole.ADMIN.name(),UserRole.USER.name())*/
                    .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                    .anyRequest()
                    .permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login/error")
                    .permitAll()
                    .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    /*.deleteCookies("JSESSIONID")*/
                    .logoutSuccessUrl("/")
                    .permitAll()
                .and()
                    .sessionManagement()
                    // maximumSessions(1) убрано намеренно. Повторный вход того же
                    // пользователя вытеснял предыдущую сессию, а та затем получала пустой
                    // ответ 200 (expiredUrl не задан) — в браузере это выглядело как
                    // «выбросило на главную без авторизации». Вдобавок HttpSessionEventPublisher
                    // отсутствует, поэтому выход не освобождал слот и ограничение залипало
                    // до перезапуска приложения. Для нескольких задач ECS оно всё равно
                    // не работает: SessionRegistry хранится в памяти одного экземпляра.
                    .invalidSessionUrl("/");
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImp);
        return daoAuthenticationProvider;
    }
}
