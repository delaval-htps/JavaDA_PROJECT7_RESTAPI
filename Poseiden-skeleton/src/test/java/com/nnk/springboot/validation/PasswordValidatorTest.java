package com.nnk.springboot.validation;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
 
    @Parameterized.Parameters
    public static Collection data() {
        return (Arrays.asList(new Object[][] { { "Jsadmin4all&lp4e", true }, { "Unpa sword2", false } }));
    }

    public PasswordValidatorTest(String password, Boolean expectedResult) {
        this.password = password;
        this.expectedResult = expectedResult;
    }

    @Test
    public void test_password_regex_valid() {
        System.out.println("password= "+password);
       Boolean result = cut.isValid(password, null);
       System.out.println(result.equals(expectedResult));
       assertEquals(result, expectedResult);
    }

}
