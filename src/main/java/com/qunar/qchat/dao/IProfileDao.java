package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.InviteInfoModel;
import com.qunar.qchat.dao.model.Profile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IProfileDao {

    List<Profile> selectProfileInfo(
            @Param("username") String username,
            @Param("host") String host,
            @Param("version") int version
    );

    List<Profile> updateProfileInfo(
            @Param("username") String username,
            @Param("host") String host,
            @Param("mood") String mood
    );

    List<Profile> updateUrlInfo(
            @Param("username") String username,
            @Param("host") String host,
            @Param("url") String url
    );
}