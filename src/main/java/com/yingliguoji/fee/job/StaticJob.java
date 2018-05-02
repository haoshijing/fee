package com.yingliguoji.fee.job;


import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.service.FeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class StaticJob {

    @Autowired
    private MemberMapper memberMapper;


    @Autowired
    private FeeService feeService;

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init(){
        try {
            work();
        }catch (Throwable e){
            log.error("",e);
        }
    }

    private void work(){

        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                DateTime endDate = new DateTime().withTime(0,0,0,0);
                Long end = endDate.getMillis();
                Long start = endDate.plusDays(-1).getMillis();

                List<MemberPo> memberPoList = memberMapper.selectAll();
                memberPoList.forEach(memberPo -> {
                    Integer memberId = memberPo.getId();
                    feeService.backFeeToAgent(memberId,start,end);
                });

            }
        },1,20, TimeUnit.MINUTES);
    }

}
