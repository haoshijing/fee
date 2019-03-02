package com.yingliguoji.fee.service;

import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.po.GameRecordPo;
import com.yingliguoji.fee.po.js.MemberGamePo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class GameRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;




    @Value("${fireData}")
    private Integer fireData;

    public List<MemberGamePo> queryMemberGamePos(GameRecordPo queryPo) {
        return gameRecordMapper.queryMemberGamePos(queryPo);
    }


    @Data
    private class QueryDataVo{
        private List<Integer> memberIds;
        private String startTime;
        private String endTime;
        private List<Integer> gameTypes;
    }
}
