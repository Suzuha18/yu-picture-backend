package com.yupi.yupicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 任何一个功能需要为其单独定义类，强行修改有联动的数据容易出错，比如管理员强行修改图片状态、用户积分
 */
@Data
public class PictureReviewRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 状态：0-待审核; 1-通过; 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}
