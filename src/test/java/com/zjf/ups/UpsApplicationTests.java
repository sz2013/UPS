package com.zjf.ups;

import com.zjf.ups.service.UpsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UpsApplicationTests {

    @Autowired
    private UpsService upsService;

    @Test
    void contextLoads() {
    }

    @Test
    void testOnlineStatusChecker() {
        upsService.OnlineStatusChecker();
    }

}
