package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.dao.IProfileDao;
import com.qunar.qchat.dao.model.Profile;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetProfileRequest;
import com.qunar.qchat.utils.CookieUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/newapi/profile/")
@RestController
public class QProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QProfileController.class);

    @Autowired
    private IProfileDao iProfileDao;

    @RequestMapping(value = "/get_profile.qunar", method = RequestMethod.POST)
    public JsonResult<?> getProfile(@RequestBody GetProfileRequest getProfileRequest) {
        try {

            if(StringUtils.isBlank(getProfileRequest.getUser()) ||
                    StringUtils.isBlank(getProfileRequest.getDomain())) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            List<Profile> profileList = iProfileDao.selectProfileInfo(getProfileRequest.getUser(), getProfileRequest.getDomain(), getProfileRequest.getVersion());

            List<Map<String, Object>> resultList = new ArrayList<>();

            for(Profile profile : profileList) {
                Map<String, Object> map = new HashMap<>();
                // username, host, mood, profile_version as version
                map.put("username", profile.getUsername());
                map.put("host", profile.getHost());
                map.put("mood", profile.getMood());
                map.put("version", profile.getVersion());
                resultList.add(map);
            }

            return JsonResultUtils.success(resultList);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/set_profile.qunar", method = RequestMethod.POST)
    public JsonResult<?> SetProfile(HttpServletRequest request,
                                    @RequestBody GetProfileRequest getProfileRequest) {
        try {
            //Map<String, Object> cookie = CookieUtils.getUserbyCookie(request);
            //getProfileRequest.setUser(cookie.get("u").toString());
            //getProfileRequest.setDomain(cookie.get("d").toString());

            if(StringUtils.isBlank(getProfileRequest.getUser()) || StringUtils.isBlank(getProfileRequest.getDomain())) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            if (!StringUtils.isBlank(getProfileRequest.getMood())) {
                //LOGGER.info("the user is {}", cookie.get("u").toString());
                List<Profile> profileList = iProfileDao.updateProfileInfo(getProfileRequest.getUser(), getProfileRequest.getDomain(), getProfileRequest.getMood());

                return JsonResultUtils.success(profileList);
            }

            if (!StringUtils.isBlank(getProfileRequest.getUrl())) {
                //LOGGER.info("the user is {}", cookie.get("u").toString());
                List<Profile> profileList = iProfileDao.updateUrlInfo(getProfileRequest.getUser(), getProfileRequest.getDomain(), getProfileRequest.getUrl());
                return JsonResultUtils.success(profileList);
            }

            return JsonResultUtils.fail(0, "请求参数异常");
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }
}
