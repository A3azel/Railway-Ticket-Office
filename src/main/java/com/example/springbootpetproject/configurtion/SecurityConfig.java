package com.example.springbootpetproject.configurtion;

import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.security.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
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
                    .csrf().disable()
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
                    .invalidSessionUrl("/")
                    .maximumSessions(1)//?
                    .maxSessionsPreventsLogin(false)
                    /*.sessionRegistry(sessionRegistry())*/;
    }

    /*@Bean(name = "sessionRegistry")
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }*/

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImp);
        return daoAuthenticationProvider;
    }
}
