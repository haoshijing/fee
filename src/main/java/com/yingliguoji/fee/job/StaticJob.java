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

    @PostConstruct
    public void init(){
        work();
    }

    @Scheduled(cron = "0 30 * * * ?")
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
        DateTime endDate = new DateTime().withTime(0, 0, 0, 0);
        Long end = endDate.getMillis();
        Long start = endDate.plusDays(-1).getMillis();

        List<MemberPo> memberPoList = memberMapper.selectAll();
        memberPoList.forEach(memberPo -> {
            Integer memberId = memberPo.getId();
            feeService.backFeeToAgent(memberId, start, end);
        });
    }

}
