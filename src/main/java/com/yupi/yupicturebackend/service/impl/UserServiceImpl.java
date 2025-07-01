package com.yupi.yupicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.User;
import com.yupi.yupicturebackend.service.UserService;
import com.yupi.yupicturebackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author SUZURI
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-07-01 14:55:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




