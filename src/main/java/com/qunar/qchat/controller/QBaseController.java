package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.dao.IHostInfoDao;
import com.qunar.qchat.dao.IInviteInfoDao;
import com.qunar.qchat.dao.model.HostInfoModel;
import com.qunar.qchat.dao.model.InviteInfoModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetDepsRequest;
import com.qunar.qchat.model.request.GetInviteInfoRequest;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qtalk.ss.common.exception.QTalkRequestFailedExecption;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/newapi/base/")
@RestController
public class QBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QBaseController.class);

    @Autowired
    private IInviteInfoDao iInviteInfoDao;
    @Autowired
    private IHostInfoDao hostInfoDao;

    @RequestMapping(value = "/getservertime.qunar", method = RequestMethod.GET)
    public JsonResult<?> getServerTime(HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis() / 1000;
            return JsonResultUtils.success(time);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/get_invite_info.qunar", method = RequestMethod.POST)
    public JsonResult<?> getInviteInfo(@RequestBody GetInviteInfoRequest request) {
        try {

            if(StringUtils.isBlank(request.getDomain()) ||
                    StringUtils.isBlank(request.getUser()) ||
                        request.getTime() == -1) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            if (Objects.isNull(hostInfoDao.selectHostInfoByHostName(request.getDomain()))) {
                return JsonResultUtils.fail(1,"domain [" + request.getDomain()+ "] 不存在");
            }

            List<InviteInfoModel> inviteInfoModelList = iInviteInfoDao.selectInviteInfo(request.getUser(),
                                          request.getDomain(), request.getTime());

            return JsonResultUtils.success(inviteInfoModelList);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }


    @RequestMapping(value = "/get_deps.qunar", method = RequestMethod.GET)
    public Object getDeps(GetDepsRequest request){
        try{
            if(StringUtils.isBlank(request.getD())) {
                return JsonResultUtils.fail(0, "参数错误");
            }

            HostInfoModel hostInfo = hostInfoDao.selectHostInfoByHostName(request.getD());
            if (Objects.isNull(hostInfo)) {
                return JsonResultUtils.fail(1,"domain [" + request.getD() + "] 不存在");
            }

            String parameter = "{\"domain\":\""+ request.getD() +"\"}";
            return HttpClientUtils.postJson(Config.URL_GET_DEPS, parameter);
        }catch (Exception ex) {
            LOGGER.error("cache error", ex);
            return JsonResultUtils.fail(0, "服务器操作异常:\n" + ExceptionUtils.getStackTrace(ex));
        }
    }


}
