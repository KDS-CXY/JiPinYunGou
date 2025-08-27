package com.baizhanshopping.shopping_manager_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class EncodeTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void testaa(){
        String baizhan = passwordEncoder.encode("baizhan");
        System.out.println(baizhan);
    }
}
