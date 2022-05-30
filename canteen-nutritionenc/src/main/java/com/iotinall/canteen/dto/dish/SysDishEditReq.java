package com.iotinall.canteen.dto.dish;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 编辑菜谱请求参数
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Data
@ApiModel(description = "编辑菜谱请求参数")
public class SysDishEditReq implements Serializable {
    @NotBlank(message = "菜谱ID不能为空")
    private String id;

    @ApiModelProperty(value = "菜品图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @NotBlank(message = "菜谱图片不能为空")
    private String img;

    @ApiModelProperty(value = "菜品名称")
    @NotBlank(message = "菜谱名称不能为空")
    private String name;

    @ApiModelProperty(value = "工艺ID，多个逗号拼接")
    @NotBlank(message = "菜品工艺不能为空")
    private String craftId;

    @ApiModelProperty(value = "口味ID，多个逗号拼接")
    @NotBlank(message = "菜品口味不能为空")
    private String flavoursId;

    @ApiModelProperty(value = "类别，多个逗号拼接")
    @NotBlank(message = "菜品类别不能为空")
    private String cuisines;

    @ApiModelProperty(value = "原材料,JSON字符串,格式:[{'id':'000C8C1C408B4DC184A6C4A659FDA077','name':'豆瓣菜','amount':'1','sort':0,'master':0},{'id':'000C8C1C408B4DC184A6C4A659FDA077','name':'豆瓣菜','amount':'1','sort':0,'master':0}]")
    private String materials;

    @ApiModelProperty(value = "营养信息")
    private String intro;

    @ApiModelProperty(value = "口感")
    private String taste;

    @ApiModelProperty(value = "菜品的健康提示")
    private String healthTips;

    @ApiModelProperty(value = "食物相克，逗号拼接")
    private String restriction;

    @ApiModelProperty(value = "菜品做法,JSON字符串,[{\"description\":\"把菜谱列的用料全部放入面盆中～\",\"fileType\":0,\"filePath\":null},{\"description\":\"用筷子搅拌一下，和成面团，和面团的时候会粘手，用干面粉把粘在手上的面清理一下，手上抹油，一边揉面，一边摔打。\",\"fileType\":0,\"filePath\":null},{\"description\":\"和好的面团，比较软，盖上保鲜膜室温醒发。\",\"fileType\":0,\"filePath\":null},{\"description\":\"醒发至满满的蜂窝状，图片是醒发好的面团～\",\"fileType\":0,\"filePath\":null},{\"description\":\"面板撒点面粉，面团倒在面粉上。\\n也可以在面板上抹上油防粘（随个人习惯）～\",\"fileType\":0,\"filePath\":null},{\"description\":\"不用揉，不用揉，不用揉，直接整形成条（不要太薄，也不要太厚）～\",\"fileType\":0,\"filePath\":null},{\"description\":\"切条，筷子蘸水，抹在其中的一个面条上（可以防止炸油条时两个面条分开）\",\"fileType\":0,\"filePath\":null},{\"description\":\"另一个面条放在蘸水的面条上，用筷子将两个面条压在一起\",\"fileType\":0,\"filePath\":null},{\"description\":\"锅里加油，烧热（油温不能太低，油的温度为：油条胚放进去可以马上浮起来即可）。\\n捏住油条胚的两端，把油条胚放入油中（油条胚不厚的话不用抻，油条胚做的厚轻轻抻一下）。\",\"fileType\":0,\"filePath\":null},{\"description\":\"炸至金黄，出锅即可。\",\"fileType\":0,\"filePath\":null}]")
    private String practice;

    @ApiModelProperty(value = "菜品的制作提示")
    private String practiceTips;

    /**
     * true - 适合
     * false - 不适合
     */
    @ApiModelProperty(value = "是否适合晚餐")
    private Boolean dinner;

    /**
     * true - 适合
     * false - 不适合
     */
    @ApiModelProperty(value = "是否适合午餐")
    private Boolean lunch;

    /**
     * true - 适合
     * false - 不适合
     */
    @ApiModelProperty(value = "是否适合早餐")
    private Boolean breakfast;
}
