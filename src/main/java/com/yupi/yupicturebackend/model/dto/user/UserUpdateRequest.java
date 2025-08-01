package com.yupi.yupicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id 通过id查询来更新用户信息
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
