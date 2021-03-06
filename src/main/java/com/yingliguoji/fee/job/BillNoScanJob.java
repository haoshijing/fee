package com.yingliguoji.fee.job;

import com.yingliguoji.fee.dao.GameRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class BillNoScanJob {

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Scheduled(cron = "3/10 * * * * ?")

    public void execute() {
        try {
            work();
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    private void work() {
        List<String> billNos = gameRecordMapper.querySameBillNo();
        for(String billNo : billNos){
            log.info("billNo = {}", billNo);
            Integer id = gameRecordMapper.selectByBillNo(billNo);
            if(id != null){
                Integer deleteRet = gameRecordMapper.deleteById(id);
                log.info("id = {}, deleteRet = {}", id,deleteRet);
            }
        }


    }
}
