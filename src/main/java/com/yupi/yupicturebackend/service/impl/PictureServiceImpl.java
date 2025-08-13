package com.yupi.yupicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Picture;
import com.yupi.yupicturebackend.service.PictureService;
import com.yupi.yupicturebackend.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author SUZURI
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-08-14 00:30:46
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}




