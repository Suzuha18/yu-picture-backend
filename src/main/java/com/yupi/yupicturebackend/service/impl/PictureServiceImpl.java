package com.yupi.yupicturebackend.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupicturebackend.exception.BusinessException;
import com.yupi.yupicturebackend.exception.ErrorCode;
import com.yupi.yupicturebackend.exception.ThrowUtils;
import com.yupi.yupicturebackend.manage.FileManager;
import com.yupi.yupicturebackend.model.dto.file.UploadPictureResult;
import com.yupi.yupicturebackend.model.dto.picture.PictureQueryRequest;
import com.yupi.yupicturebackend.model.dto.picture.PictureUploadRequest;

import com.yupi.yupicturebackend.model.entity.Picture;
import com.yupi.yupicturebackend.model.entity.User;
import com.yupi.yupicturebackend.model.vo.PictureVO;
import com.yupi.yupicturebackend.service.PictureService;
import com.yupi.yupicturebackend.mapper.PictureMapper;
import com.yupi.yupicturebackend.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author SUZURI
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-08-14 00:30:46
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

    @Resource
    private UserService userService;
    @Resource
    private FileManager fileManager;


    @Override
    public PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        // 先判断用户首先要登录才能上传图片
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NO_AUTH_ERROR);
        // 判断是图片更新，还是要删除图片的请求
        Long pictureId = null;
        // 不空说明是要更新图片
        if (pictureUploadRequest != null) {
            //
            pictureId = pictureUploadRequest.getId();
        }
        // 如果ID不为空，就是要更新图片，就要先看图片是否存在，ID不在数据库就是不对的
        if (pictureId != null) {
            boolean exists = this.lambdaQuery()
                    .eq(Picture::getId, pictureId)
                    .exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        // 如果ID为空，就说明本来没有传过图片，要新增图片，直接从这里开始
        // 上传图片得到信息，并按照用户等级和ID划分目录
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());
        // ID不为空，更新还要改变编辑时间和更改的图片的随机ID
        if (pictureId != null) {
            picture.setId(pictureId);
            picture.setEditTime(new Date());
        }
        // 使用mybatis自带的自动新增或更新功能
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "图片上传失败");
        return PictureVO.objToVo(picture);
    }

    /**
     * 将查询请求转化为mybatis的QueryWrapper
     *
     * @param
     * @return
     */
    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        // 从对象取值
        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        Long picSize = pictureQueryRequest.getPicSize();
        Integer picWidth = pictureQueryRequest.getPicWidth();
        Integer picHeight = pictureQueryRequest.getPicHeight();
        Double picScale = pictureQueryRequest.getPicScale();
        String picFormat = pictureQueryRequest.getPicFormat();
        String searchText = pictureQueryRequest.getSearchText();
        Long userId = pictureQueryRequest.getUserId();
        int current = pictureQueryRequest.getCurrent();
        int pageSize = pictureQueryRequest.getPageSize();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();
        // 从多字段中搜索
        if (StrUtil.isNotBlank(searchText)) {
            // 拼接查询条件
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or()
                    .like("introduction", searchText)
            );
        }
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(introduction), "introduction", introduction);
        queryWrapper.like(StrUtil.isNotBlank(picFormat), "picFormat", picFormat);
        queryWrapper.eq(StrUtil.isNotBlank(category), "category", category);
        queryWrapper.eq(ObjUtil.isNotEmpty(picWidth), "picWidth", picWidth);
        queryWrapper.eq(ObjUtil.isNotEmpty(picHeight), "picHeight", picHeight);
        queryWrapper.eq(ObjUtil.isNotEmpty(picSize), "picSize", picSize);
        queryWrapper.eq(ObjUtil.isNotEmpty(picScale), "picScale", picScale);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    /**
     * 通过查出来的图片，对图片信息脱敏，在找到查询到的图片对应的用户进行脱敏，然后返回
     * @param picture
     * @param request
     * @return
     */
    @Override
    public PictureVO getPictureVO(Picture picture, HttpServletRequest request) {
        //对象转封装类
        PictureVO pictureVO = PictureVO.objToVo(picture);

        // 获取关联用户信息返回前端
        Long id = pictureVO.getUserId();
        if (id != null && id > 0) {
            User user = userService.getById(id);
            pictureVO.setUser(userService.getUserVO(user));
        }
        return pictureVO;
    }

    @Override
    public Page<PictureVO> getPictureVOPage(Page<Picture> page, HttpServletRequest request) {
        List<Picture> pageRecords = page.getRecords();
        Page<PictureVO> pictureVOPage = new Page<>(page.getCurrent(), page.getSize());
        if(pageRecords == null || pageRecords.isEmpty()){
            return pictureVOPage;
        }
        // 将对象列表转换为封装脱敏对象列表
        List<PictureVO> pictureVOList = pageRecords.stream().map(PictureVO::objToVo).collect(Collectors.toList());

        // 1.查询关联用户信息
        Set<Long> userIds = pageRecords.stream().map(Picture::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userMapById = userService.listByIds(userIds).stream().collect(Collectors.groupingBy(User::getId));
        for(PictureVO pictureVO : pictureVOList){
            Long userId = pictureVO.getUserId();
            User user = null;
            if(userMapById.containsKey(userId)){
                user = userMapById.get(userId).get(0);
            }
            pictureVO.setUser(userService.getUserVO(user));
        }
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }

    @Override
    public void validPicture(Picture picture) {
        ThrowUtils.throwIf(picture == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = picture.getId();
        String url = picture.getUrl();
        String introduction = picture.getIntroduction();
        // 修改数据时，id 不能为空，有参数则校验
        ThrowUtils.throwIf(ObjUtil.isNull(id), ErrorCode.PARAMS_ERROR, "id 不能为空");
        // 如果传入了url，才校验
        if (StrUtil.isNotBlank(url)) {
            ThrowUtils.throwIf(url.length() > 1024, ErrorCode.PARAMS_ERROR, "url 过长");
        }
        if (StrUtil.isNotBlank(introduction)) {
            ThrowUtils.throwIf(introduction.length() > 800, ErrorCode.PARAMS_ERROR, "简介过长");
        }
    }


}




