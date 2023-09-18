package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initService() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegistrationPageThenGetPageWithRegister() {
        var view = userController.getRegistrationPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterNewUserThenRedirectIndex() {
        var user = new User(1, "email", "name", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenRegisterNewUserThenRedirectRegisterPage() {
        var expectedException = new RuntimeException("User with this email already exists");
        when(userService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view  = userController.register(model, new User());
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("users/register");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestLoginPageThenGetPageWithLogin() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenRedirectVacanciesPage() {
        var user = new User(1, "email", "name", "password");
        var emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(userService.findByEmailAndPassword(emailArgumentCaptor.capture(),
                passwordArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, new MockHttpServletRequest());
        var actualEmail = emailArgumentCaptor.getValue();
        var actualPassword = passwordArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualEmail).isEqualTo("email");
        assertThat(actualPassword).isEqualTo("password");
    }

    @Test
    public void whenLoginUserThenGetLoginPage() {
        var expectedException = new RuntimeException("Email or password entered incorrectly");
        when(userService.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUser(new User(), model, new MockHttpServletRequest());
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenLogoutUserThenRedirectUsersLogin() {
        var view = userController.logout(new MockHttpSession());
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}
