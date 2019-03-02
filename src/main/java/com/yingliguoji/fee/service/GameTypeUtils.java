package com.yingliguoji.fee.service;

import com.google.common.collect.Maps;
import com.yingliguoji.fee.enums.GameType;

import java.util.Map;

public final class GameTypeUtils {
    static Map<Integer, String> maps = Maps.newConcurrentMap();

    static {
        maps.put(0, "占成");
        maps.put(GameType.ZR, "真人");
        maps.put(GameType.DZ, "电子");
        maps.put(GameType.CP, "彩票");
        maps.put(GameType.TY, "体育");
        maps.put(GameType.DJ, "电竞");
        maps.put(GameType.BY, "捕鱼");
        maps.put(GameType.QP, "棋牌");

    }

    public static  String  getGameName(Integer gameType){
        return maps.get(gameType);
    }
}
