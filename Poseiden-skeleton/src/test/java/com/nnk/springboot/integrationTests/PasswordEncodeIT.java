package com.nnk.springboot.integrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 09/03/2019
 * Time: 11:26 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class PasswordEncodeIT {
    @Test
    public void testPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pw = encoder.encode("Jsadmin4all&lp4e");
        System.out.println("[ "+ pw + " ]");
    }
}
