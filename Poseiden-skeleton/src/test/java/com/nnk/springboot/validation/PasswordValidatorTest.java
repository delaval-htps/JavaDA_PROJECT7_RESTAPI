package com.nnk.springboot.validation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(Parameterized.class)
@Import(LocalValidatorFactoryBean.class)
public class PasswordValidatorTest {

    private PasswordValidator cut;

    private final String password;

    private final Boolean expectedResult;

    @Before
    public void initialize() {
        cut = new PasswordValidator();
    }

    @Parameterized.Parameters(name = "{index}: \"{0}\" is valid password ? : {1}")
    public static Collection<Object[]> data() {
        return (Arrays.asList(new Object[][] {
                // with at least 8 characters , 1 Uppercase, 1 digit, 1 symbol
                { "Jsadmi#4", true }, { "Jsadm&n4", true }, { "Jsad(in4", true }, { "Jsa)min4", true }, { "Js-dmin4", true }, { "Js@dmin4", true }, { "J[admin4", true }, { "{Sadmin4", true },
                { "Jsd:min4", true }, { "J]admin4", true }, { "{Sadmin4", true }, { "}5adminT", true }, { "jsAd/in4", true }, { "jSad*in4", true }, { ",5adminT", true }, { "jsAdm?n4", true },
                { "4Sadmin~", true }, { "J5admin$", true }, { "jsd^mIn4", true }, { "jSad+in4", true }, { "jsAd<in4", true }, { "jSad>in4", true },{ "jSad_i&4", true },{ "jSad=in4", true },
                // 1 uppercase, 1 digit, 1 symbol, 0 space, characters <8
                { "Jsad/i4", false },
                // O uppercase, 1digit, 1 symbol at least 8 characters
                { "jsad/im4", false },
                // 1 uppercase, 0 digit, 1 symbol at least 8 characters
                { "Jsadmin*", false },
                // 1 uppercase, 1 digit, 0 symbol at least 8 characters
                { "Jsa8mine", false },
                // 1 uppercase, 1 digit, 1 symbol, 1 space at least 8 characters
                { "Jsa mi'4", false }

        }));
    }

    public PasswordValidatorTest(String password, Boolean expectedResult) {
        this.password = password;
        this.expectedResult = expectedResult;
    }

    @Test
    public void test_password_regex_valid() {
        // System.out.println("password= "+password);
        Boolean result = cut.isValid(password, null);
        // System.out.println(result.equals(expectedResult));
        assertEquals(result, expectedResult);
    }

}
