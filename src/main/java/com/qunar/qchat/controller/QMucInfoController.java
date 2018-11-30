package com.qunar.qchat.controller;

import com.qunar.qchat.dao.IMucInfoDao;
import com.qunar.qchat.dao.model.MucIncrementInfo;
import com.qunar.qchat.dao.model.MucInfoModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetIncrementMucsRequest;
import com.qunar.qchat.model.request.GetMucVcardRequest;
import com.qunar.qchat.model.request.UpdateMucNickRequest;
import com.qunar.qchat.model.result.GetMucVcardResult;
import com.qunar.qchat.model.result.UpdateMucNickResult;
import com.qunar.qchat.utils.CookieUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/newapi/muc/")
@RestController
public class QMucInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QMucInfoController.class);

    @Autowired
    private IMucInfoDao iMucInfoDao;

    @RequestMapping(value = "/get_increment.qunar", method = RequestMethod.POST)
    public JsonResult<?> getIncrement(HttpServletRequest request,
                                    @RequestBody GetIncrementMucsRequest getIncrementMucsRequest) {
        try {
            if(Objects.isNull(getIncrementMucsRequest.getT())) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            if(StringUtils.isBlank(getIncrementMucsRequest.getU())) {
                Map<String, Object> cookie = CookieUtils.getUserbyCookie(request);
                getIncrementMucsRequest.setU(cookie.get("u").toString());
            }
            if(StringUtils.isBlank(getIncrementMucsRequest.getD())) {
                Map<String, Object> cookie = CookieUtils.getUserbyCookie(request);
                getIncrementMucsRequest.setD(cookie.get("d").toString());
            }
            List<MucIncrementInfo> mucIncrementInfoList = iMucInfoDao.selectMucIncrementInfo(getIncrementMucsRequest.getU(), getIncrementMucsRequest.getD(), getIncrementMucsRequest.getT());

            return JsonResultUtils.success(mucIncrementInfoList);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/set_muc_nick.qunar", method = RequestMethod.POST)
    public JsonResult<?> updateMucNick(@RequestBody UpdateMucNickRequest request) {

        try{
            if (StringUtils.isBlank(request.getId())) {
                return JsonResultUtils.fail(0, "参数错误");
            }
            MucInfoModel parameter = new MucInfoModel();
            parameter.setMucName(request.getId());
            parameter.setShowName(request.getName());
            MucInfoModel newMucInfo = iMucInfoDao.updateMucInfo(parameter);

            UpdateMucNickResult result = new UpdateMucNickResult();
            result.setId(newMucInfo.getMucName());
            result.setVersion(newMucInfo.getVersion());

            return JsonResultUtils.success(result);
        }catch (Exception ex) {
            LOGGER.error("catch error ", ex);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(ex));
        }
    }

    @RequestMapping(value = "/get_muc_vcard.qunar", method = RequestMethod.POST)
    public JsonResult<?> getMucVCard(@RequestBody List<GetMucVcardRequest> request) {

        try{
            //LOGGER.info(request.toString());

            //检查参数是否合法
            if (CollectionUtils.isEmpty(request)) {
                return JsonResultUtils.success(Collections.EMPTY_LIST);
            }
            for(GetMucVcardRequest item : request) {
                List<GetMucVcardRequest.MucInfo> mucInfos = item.getMucs();
                for(GetMucVcardRequest.MucInfo info : mucInfos) {
                    if (StringUtils.isBlank(info.getMuc_name())) {
                        return JsonResultUtils.fail(0, "参数错误");
                    }
                }
            }

            List<GetMucVcardResult> results =
            request.stream().map(item -> {
                List<GetMucVcardRequest.MucInfo> mucInfos = item.getMucs();
                GetMucVcardResult result = new GetMucVcardResult();
                result.setDomain(item.getDomain());
                if(CollectionUtils.isNotEmpty(mucInfos)){
                    List<String> mucIds = mucInfos.stream().
                            map(requestMucInfo -> requestMucInfo.getMuc_name()).collect(Collectors.toList());

                    List<MucInfoModel> mucInfoModels = iMucInfoDao.selectMucInfoByIds(mucIds);
                    List<GetMucVcardResult.MucInfo> mucInfoResultList =
                            mucInfoModels.stream().map(mucInfoModel -> {
                                GetMucVcardResult.MucInfo resultMucInfo = new GetMucVcardResult.MucInfo();
                                resultMucInfo.setMN(mucInfoModel.getMucName());
                                resultMucInfo.setSN(mucInfoModel.getShowName());
                                resultMucInfo.setMD(mucInfoModel.getMucDesc());
                                resultMucInfo.setMT(mucInfoModel.getMucTitle());
                                resultMucInfo.setMP(mucInfoModel.getMucPic());
                                resultMucInfo.setVS(mucInfoModel.getVersion());
                                return resultMucInfo;
                            }).collect(Collectors.toList());
                    result.setMucs(mucInfoResultList);
                }else {
                    result.setMucs(new ArrayList<>());
                }

                return result;
            }).collect(Collectors.toList());

            return JsonResultUtils.success(results);
        }catch (Exception ex) {
            LOGGER.error("catch error ", ex);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(ex));
        }

    }
}
