package com.yingliguoji.fee.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.response.PlayerRecordRequest;
import com.yingliguoji.fee.controller.response.PlayerRecordTotalVo;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.service.GameRecordService;
import com.yingliguoji.fee.service.MemberService;
import com.yingliguoji.fee.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class PlayRecordsController {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private GameRecordService gameRecordService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Value("${cpHost}")
    private String cpHost;

    @Value("${cpMerchantId}")
    private String cpMerchantId;

    @Value("${cpSafeCode}")
    private String cpSafeCode;

    @RequestMapping("/updateTie")
    public ApiResponse<Boolean> updateTie(String name,String tie){
        JSONObject jsonObject = new JSONObject();
        log.info("name= {},tie = {}",name,tie);
        jsonObject.put("MerchantId",cpMerchantId);
        jsonObject.put("UserName",name);
        String point = new DecimalFormat("0.00").format(Double.valueOf(tie));
        jsonObject.put("Point",point);
        jsonObject.put("Time",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        String signKeyStr =cpMerchantId+"&"+name+"&"+point+"&"+jsonObject.getString("Time")+"&"+cpSafeCode;
        String signKey = MD5Util.md5(signKeyStr.toLowerCase());
        jsonObject.put("SignKey",signKey);
        try {
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(cpHost));
            httpPost.addHeader("Content-Type","application/json");
            httpPost.setEntity(new StringEntity(jsonObject.toJSONString()));
            CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            log.info("result = {}",result);
        }catch (Exception e){
            log.error("",e);
        }
        return new ApiResponse<>(true);
    }


    @RequestMapping("/underMember")
    public ApiResponse<List<Integer>> getUnderList(Integer proxyId){
        List<Integer> ids  = memberService.getMemberIds(proxyId).stream()
                .filter(memberPo -> {
                    return memberPo != null;
                }).map(memberPo -> {
                    return memberPo.getId();
                }).collect(Collectors.toList());
        return new ApiResponse<>(ids);
    }

    @RequestMapping("/getPlayerRecordTotal")
    public ApiResponse<List<PlayerRecordTotalVo>> getPlayerRecordTotal(@RequestBody PlayerRecordRequest recordRequest) {

        List<ClassifyPo> classifyPos = classifyMapper.selectAll();
        Integer proxyId = recordRequest.getProxyId();

        if (proxyId == null) {
            return new ApiResponse<>(Lists.newArrayList());
        }
        List<Integer> memberIds = memberService.getMemberIds(proxyId).stream()
                .filter(memberPo -> {
                    return memberPo != null;
                }).map(memberPo -> {
                    return memberPo.getId();
                }).collect(Collectors.toList());
        List<PlayerRecordTotalVo> recordTotalVos = memberIds.stream().map(
                memberId -> {
                    PlayerRecordTotalVo recordTotalVo = new PlayerRecordTotalVo();
                    MemberPo memberPo = memberMapper.findById(memberId);
                    if(memberPo != null){
                        recordTotalVo.setName(memberPo.getName());
                        recordTotalVo.setRealName(memberPo.getReal_name());
                    }
                    List<PlayerRecordTotalVo.ClassiFyItem> items =
                            classifyPos.stream().map(classifyPo -> {
                                PlayerRecordTotalVo.ClassiFyItem item = new PlayerRecordTotalVo.ClassiFyItem();
                                item.setClassiFyId(classifyPo.getId());
                                item.setName(classifyPo.getName());
                                List<Integer> gameTypes = getGameTypes(classifyPo);
                                List<Integer> queryMembers = Lists.newArrayList(memberId);
                                BigDecimal bigDecimal = gameRecordService.getReAmountTotal(queryMembers, gameTypes,
                                        recordRequest.getStart(), recordRequest.getEnd());
                                BigDecimal totalMoney = gameRecordService.getTotalValidBet(queryMembers, gameTypes,
                                        recordRequest.getStart(), recordRequest.getEnd());
                                item.setMoney(bigDecimal.doubleValue());
                                item.setBetMoney(totalMoney.doubleValue());
                                return item;
                            }).collect(Collectors.toList());
                    recordTotalVo.setMemberId(memberId);
                    recordTotalVo.setClassiFyItemList(items);

                    return recordTotalVo;
                }
        ).collect(Collectors.toList());
        return new ApiResponse(recordTotalVos);
    }

    private List<Integer> getGameTypes(ClassifyPo classifyPo) {
        String type = classifyPo.getSmallType();
        String[] typeArr = type.split(",");
        List<Integer> gameTypes = Lists.newArrayList();
        for (String typeStr : typeArr) {
            gameTypes.add(Integer.valueOf(typeStr));
        }
        return gameTypes;
    }
}
