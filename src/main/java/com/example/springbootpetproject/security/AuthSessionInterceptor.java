package com.example.springbootpetproject.security;

import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Заполняет атрибуты сессии (username/role/balance), по которым header.html определяет,
 * вошёл ли пользователь.
 * <p>
 * Раньше их выставляли только UserController#getUserPage и OrderController#goToOrder,
 * поэтому после входа Spring Security редиректил на "/", эта страница отрисовывается
 * вью-контроллером из MvcConfig без участия контроллера, атрибуты оставались пустыми —
 * и шапка показывала вошедшему пользователю кнопку «Войти».
 * <p>
 * Интерцептор работает и для вью-контроллеров, поэтому закрывает все страницы сразу.
 * Данные читаются из БД один раз на сессию: дальше атрибут уже есть.
 */
@Component
@Slf4j
public class AuthSessionInterceptor implements HandlerInterceptor {

    private final UserServiceI userServiceI;

    public AuthSessionInterceptor(UserServiceI userServiceI) {
        this.userServiceI = userServiceI;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return true;
        }

        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            return true;
        }

        User user = userServiceI.findUserByUsername(authentication.getName());
        if (user == null) {
            // Аутентификация есть, а записи в БД нет (например, пользователя удалили при
            // живой сессии). Шапка просто покажет состояние «не вошёл».
            log.warn("authenticated user {} not found in database", authentication.getName());
            return true;
        }

        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getUserRole().name());
        session.setAttribute("balance", user.getUserCountOfMoney());
        return true;
    }
}
