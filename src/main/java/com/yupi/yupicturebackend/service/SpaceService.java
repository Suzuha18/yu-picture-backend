package com.yupi.yupicturebackend.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupicturebackend.model.dto.space.SpaceAddRequest;
import com.yupi.yupicturebackend.model.dto.space.SpaceQueryRequest;
import com.yupi.yupicturebackend.model.entity.Space;
import com.yupi.yupicturebackend.model.entity.User;
import com.yupi.yupicturebackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author SUZUHA
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2026-03-04 23:28:13
*/
public interface SpaceService extends IService<Space> {
    /**
     * 将查询条件转化为QueryWrapper
     *
     * @param SpacequeryRequest
     * @return
     */
    public QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest SpacequeryRequest);

    /**
     * 对查询到单个空间进行脱敏
     * @param space
     * @param request
     * @return
     */
    public SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 对查询到的空间列表进行脱敏
     * @param page
     * @param request
     * @return
     */
    public Page<SpaceVO> getSpaceVOPage(Page<Space> page, HttpServletRequest request);

    /**
     * 空间校验
     * @param space
     */
    void validSpace(Space space, boolean add);

    /**
     * 根据空间等级填充空间信息
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 创建空间
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

}
