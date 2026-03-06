package com.yupi.yupicturebackend.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupicturebackend.common.BaseResponse;
import com.yupi.yupicturebackend.common.DeleteRequest;
import com.yupi.yupicturebackend.exception.BusinessException;
import com.yupi.yupicturebackend.exception.ErrorCode;
import com.yupi.yupicturebackend.model.dto.file.UploadPictureResult;
import com.yupi.yupicturebackend.model.dto.picture.*;
import com.yupi.yupicturebackend.model.dto.user.UserQueryRequest;
import com.yupi.yupicturebackend.model.entity.Picture;
import com.yupi.yupicturebackend.model.entity.User;
import com.yupi.yupicturebackend.model.vo.PictureVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author SUZURI
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-08-14 00:30:46
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     *
     * @param inputSource
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    /**
     * 将查询条件转化为QueryWrapper
     *
     * @param queryRequest
     * @return
     */
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest queryRequest);

    /**
     * 对查询到的单张图片进行脱敏
     * @param picture
     * @param request
     * @return
     */
    public PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 对查询到的图片列表进行脱敏
     * @param page
     * @param request
     * @return
     */
    public Page<PictureVO> getPictureVOPage(Page<Picture> page, HttpServletRequest request);

    /**
     * 验证数据格式正确性
     * @param picture
     */
    public void validPicture(Picture picture);

    /**
     * 图片审核
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginuser);

//     /**
//      * 删除图片
//      * @param deleteRequest
//      * @param request
//      * @return
//      */
//     boolean deletePicture(DeleteRequest deleteRequest, HttpServletRequest request);
    /**
     * 填充图片审核参数
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 批量抓取和创建图片
     *
     * @param pictureUploadByBatchRequest
     * @param loginUser
     * @return 成功创建的图片数
     */
    Integer uploadPictureByBatch(
            PictureUploadByBatchRequest pictureUploadByBatchRequest,
            User loginUser
    );

    /**
     *
     * @param oldPicture
     */
    void clearPictureFile(Picture oldPicture);

    /**
     * 验证图片操作权限 修改和删除使用
     * @param loginUser
     * @param picture
     */
    void checkPictureAuth(User loginUser, Picture picture);

    /**
     * 删除图片
     * @param pictureId
     * @param loginUser
     */
    void deletePicture(long pictureId, User loginUser);

    void editPicture(PictureEditRequest pictureEditRequest, User loginUser);
}
