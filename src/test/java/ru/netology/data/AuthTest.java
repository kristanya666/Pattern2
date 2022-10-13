package ru.netology.data;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;

import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        String login = registeredUser.getLogin();
        String password = registeredUser.getPassword();

        //заполнение формы входа в личный кабинет валидными данными
        SelenideElement form = $("form.form");
        form.$("[data-test-id=login] input").setValue(login);
        form.$("[data-test-id=password] input").setValue(password);
        form.$("[data-test-id=action-login]").click();

        //проверка входа в лк
        $("h2.heading").shouldHave(Condition.exactText("  Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        //заполнение формы входа в личный кабинет невалидными данными
        SelenideElement form = $("form.form");
        form.$("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        form.$("[data-test-id=action-login]").click();

        //проверка появления ошибки
        SelenideElement notification = $("div.notification");
        $(withText("Ошибка!")).shouldBe(Condition.visible, Duration.ofSeconds(8));
        notification.$("div.notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        //заполнение формы входа в личный кабинет заблокированным пользователем
        SelenideElement form = $("form.form");
        form.$("[data-test-id=login] input").setValue(blockedUser.getLogin());
        form.$("[data-test-id=password] input").setValue(blockedUser.getPassword());
        form.$("[data-test-id=action-login]").click();


        //проверка появления ошибки о заблокированном пользователе
        SelenideElement notification = $("div.notification");
        $(withText("Ошибка!")).shouldBe(Condition.visible, Duration.ofSeconds(8));
        notification.$("div.notification__content").shouldHave(Condition.text("Пользователь заблокирован"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();



        //заполнение формы входа в личный кабинет невалидным логином, но валидным паролем
        SelenideElement form = $("form.form");
        form.$("[data-test-id=login] input").setValue(wrongLogin);
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();


        //проверка появления ошибки
        SelenideElement notification = $("div.notification");
        $(withText("Ошибка!")).shouldBe(Condition.visible, Duration.ofSeconds(8));
        notification.$("div.notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        System.out.println(wrongPassword);


        //заполнение формы входа в личный кабинет невалидным паролем, но валидным логином
        SelenideElement form = $("form.form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(wrongPassword);
        form.$("[data-test-id=action-login]").click();


        //проверка появления ошибки
        SelenideElement notification = $("div.notification");
        $(withText("Ошибка!")).shouldBe(Condition.visible, Duration.ofSeconds(8));
        notification.$("div.notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"));

    }

}
