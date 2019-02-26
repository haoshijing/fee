package com.yingliguoji.test;

import com.yingliguoji.fee.service.FsZcService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.yingliguoji.fee.ApplicationStarter.class, properties = "spring.profiles.active=dev")
public class BaseApiTest {

    @Autowired
    private FsZcService fsZcService;

    @Test
    public void testJs() {
        DateTime endDateTime = new DateTime().
                withTime(0, 0, 0, 0);
        DateTime startDateTime = endDateTime.plusDays(-1);
        fsZcService.backMoney(startDateTime, endDateTime);
    }
}
