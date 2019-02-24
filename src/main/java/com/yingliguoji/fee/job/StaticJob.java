package com.yingliguoji.fee.job;


import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.service.FeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@Slf4j
public class StaticJob {

    @Autowired
    private GameRecordMapper gameRecordMapper;


    @Autowired
    private FeeService feeService;

    @Scheduled(cron = "0 0 1 * * ?")

    public void execute() {
        try {
            feeService.updateReAmount();
            log.info("start work");
            work();
            log.info(" end work");
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    private void work() {

        DateTime endDate = new DateTime().withHourOfDay(0).withMillisOfSecond(0).withMinuteOfHour(0);
        Long endDateMills = endDate.getMillis();
        Integer end = new Long(endDateMills /1000l).intValue();
        Integer start = new Long(endDate.plusDays(-1).getMillis()/1000).intValue();

        List<Integer> memberIds = gameRecordMapper.queryBetClient();
    }

}
