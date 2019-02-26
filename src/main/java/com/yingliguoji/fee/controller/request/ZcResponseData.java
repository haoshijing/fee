/*
 * @(#) ZcResponseData.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.controller.request;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.enums.GameType;
import com.yingliguoji.fee.po.js.ZcSumPo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Data
public class ZcResponseData {
    private List<ZcResponseListData> zcResponseListData;
    private List<GameZcData> gameZcDataList;

    public ZcResponseData() {
        gameZcDataList = Lists.newArrayList();
        gameZcDataList.add(new GameZcData(GameType.ZR, "真人", 0.0, 0.0));
        gameZcDataList.add(new GameZcData(GameType.DZ, "电子", 0.0, 0.0));
        gameZcDataList.add(new GameZcData(GameType.CP, "彩票", 0.0, 0.0));
        gameZcDataList.add(new GameZcData(GameType.TY, "体育", 0.0, 0.0));
        gameZcDataList.add(new GameZcData(GameType.DJ, "电竞", 0.0, 0.0));
        gameZcDataList.add(new GameZcData(GameType.BY, "捕鱼", 0.0, 0.0));
        gameZcDataList.add(new GameZcData(GameType.QP, "棋牌", 0.0, 0.0));
    }

    @Data
    public static class ZcResponseListData {
        private Integer quota;
        private String name;
        private String realName;
        private Double yk;
        private Integer memberId;
        private List<GameZcData> gameZcDataList = Lists.newArrayList();

        public ZcResponseListData() {
            gameZcDataList.add(new GameZcData(GameType.ZR, "真人", 0.0, 0.0));
            gameZcDataList.add(new GameZcData(GameType.DZ, "电子", 0.0, 0.0));
            gameZcDataList.add(new GameZcData(GameType.CP, "彩票", 0.0, 0.0));
            gameZcDataList.add(new GameZcData(GameType.TY, "体育", 0.0, 0.0));
            gameZcDataList.add(new GameZcData(GameType.DJ, "电竞", 0.0, 0.0));
            gameZcDataList.add(new GameZcData(GameType.BY, "捕鱼", 0.0, 0.0));
            gameZcDataList.add(new GameZcData(GameType.QP, "棋牌", 0.0, 0.0));
        }

        public void resetData(ZcSumPo zcSumPo) {
            Optional<GameZcData> optional = gameZcDataList.stream()
                    .filter(gameZcData -> gameZcData.getGameType().equals(zcSumPo.getGameType()))
                    .findFirst();
            if (optional.isPresent()) {
                optional.get().setGameYc(zcSumPo.getYcAmount().doubleValue());
                optional.get().setGameYc(zcSumPo.getBackAmount().doubleValue());
            }
        }
    }

    public void addGamePo(ZcSumPo zcSumPo) {
        Optional<GameZcData> optional = gameZcDataList.stream()
                .filter(gameZcData -> gameZcData.getGameType().equals(zcSumPo.getGameType())).
                        findFirst();
        if (optional.isPresent()) {
            GameZcData gameZcData = optional.get();
            gameZcData.setGameYc(gameZcData.getGameYc() + zcSumPo.getYcAmount().doubleValue());
            gameZcData.setGameBack(gameZcData.getGameBack() + zcSumPo.getBackAmount().doubleValue());
        }
    }

    @Data
    @AllArgsConstructor
    public static class GameZcData {
        private Integer gameType;
        private String gameName;
        private Double gameYc;
        private Double gameBack;
    }
}
