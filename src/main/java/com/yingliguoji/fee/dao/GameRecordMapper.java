package com.yingliguoji.fee.dao;

import java.math.BigDecimal;
import java.util.Map;

public interface GameRecordMapper {

    BigDecimal getPlayerTotal(Map<String,Object> data);

    BigDecimal getValidBetTotal(Map<String,Object> data);

}


