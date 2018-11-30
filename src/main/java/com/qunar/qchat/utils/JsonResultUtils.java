package com.qunar.qchat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.model.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Author : mingxing.shao
 * Date : 16-4-12
 * email : mingxing.shao@qunar.com
 */
public class JsonResultUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonResultUtils.class);

    public static JsonResult<?> success() {
        return success(null);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(JsonResult.SUCCESS, JsonResult.SUCCESS_CODE, "", data);
    }

    public static JsonResult<?> fail(int errcode, String errmsg) {
        return new JsonResult<>(JsonResult.FAIL, errcode, errmsg, "");
    }

    public static JsonResult<?> response(String response) {
        try {
            if (response == null) {
                return new JsonResult<>(JsonResult.FAIL, 1, "parse response fail", "");
            }
            JsonResult result = JacksonUtils.string2Obj(response, JsonResult.class);
            return result;
        } catch  (Exception e) {
            return new JsonResult<>(JsonResult.FAIL, 1, "parse response fail", "");
        }
    }
}
