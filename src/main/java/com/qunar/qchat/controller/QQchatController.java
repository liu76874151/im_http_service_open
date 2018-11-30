package com.qunar.qchat.controller;

import com.qunar.qchat.constants.Config;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.SendWeRequest;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.URLBuilder;
import com.qunar.qtalk.ss.common.utils.HttpClientUtils;
import com.qunar.qchat.constants.QChatConstant;
import com.alibaba.fastjson.JSONObject;
import com.qunar.qchat.utils.ConsultUtils;
import com.qunar.qtalk.ss.common.org.xmpp.packet.JID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RequestMapping("/newapi/qchat/")
@RestController
public class QQchatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QBaseController.class);
    private static final String GUEST_TYPE = "5";
    private static final String toCusMassage  = "为了方便您接收咨询，请使用微信扫此二维码关注公众号【度假小助手】，您关注后，商家的后续咨询回复会通过微信公众号发送给您，您可以直接在公众号中点击消息进入对话，查看和回复消息。\n" +
            "请注意：对话中可能包含您的敏感信息，此二维码仅供您绑定微信使用，请自己保存，不要提供给第三方。后续如果想取消绑定，请直接取消关注公众号。";

    @RequestMapping(value = "/get_dep_info.qunar", method = RequestMethod.GET)
    public JsonResult<?> getDepInfo(@RequestParam(value = "strid") String username) {
        try {
            String NewUsername = username.replace("[at]", "@");
            String url = Config.getProperty("admin_corp_url");
            URLBuilder builder = new URLBuilder();
            builder.setHost(url+"/api/supplier/getDeptTree.qunar");
            builder.addQuery("qunarName", NewUsername);
            String response = HttpClientUtils.get(builder.build());
            return JsonResultUtils.response(response);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/qvt/send_wlan_message.qunar", method = RequestMethod.POST)
    public JsonResult<?> sendWlanMessage(@RequestBody String json) {
        try {
            String url = Config.getProperty("ejabberd_http_url");
            String response = HttpClientUtils.postJson(url + "send_wlan_message", json);
            return JsonResultUtils.response(response);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/nck/get_power.qunar", method = RequestMethod.GET)
    public JsonResult<?> getPower(HttpServletRequest request,
                                  @RequestParam(value = "strid") String username,
                                  @RequestParam(value = "password") String password) {
        try {
            String xRealIp = request.getHeader("X-Real-Ip");
            String remoteAddr = request.getRemoteAddr();
            String ip = null;
            if(xRealIp == null) {
                ip =remoteAddr;
            } else {
                ip = xRealIp;
            }

            return JsonResultUtils.success();
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取二维码图片发送消息
     * @param sendWeRequest 请求参数
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendWe.qunar", method = RequestMethod.POST)
    public JsonResult<?> sendQRCodeImage(@RequestBody SendWeRequest sendWeRequest, HttpServletRequest request) {

        try {
            String qrcodeUrl = Config.CREATE_QRCODE_URL;
            if (StringUtils.isEmpty(qrcodeUrl)) {
                return JsonResultUtils.fail(500, "服务端配置错误");
            }
            Integer indexSpliter = sendWeRequest.getRealJid().indexOf("@");
            if(indexSpliter.equals(-1)||indexSpliter.equals(0)){
                return JsonResultUtils.fail(1, "RealJid 不合法");
            }
            if(!GUEST_TYPE.equals(sendWeRequest.getChatType())){
                return JsonResultUtils.fail(1, "ChatType 不合法");
            }
            String url = String.format(Config.CREATE_QRCODE_URL, sendWeRequest.getRealJid().substring(0,indexSpliter));
            String result = HttpClientUtils.get(url);
            LOGGER.info("create_qrcode_url result:{}", result);
            if (StringUtils.isEmpty(result)) {
                LOGGER.error("QRCode server error:{}", Config.CREATE_QRCODE_URL);
                return JsonResultUtils.fail(500, "服务端错误");
            }
            JID seat = new JID(sendWeRequest.getFrom());
            JID customer = new JID(sendWeRequest.getRealJid());
            JID shopJid =  new JID(sendWeRequest.getTo());
           // JID seat = parse(sendWeRequest.getFrom(), sendWeRequest.getFhost());
           // JID customer = parse(sendWeRequest.getRealJid(), sendWeRequest.getRhost());
           // JID shopJid = parse(sendWeRequest.getTo(), sendWeRequest.getThost());

            JSONObject qChatResult = JSONObject.parseObject(result, JSONObject.class);
            JSONObject data = qChatResult.getJSONObject("data");
            if (!qChatResult.getBoolean("ret") || data == null || StringUtils.isEmpty(data.getString("qrcode"))) {
                String msg = qChatResult.getString("msg");
                ConsultUtils.sendMessage(shopJid, seat, customer, seat, msg, true, false);
                return JsonResultUtils.fail(500, msg);
            }
            String massage = data.getString("qrcode");
            ConsultUtils.sendMessage(shopJid, customer, seat, customer, massage, false, true);
            ConsultUtils.sendMessage(shopJid, customer, seat, customer, toCusMassage, false, false);

            return JsonResultUtils.success("success");
        } catch (Exception e) {
            LOGGER.error("sendQRCodeImage error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常");
        }
    }

    private JID parse(String name, String host) {
        if (!name.contains("@")) {
            name = String.format("%s@%s", name, QChatConstant.QCHAR_HOST);
        }
        return JID.parseAsJID(name);
    }
}
