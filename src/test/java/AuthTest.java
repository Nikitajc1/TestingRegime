import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
    @BeforeEach
    void openChrome() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//*[@id='root']/div/h2").shouldBe(Condition.appear).getText().equals("Личный кабинет");
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGeneration.Registration.getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGeneration.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.exactText("Ошибка! Пользователь заблокирован")).shouldBe(Condition.appear);
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        var wrongLogin = DataGeneration.generatedLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        var wrongPassword = DataGeneration.generatedLogin();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }


    @Test
    void testWhenPasswordIsEmpty() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue("");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=password].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.appear);
    }

    @Test
    void testWhenLoginIsEmpty() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").setValue("");
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=login].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.appear);
    }
}
