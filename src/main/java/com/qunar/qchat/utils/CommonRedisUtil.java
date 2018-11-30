package com.qunar.qchat.utils;

import com.alibaba.fastjson.JSON;
import com.qunar.qtalk.ss.common.utils.QtalkStringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @auth dongzd.zhang
 * @Date 2018/10/19 15:06
 */
public class CommonRedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonRedisUtil.class);

    /**
     * 是否有平台在线
     * @param toUser
     * @param toDomain
     * @return
     */
    public static String getUserStatus(String toUser, String toDomain){
        //boolean isHasOtherPlatOnline = false;
        if(TextUtils.isEmpty(toDomain) || TextUtils.isEmpty(toUser)) {
            return "offline";
        }
        String key = "ejabberd:sm:other:" + QtalkStringUtils.userId2Jid(toUser, toDomain);

        //LOGGER.info("redis key: {}", key);

        Map<String,String> result = null;
        if(isPubim(toDomain)) {
            result = PubimRedisUtil.hGetAll(7, key);
        } else {
            result = RedisUtil.hGetAll(7, key);
        }

        if(!CollectionUtils.isEmpty(result)){
            for(String value : result.values()) {

                Map<String, String> resouce = JSON.parseObject(value, Map.class);

                if(!CollectionUtils.isEmpty(resouce)){
                    String r = resouce.get("r").toString();
                    String f = resouce.get("f").toString();

                    LOGGER.info("current {} status: {}", toUser, f);

                    if("normal".equalsIgnoreCase(f)
                            || "online".equalsIgnoreCase(f)
                            || "push".equalsIgnoreCase(f)){
                        return "online";
                    }

                    if("busy".equalsIgnoreCase(f) || "away".equalsIgnoreCase(f)) {
                        return f.toLowerCase();
                    }
                    return "offline";
                }
            }
        }
        return "offline";
    }


    /**
     * 是否是公共域
     * @param toDomain
     * @return
     */
    public static boolean isPubim(String toDomain) {
        if(toDomain.equalsIgnoreCase("ejabhost1")
                || toDomain.equalsIgnoreCase("ejabhost2")) {
            return false;
        } else {
            return true;
        }
    }

}
