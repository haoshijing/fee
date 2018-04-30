package com.yingliguoji.fee.job;

import com.yingliguoji.fee.ApplicationStarter;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import com.yingliguoji.fee.po.MemberPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class StaticJob {

    @Autowired
    private MemberMapper memberMapper;

    private ClassifyMapper classifyMapper;

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    @EventListener
    public void work(ApplicationStartingEvent event){
        work();
    }

    private void work(){

        List<ClassifyPo> classifyPos = classifyMapper.selectAll();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                List<MemberPo> memberPoList = memberMapper.selectAll();
                memberPoList.forEach(memberPo -> {
                    Integer memberId = memberPo.getId();

                });

            }
        },1,3, TimeUnit.SECONDS);
    }

}
