package com.iotinall.canteen.constants;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum BodySizeEnum {
    NoBodyType(0, "无体型值", ""),
    InvisibleObesity(1, "隐形肥胖型", "您的体型属于隐形肥胖型，得多进行有氧运动，否则很容 易成为真胖子了。"),
    LackOfExercise(2, "运动不足型", "您的体型属于运动不足型，需要运动起来了。"),
    thin(3, "偏瘦型", "您的体型属于偏瘦型，需要加强营养了。"),
    standard(4, "标准型", "您的体型属于标准型，继续保持。"),
    leanMuscle(5, "偏瘦肌肉型", "您的体型属于偏瘦肌肉型，继续保持。"),
    obese(6, "肥胖型", "您的体型属于肥胖型，控制饮食和加强有氧运动能够助您 降低脂肪。"),
    overweight(7, "偏胖型", "您的体型属于偏胖型，控制饮食和加强有氧运动能够助您 降低脂肪。"),
    standardMuscle(8, "标准肌肉型", "您的体型属于标准肌肉型，继续保持。"),
    veryMuscular(9, "非常肌肉型", "您的体型属于非常肌肉型，继续保持。");

    public static BodySizeEnum findByCode(Integer code){
        if(code == null){
            return null;
        }
        for (BodySizeEnum value : BodySizeEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未找到身体数据类型");
    }

    BodySizeEnum(Integer code, String name, String remark) {
        this.code = code;
        this.name = name;
        this.remark = remark;
    }

    private final Integer code;

    private final String name;

    private final String remark;
}
