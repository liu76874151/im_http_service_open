package com.qunar.qchat.constants;

/**
 * Created by yinmengwang on 17-5-19.
 */
public interface QChatConstant {

    String QCADMIN = "qcadmin";

    String SEATROBOTPOSTFIX = "_robot";
    String SEATSHOPPREFIX = "shop_";

    // host
    String QCHAR_HOST = "ejabhost2";
    String QTALK_HOST = "ejabhost1";

    interface Note {
        String FROM = "from";
        String TO = "to";
        String IP = "ip";
        String BU = "bu";
        String Type = "type";
        String URL = "url";
        String DATA = "data";
        String NOTE = "note";
        String CHAT = "chat";
        String SUBSCRIPTION = "subscription";
        String MESSAGE = "message";
        String MESSAGE_AUTO_REPLY="auto_reply";
        String MESSAGE_NO_UPDATE_MSG_LOG="no_update_msg_log";
        String XMLNS = "xmlns";
        String JABBER_CLIENT = "jabber:client";
        String BODY = "body";
        String ID = "id";
        String MSGTYPE = "msgType";
        String MSGTYPE_NOTE = "11";
        String MSGTYPE_COMM = "1";
        String MATYPE = "maType";
        String MATYPE_WEB = "3";
        String ACTIVE = "active";
        String JABBER_URL = "http://jabber.org/protocol/chatstates";

        String CONSULT = "consult";
        String QCHAT_ID = "qchatid";
        String QCHAT_ID_USER2SEAT = "4";
        String QCHAT_ID_SEAT2USER = "5";
        String CHANNEL_ID = "channelid";
        String REAL_FROM = "realfrom";
        String REAL_TO = "realto";
        String IS_HIDDEN_MSG = "isHiddenMsg";

        String BODY_BACKUPINFO = "backupinfo";
        String BODY_EXTENDINFO = "extendInfo";

        String CARBON_MSG = "carbon_message";
    }

    String VCARD_COMMON_URL = "https://qt.qunar.com/dianping/user_comment.php";
    String VCARD_IMAGE_URL_FEMAIL = "https://qt.qunar.com/file/v2/download/perm/784bf8637e509d2c1e22794252c0d9c3.png";
    String VCARD_IMAGE_URL_MAIL = "https://qt.qunar.com/file/v2/download/perm/3ca05f2d92f6c0034ac9aee14d341fc7.png";
}
