package com.yingliguoji.fee.job;


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
    private MemberMapper memberMapper;


    @Autowired
    private FeeService feeService;

    @Scheduled(cron = "0 03 * * * ?")

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

        DateTime endDate = new DateTime().withMillisOfSecond(0).withMinuteOfHour(0);
        Long endDateMills = endDate.getMillis();
        Integer end = new Long(endDateMills /1000l).intValue();

        List<MemberPo> memberPoList = memberMapper.selectAll();
        memberPoList.forEach(memberPo -> {
            Integer memberId = memberPo.getId();
            Long startTime= feeService.getMinTimeForMember(memberId);
            Integer start = new Long(startTime /1000l).intValue();
            feeService.backFeeToAgent(memberId, start, end);
        });
    }

}
