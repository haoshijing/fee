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
    private Double data;

    public ZcResponseData() {
        gameZcDataList = Lists.newArrayList();
        setData(0.0);
        gameZcDataList.add(new GameZcData(GameType.ZR, "真人", 0.0));
        gameZcDataList.add(new GameZcData(GameType.DZ, "电子", 0.0));
        gameZcDataList.add(new GameZcData(GameType.CP, "彩票", 0.0));
        gameZcDataList.add(new GameZcData(GameType.TY, "体育", 0.0));
        gameZcDataList.add(new GameZcData(GameType.DJ, "电竞", 0.0));
        gameZcDataList.add(new GameZcData(GameType.BY, "捕鱼", 0.0));
        gameZcDataList.add(new GameZcData(GameType.QP, "棋牌", 0.0));
    }

    public void addGamePo(ZcSumPo zcSumPo, int queryType) {
        Optional<GameZcData> optional = gameZcDataList.stream()
                .filter(gameZcData -> gameZcData.getGameType().equals(zcSumPo.getGameType())).
                        findFirst();
        if (optional.isPresent()) {
            GameZcData gameZcData = optional.get();
            if (queryType == 1) {
                gameZcData.setData(gameZcData.getData() + zcSumPo.getYkAmount().doubleValue());
                setData(getData() + zcSumPo.getYkAmount().doubleValue());
            } else if (queryType == 2) {
                gameZcData.setData(gameZcData.getData() + zcSumPo.getValidBetAmount().doubleValue());
                setData(getData() + zcSumPo.getValidBetAmount().doubleValue());
            } else if (queryType == 3) {
                gameZcData.setData(gameZcData.getData() + zcSumPo.getBetAmount().doubleValue());
                setData(getData() + zcSumPo.getBetAmount().doubleValue());
            } else if (queryType == 4) {
                gameZcData.setData(gameZcData.getData() + zcSumPo.getFsAmount().doubleValue());
                setData(getData() + zcSumPo.getFsAmount().doubleValue());
            } else if (queryType == 5) {
                gameZcData.setData(gameZcData.getData() + zcSumPo.getZcAmount().doubleValue());
                setData(getData() + zcSumPo.getZcAmount().doubleValue());
            } else if (queryType == 6) {
                gameZcData.setData(gameZcData.getData() + zcSumPo.getNetAmount().doubleValue());
                setData(getData() + zcSumPo.getNetAmount().doubleValue());
            }
        }
    }

    @Data
    public static class ZcResponseListData {
        private Integer quota;
        private String name;
        private String realName;
        private Double data;
        private Integer memberId;
        private List<GameZcData> gameZcDataList = Lists.newArrayList();

        public ZcResponseListData() {
            this.setData(0.0);
            gameZcDataList.add(new GameZcData(GameType.ZR, "真人", 0.0));
            gameZcDataList.add(new GameZcData(GameType.DZ, "电子", 0.0));
            gameZcDataList.add(new GameZcData(GameType.CP, "彩票", 0.0));
            gameZcDataList.add(new GameZcData(GameType.TY, "体育", 0.0));
            gameZcDataList.add(new GameZcData(GameType.DJ, "电竞", 0.0));
            gameZcDataList.add(new GameZcData(GameType.BY, "捕鱼", 0.0));
            gameZcDataList.add(new GameZcData(GameType.QP, "棋牌", 0.0));
        }

        public void resetData(ZcSumPo zcSumPo, Integer queryType) {
            Optional<GameZcData> optional = gameZcDataList.stream()
                    .filter(gameZcData -> gameZcData.getGameType().equals(zcSumPo.getGameType()))
                    .findFirst();
            if (optional.isPresent()) {
                GameZcData tmpZcData = optional.get();
                if (queryType == 1) {
                    tmpZcData.setData(zcSumPo.getYkAmount().doubleValue());
                    setData(getData() + zcSumPo.getYkAmount().doubleValue());
                } else if (queryType == 2) {
                    tmpZcData.setData(zcSumPo.getValidBetAmount().doubleValue());
                    setData(getData() + zcSumPo.getValidBetAmount().doubleValue());
                } else if (queryType == 3) {
                    tmpZcData.setData(zcSumPo.getBetAmount().doubleValue());
                    setData(getData() + zcSumPo.getBetAmount().doubleValue());
                } else if (queryType == 4) {
                    tmpZcData.setData(zcSumPo.getFsAmount().doubleValue());
                    setData(getData() + zcSumPo.getFsAmount().doubleValue());
                } else if (queryType == 5) {
                    tmpZcData.setData(zcSumPo.getZcAmount().doubleValue());
                    setData(getData() + zcSumPo.getZcAmount().doubleValue());
                } else if (queryType == 6) {
                    tmpZcData.setData(zcSumPo.getNetAmount().doubleValue());
                    setData(getData() + zcSumPo.getNetAmount().doubleValue());
                }
            }
        }
    }


    @Data
    @AllArgsConstructor
    public static class GameZcData {
        private Integer gameType;
        private String gameName;
        private Double data;
    }
}
