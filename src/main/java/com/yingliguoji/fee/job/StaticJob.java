package com.yingliguoji.fee.job;


import com.yingliguoji.fee.service.FsZcService;
import com.yingliguoji.fee.service.MoneyFeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class StaticJob {

    @Autowired
    private FsZcService fsZcService;

    @Autowired
    private MoneyFeeService moneyFeeService;

    @Scheduled(cron = "0 0 1 * * ?")

    public void execute() {
        try {
            log.info("start work");
            work();

            log.info(" end work");
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    private void work() {
        for (int i = 0; i < 10; i++) {
            DateTime endDatetime = new DateTime().plusDays(0 - i).withHourOfDay(0).withMillisOfSecond(0).withMinuteOfHour(0);
            DateTime startDateTime = endDatetime.plusDays(-1);
            fsZcService.backMoney(startDateTime, endDatetime);
        }
        DateTime endDatetime = new DateTime().withHourOfDay(0).withMillisOfSecond(0).withMinuteOfHour(0);
        DateTime startDateTime = endDatetime.plusDays(-1);
        moneyFeeService.handlerFee(startDateTime, endDatetime);
    }

}
