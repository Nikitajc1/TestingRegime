import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class DataGeneration {
    public static RegistrationDto generateInfo(String locale) {
        Faker faker = new Faker(new Locale(locale));
        String status = "active";

        return new RegistrationDto(
                faker.name().firstName(),
                faker.name().lastName(),
                status
        );
    }

    public String login() {
        return generateInfo("eng").getLogin();
    }

    public String password() {
        return generateInfo("eng").getPassword();
    }
}
