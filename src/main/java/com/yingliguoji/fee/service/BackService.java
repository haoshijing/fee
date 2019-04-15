package com.yingliguoji.fee.service;

import com.yingliguoji.fee.dao.DividendMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.DividendPo;
import com.yingliguoji.fee.po.MemberPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BackService {

    @Autowired
    private DividendMapper dividendMapper;

    @Autowired
    private MemberMapper memberMapper;

    public void backMoney() {

        List<DividendPo> dividendPos = dividendMapper.queryList(3);

        List<DividendPo> updatePos = dividendPos.stream().map(dividendPo -> {
            DividendPo updatePo = new DividendPo();
            MemberPo memberPo = memberMapper.findById(dividendPo.getMemberId());
            if (memberPo == null || dividendPo.getMoney().intValue() == 0) {
                log.info("memberId = {}", dividendPo.getMemberId());
                return null;
            }
            updatePo.setMemberId(dividendPo.getMemberId());
            updatePo.setMoney(dividendPo.getMoney().multiply(new BigDecimal(-1)));
            updatePo.setMemberId(dividendPo.getMemberId());
            updatePo.setStatus(dividendPo.getStatus());
            updatePo.setDescribe("反水对冲");
            updatePo.setType(4);
            updatePo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            updatePo.setBeforeMoney(memberPo.getMoney());
            MemberPo updateMemberPo = new MemberPo();
            updateMemberPo.setMoney(memberPo.getMoney().add(updatePo.getMoney()));
            updateMemberPo.setId(memberPo.getId());

            memberMapper.update(updateMemberPo);

            memberPo = memberMapper.findById(dividendPo.getMemberId());
            updatePo.setAfterMoney(memberPo.getMoney());

            return updatePo;
        }).filter(dividendPo -> dividendPo != null).collect(Collectors.toList());

        dividendMapper.batchInsertData(updatePos);
    }
}
