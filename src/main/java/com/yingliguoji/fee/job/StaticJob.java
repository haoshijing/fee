package com.yingliguoji.fee.job;

import com.yingliguoji.fee.ApplicationStarter;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.service.GameRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.event.EventListener;
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
    private GameRecordService gameRecordService;

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
                List<MemberPo> memberPoList = memberMapper.selectAll();
                memberPoList.forEach(memberPo -> {
                    Integer memberId = memberPo.getId();
                    gameRecordService.calMemberBet(memberId);
                });

            }
        },1,3000, TimeUnit.SECONDS);
    }

}
