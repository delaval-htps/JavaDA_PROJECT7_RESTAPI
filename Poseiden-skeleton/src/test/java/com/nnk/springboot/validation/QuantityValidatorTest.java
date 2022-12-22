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
public class QuantityValidatorTest {

    private QuantityValidator cut;

    private final Double value;

    private final Boolean expectedResult;

    @Before
    public void initialize() {
        cut = new QuantityValidator();
    }

    @Parameterized.Parameters(name = "{index}: \"{0}\" is valid quantity ? : {1}")
    public static Collection<Object[]> data() {
        return (Arrays.asList(new Object[][] {
                // positive double with 4 digits before point and one after
                { 1000.0, true },
                // positive double with at least 1 digits before point and one after
                { 0.1, true },
                // positive double with 4 digits before point and two after
                { 1000.01, false },
                // positive double with 5digit before point
                { 10000.0, false },
                // positive double with 5digit before point
                { 0.0, false },
                // negative double with maximum 4 digits before point and only one after
                { -999.9, false },

        }));
    }

    public QuantityValidatorTest(Double value, Boolean expectedResult) {
        this.value = value;
        this.expectedResult = expectedResult;
    }

    @Test
    public void test_quantity_regex_valid() {
        System.out.println("value= " + value);
        Boolean result = cut.isValid(value, null);
        // System.out.println(result.equals(expectedResult));
        assertEquals(result, expectedResult);
    }

}
