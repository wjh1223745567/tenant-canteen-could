package com.iotinall.canteen.dto.cookrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 *
 * @author xinbing
 * @date 2020-07-09 15:26:01
 */
@Data
@ApiModel(description = "烹饪记录列表")
public class CookRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;

    @ApiModelProperty(value = "餐次", required = true)
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "检查结果")
    private Integer state; // 检查结果

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime recordTime; // 操作时间

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "菜品id")
    private Long productId; // 菜品id

    @ApiModelProperty(value = "菜品名称")
    private String productName;

    @ApiModelProperty(value = "菜品图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String productImg;

    @ApiModelProperty(value = "责任人id", required = true)
    private Long dutyEmpId; // 责任人id

    @ApiModelProperty(value = "责任人姓名")
    private String dutyEmpName;

    @ApiModelProperty(value = "员工头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String dutyEmpAvatar;

    @ApiModelProperty(value = "审核人id", required = true)
    private Long auditorId; // 审核人id

    @ApiModelProperty(value = "审核人姓名")
    private String auditorName;

    @ApiModelProperty(value = "评论统计")
    private CommentsInfo commentsInfo;

    private String role;

    @Data
    @ApiModel(value = "评论数量信息")
    public static class CommentsInfo {

        @ApiModelProperty(value = "差评数")
        Integer negativeCount;

        @ApiModelProperty(value = "好评数")
        Integer praiseCount;

        @ApiModelProperty(value = "五星好评")
        Integer fiveStar;

        Integer fourStar;

        Integer threeStar;

        Integer twoStar;

        Integer oneStar;

        public static CommentsInfo of(CommentsDTO commentsDTO) {
            CommentsInfo commentsInfo = new CommentsInfo();
            commentsInfo.setFiveStar(commentsDTO.getFiveStar()==null?0:commentsDTO.getFiveStar().intValue());
            commentsInfo.setFourStar(commentsDTO.getFourStar()==null?0:commentsDTO.getFourStar().intValue());
            commentsInfo.setThreeStar(commentsDTO.getThreeStar()==null?0:commentsDTO.getThreeStar().intValue());
            commentsInfo.setTwoStar(commentsDTO.getTwoStar()==null?0:commentsDTO.getTwoStar().intValue());
            commentsInfo.setOneStar(commentsDTO.getOneStar()==null?0:commentsDTO.getOneStar().intValue());

            commentsInfo.setPraiseCount(commentsInfo.getFiveStar() + commentsInfo.getFourStar() + commentsInfo.getThreeStar());
            commentsInfo.setNegativeCount(commentsInfo.getTwoStar() + commentsInfo.getOneStar());
            return commentsInfo;
        }
    }
}