package com.example.springbootpetproject;

import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.service.serviceImplementation.CityServiceI;
import com.example.springbootpetproject.service.serviceImplementation.OrdersServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Защита от регрессий по шагу 3: CSRF, работающий @PreAuthorize, отсутствие хеша
 * пароля в модели и запрет доступа к чужому заказу.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityRulesTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private OrdersServiceI ordersServiceI;
    @Autowired private UserServiceI userServiceI;
    @Autowired private CityServiceI cityServiceI;

    // ---------- правила доступа по URL ----------

    @Test
    @DisplayName("Неаутентифицированный пользователь уходит на страницу входа")
    void anonymousIsRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Публичные страницы доступны без входа")
    void publicPagesAreOpen() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/actuator/health/readiness")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "plainUser", roles = "USER")
    @DisplayName("Обычному пользователю админка запрещена")
    void userCannotReachAdminArea() throws Exception {
        mockMvc.perform(get("/admin/users/all/page/1")).andExpect(status().isForbidden());
    }

    // ---------- CSRF ----------

    @Test
    @WithMockUser(username = "plainUser", roles = "USER")
    @DisplayName("POST без CSRF-токена не доходит до контроллера")
    void postWithoutCsrfTokenIsRejected() throws Exception {
        // Токена в сессии нет вовсе -> MissingCsrfTokenException. Поскольку в
        // SecurityConfig задан invalidSessionUrl("/"), Spring Security считает это
        // истёкшей сессией и редиректит на "/", а не отдаёт 403. Главное здесь —
        // запрос отклонён до контроллера и денег не прибавилось.
        mockMvc.perform(post("/user/topUpAccount").param("countOfMoney", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "plainUser", roles = "USER")
    @DisplayName("POST с подменённым CSRF-токеном получает 403")
    void postWithInvalidCsrfTokenIsForbidden() throws Exception {
        mockMvc.perform(post("/user/topUpAccount").param("countOfMoney", "100")
                        .with(csrf().useInvalidToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "plainUser", roles = "USER")
    @DisplayName("POST с CSRF-токеном проходит фильтр безопасности")
    void postWithCsrfTokenPassesSecurity() throws Exception {
        // Намеренно нечисловая сумма: до записи в БД дело не доходит, но запрос
        // проходит CSRF-фильтр и попадает в контроллер, что и проверяется.
        mockMvc.perform(post("/user/topUpAccount").param("countOfMoney", "abc").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // ---------- @PreAuthorize действительно работает ----------

    @Test
    @WithMockUser(username = "alice", roles = "USER")
    @DisplayName("Чужие заказы через сервис недоступны (@PreAuthorize активен)")
    void preAuthorizeBlocksForeignUsername() {
        assertThatThrownBy(() -> ordersServiceI.getAllUserOrdersByUserName(
                "bob", PageRequest.of(0, 10), 1, "asc", "id"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "alice", roles = "USER")
    @DisplayName("Свои заказы через тот же метод доступны")
    void preAuthorizeAllowsOwnUsername() {
        assertThatCode(() -> ordersServiceI.getAllUserOrdersByUserName(
                "alice", PageRequest.of(0, 10), 1, "asc", "id"))
                .doesNotThrowAnyException();
    }

    @Test
    @WithMockUser(username = "plainUser", roles = "USER")
    @DisplayName("Пишущий метод админского сервиса закрыт от обычного пользователя")
    void adminOnlyServiceMethodIsBlocked() {
        // id заведомо не существует: если бы @PreAuthorize не сработал, метод упал бы
        // на поиске города, но данные всё равно не изменились бы.
        assertThatThrownBy(() -> cityServiceI.setCityRelevant(-1L))
                .isInstanceOf(AccessDeniedException.class);
    }

    // ---------- хеш пароля не уходит в шаблон ----------

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Страница пользователя в админке получает UserDTO, а не entity с паролем")
    void adminUserPageExposesDtoWithoutPassword() throws Exception {
        String existing = "TestUser";
        Assumptions.assumeTrue(userServiceI.existsUserByUsername(existing),
                "в базе нет пользователя " + existing + " — тест пропущен");

        var result = mockMvc.perform(get("/admin/users/selectedUser/" + existing))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selectedUser"))
                .andReturn();

        Object selectedUser = result.getModelAndView().getModel().get("selectedUser");
        assertThat(selectedUser).isInstanceOf(UserDTO.class);
        assertThat(result.getResponse().getContentAsString()).doesNotContain("$2a$");
    }
}
