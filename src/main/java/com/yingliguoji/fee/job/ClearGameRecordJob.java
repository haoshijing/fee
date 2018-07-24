package com.yingliguoji.fee.job;

import com.yingliguoji.fee.dao.GameRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author haoshijing
 * @version 2018年05月31日 13:22
 **/
@Service
@Slf4j
public class ClearGameRecordJob {

    @Autowired
    private GameRecordMapper gameRecordMapper;
    @Scheduled(cron = "0 0 1 * * ?")
    public void execute() {
        try {
            DateTime dateTime = new DateTime().plusDays(-37);
            String date =  new StringBuilder(dateTime.getYear()).append("-")
                    .append(dateTime.getMonthOfYear()).append("-")
                    .append(dateTime.getDayOfMonth()).toString();
            gameRecordMapper.clearData(date);

        } catch (Throwable e) {
            log.error("", e);
        }
    }
}
