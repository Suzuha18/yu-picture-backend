package com.yupi.yupicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

// 自动生成getset
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 8735650154179439661L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;


}
