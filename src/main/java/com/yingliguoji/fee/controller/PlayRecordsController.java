package com.yingliguoji.fee.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PlayRecordsController {


//    @Autowired
//    private GameRecordService gameRecordService;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private MemberMapper memberMapper;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Value("${cpHost}")
//    private String cpHost;
//
//    @Value("${cpMerchantId}")
//    private String cpMerchantId;
//
//    @Value("${cpSafeCode}")
//    private String cpSafeCode;
//
//    @RequestMapping("/updateTie")
//    public ApiResponse<Boolean> updateTie(String name, String tie) {
//        String result = doSend(name, tie);
//        JSONObject jsonObject = JSON.parseObject(result);
//        if (jsonObject != null) {
//            String code = jsonObject.getString("code");
//            if (StringUtils.equals("200", code)) {
//                doSend(name, tie);
//            }
//        }
//        return new ApiResponse<>(true);
//    }
//
//
//    @RequestMapping("/underMember")
//    public ApiResponse<List<Integer>> getUnderList(Integer proxyId) {
//        List<Integer> ids = memberService.getMemberIds(proxyId).stream().filter(memberPo -> {
//            return memberPo != null;
//        }).map(memberPo -> {
//            return memberPo.getId();
//        }).collect(Collectors.toList());
//        return new ApiResponse<>(ids);
//    }
//
//    @RequestMapping("/getPlayerRecordTotal")
//    public ApiResponse<UnderPlayerRecordDataVo> getPlayerRecordTotal(@RequestBody PlayerRecordRequest recordRequest) {
//        UnderPlayerRecordDataVo underPlayerRecordDataVo = new UnderPlayerRecordDataVo();
//        Integer type = recordRequest.getType();
//        Integer proxyId = recordRequest.getProxyId();
//        String playerName = recordRequest.getPlayerName();
//
//        Integer branchId = recordRequest.getBranchId();
//
//        if (proxyId == null && type == null) {
//            return new ApiResponse<>(underPlayerRecordDataVo);
//        }
//
//        List<Integer> memberIds = null;
//        if (type == null || proxyId != null) {
//            memberIds = memberService.getMemberIds(proxyId).stream().filter(memberPo -> memberPo != null).map(memberPo -> memberPo.getId()).collect(Collectors.toList());
//        } else {
//            UserPo userPo = userMapper.selectById(branchId);
//            MemberPo queryPo = new MemberPo();
//            if (userPo != null && userPo.getIs_super_admin() == 2) {
//                queryPo.setBranch_id(branchId);
//            }
//            if (StringUtils.isNotEmpty(playerName)) {
//                queryPo.setName(playerName);
//            }
//            memberIds = memberMapper.selectList(queryPo).stream().filter(memberPo -> memberPo != null).map(memberPo -> memberPo.getId()).collect(Collectors.toList());
//        } if (type != null && proxyId != null) {
//            memberIds.add(proxyId);
//        }
//        List<PlayerRecordTotalVo> recordTotalVos = memberIds.stream().map(memberId -> {
//            PlayerRecordTotalVo recordTotalVo = new PlayerRecordTotalVo();
//            MemberPo memberPo = memberMapper.findById(memberId);
//            if (memberPo != null) {
//                recordTotalVo.setName(memberPo.getName());
//                recordTotalVo.setRealName(memberPo.getReal_name());
//            }
//            List<PlayerRecordTotalVo.ClassiFyItem> items = classifyPos.stream().map(classifyPo -> {
//                PlayerRecordTotalVo.ClassiFyItem item = new PlayerRecordTotalVo.ClassiFyItem();
//                item.setClassiFyId(classifyPo.getId());
//                item.setName(classifyPo.getName());
//                List<Integer> gameTypes = getGameTypes(classifyPo);
//                List<Integer> queryMembers = Lists.newArrayList(memberId);
//                BigDecimal bigDecimal = gameRecordService.getReAmountTotal(queryMembers, gameTypes, recordRequest.getStart(), recordRequest.getEnd());
//                BigDecimal totalMoney = gameRecordService.getTotalValidBet(queryMembers, gameTypes, recordRequest.getStart(), recordRequest.getEnd());
//                item.setMoney(bigDecimal.doubleValue());
//                item.setBetMoney(totalMoney.doubleValue());
//                return item;
//            }).collect(Collectors.toList());
//            recordTotalVo.setMemberId(memberId);
//            recordTotalVo.setClassiFyItemList(items);
//
//            return recordTotalVo;
//        }).collect(Collectors.toList());
//        underPlayerRecordDataVo.setDetails(recordTotalVos);
//        PlayerRecordTotalVo totalVo = new PlayerRecordTotalVo();
//        PlayerRecordTotalVo.ClassiFyItem sportClassiFyItem = new PlayerRecordTotalVo.ClassiFyItem();
//        PlayerRecordTotalVo.ClassiFyItem zrClassiFyItem = new PlayerRecordTotalVo.ClassiFyItem();
//        PlayerRecordTotalVo.ClassiFyItem dzClassiFyItem = new PlayerRecordTotalVo.ClassiFyItem();
//        PlayerRecordTotalVo.ClassiFyItem cpClassiFyItem = new PlayerRecordTotalVo.ClassiFyItem();
//        recordTotalVos.forEach(playerRecordTotalVo -> {
//            sportClassiFyItem.setMoney(playerRecordTotalVo.getClassiFyItemList().get(0).getMoney() + sportClassiFyItem.getMoney());
//            sportClassiFyItem.setBetMoney(playerRecordTotalVo.getClassiFyItemList().get(0).getBetMoney() + sportClassiFyItem.getBetMoney());
//
//            zrClassiFyItem.setMoney(playerRecordTotalVo.getClassiFyItemList().get(1).getMoney() + zrClassiFyItem.getMoney());
//            zrClassiFyItem.setBetMoney(playerRecordTotalVo.getClassiFyItemList().get(1).getBetMoney() + zrClassiFyItem.getBetMoney());
//
//            dzClassiFyItem.setMoney(playerRecordTotalVo.getClassiFyItemList().get(2).getMoney() + dzClassiFyItem.getMoney());
//            dzClassiFyItem.setBetMoney(playerRecordTotalVo.getClassiFyItemList().get(2).getBetMoney() + dzClassiFyItem.getBetMoney());
//
//
//            cpClassiFyItem.setMoney(playerRecordTotalVo.getClassiFyItemList().get(3).getMoney() + cpClassiFyItem.getMoney());
//            cpClassiFyItem.setBetMoney(playerRecordTotalVo.getClassiFyItemList().get(3).getBetMoney() + cpClassiFyItem.getBetMoney());
//        });
//        totalVo.setClassiFyItemList(Lists.newArrayList(sportClassiFyItem, zrClassiFyItem, dzClassiFyItem, cpClassiFyItem));
//        underPlayerRecordDataVo.setTotal(totalVo);
//        return new ApiResponse(underPlayerRecordDataVo);
//    }
//
//    private List<Integer> getGameTypes(ClassifyPo classifyPo) {
//        String type = classifyPo.getSmallType();
//        String[] typeArr = type.split(",");
//        List<Integer> gameTypes = Lists.newArrayList();
//        for (String typeStr : typeArr) {
//            gameTypes.add(Integer.valueOf(typeStr));
//        }
//        return gameTypes;
//    }
//
//    private String doSend(String name, String tie) {
//        JSONObject jsonObject = new JSONObject();
//        log.info("name= {},tie = {}", name, tie);
//        jsonObject.put("MerchantId", cpMerchantId);
//        jsonObject.put("UserName", name);
//        String point = new DecimalFormat("0.00").format(Double.valueOf(tie));
//        jsonObject.put("Point", point);
//        jsonObject.put("Time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//
//        String signKeyStr = cpMerchantId + "&" + name + "&" + point + "&" + jsonObject.getString("Time") + "&" + cpSafeCode;
//        String signKey = MD5Util.md5(signKeyStr.toLowerCase());
//        jsonObject.put("SignKey", signKey);
//        try {
//            HttpPost httpPost = new HttpPost();
//            httpPost.setURI(new URI(cpHost));
//            httpPost.addHeader("Content-Type", "application/json");
//            httpPost.setEntity(new StringEntity(jsonObject.toJSONString()));
//            CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//            StringBuilder result = new StringBuilder();
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }
//            log.info("result = {}", result);
//            return result.toString();
//        } catch (Exception e) {
//            return "";
//        }
//
//    }
}
