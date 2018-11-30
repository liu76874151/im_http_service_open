package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.ClientConfigModel;
import com.qunar.qchat.dao.model.InviteInfoModel;
import com.qunar.qchat.model.request.SetClientConfigRequst;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface IInviteInfoDao {

    List<InviteInfoModel> selectInviteInfo(
            @Param("username") String username,
            @Param("host") String host,
            @Param("time") int time
    );
}