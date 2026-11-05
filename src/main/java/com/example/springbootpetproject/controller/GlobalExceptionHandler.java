package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.routeExceptions.ProblemWithSeatsCount;
import com.example.springbootpetproject.customExceptions.userExceptions.InsufficientFunds;
import com.example.springbootpetproject.customExceptions.userExceptions.InvalidCountOfMoney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Единая обработка ошибок. До её появления любое исключение уходило пользователю
 * стандартной whitelabel-страницей со стектрейсом, а проверки в сервисах давали 500.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Нехватка средств и мест — это ожидаемый исход покупки, а не сбой. Возвращаем
     * пользователя на страницу заказа с сообщением.
     * <p>
     * Сообщение передаётся flash-атрибутом: обычный addAttribute при редиректе в модель
     * не попадает и уезжал в query-строку, поэтому раньше пользователь не видел причины.
     */
    @ExceptionHandler({InsufficientFunds.class, ProblemWithSeatsCount.class, InvalidCountOfMoney.class})
    public String handlePurchaseProblem(RuntimeException exception, HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {
        log.info("purchase rejected: {}", exception.getMessage());
        redirectAttributes.addFlashAttribute("purchaseError", exception.getMessage());
        // Берём URI текущего запроса, а не заголовок Referer: Referer приходит от клиента,
        // и редирект по нему был бы open redirect на произвольный внешний адрес.
        String uri = request.getRequestURI();
        return uri.startsWith("/order/") ? "redirect:" + uri : "redirect:/user";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException exception) {
        log.warn("access denied: {}", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(Exception exception, Model model) {
        // Логируем со стектрейсом, пользователю отдаём только нейтральный текст.
        log.error("unhandled exception", exception);
        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");
        return "error";
    }
}
