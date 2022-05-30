package com.iotinall.canteen.constants;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum BodyTypeEnum {
    TYPE_WEIGHT(1,"体重", "Kg"),
    TYPE_BMI(2,"BMI", ""),
    TYPE_BODYFAT(3, "体脂率", "%"),
    TYPE_SUBFAT(4, "皮下脂肪率", "%"),
    TYPE_VISFAT(5, "内脏脂肪等级", ""),
    TYPE_WATER(6, "身体水分率", "%"),
    TYPE_MUSCLE(7, "骨骼肌率", "%"),
    TYPE_BONE(8, "骨量", "Kg"),
    TYPE_BMR(9,"基础代谢率", "Kcal"),
    TYPE_BODY_SHAPE(10, "体型", ""),
    TYPE_PROTEIN(11, "蛋白质", "%"),
    TYPE_LBM(12, "去脂体重", "Kg"),
    TYPE_MUSCLE_MASS(13, "肌肉量", "Kg"),
    TYPE_BODY_AGE(14, "体年龄", "岁"),
    TYPE_SCORE(15, "分数", ""),
    TYPE_HEART_RATE(16, "心率", "次/分"),
    TYPE_HEART_INDEX(17, "心脏指数", ""),
    TYPE_FAT_MASS_INDEX(21, "脂肪重量", "Kg"),
    TYPE_OBESITY_DEGREE_INDEX(22, "肥胖度", "%"),
    TYPE_WATER_CONTENT_INDEX(23, "含水量", "Kg"),
    TYPE_PROTEIN_MASS_INDEX(24, "蛋白质量", "Kg"),
    TYPE_MINERAL_SALT_INDEX(25, "无机盐状况", ""),
    TYPE_BEST_VISUAL_WEIGHT_INDEX(26, "理想视觉体重", "Kg"),
    TYPE_STAND_WEIGHT_INDEX(27,"标准体重", "Kg"),
    TYPE_WEIGHT_CONTROL_INDEX(28, "体重控制量", "Kg"),
    TYPE_FAT_CONTROL_INDEX(29, "脂肪控制量", "Kg"),
    TYPE_MUSCLE_CONTROL_INDEX(30, "肌肉控制量", "Kg"),
    TYPE_MUSCLE_MASS_RATE(31,"肌肉率", "%"),
    TYPE_HEIGHT(32,"身高", "cm");

    public static BodyTypeEnum findByCode(Integer code){
        if(code == null){
            return null;
        }
        for (BodyTypeEnum value : BodyTypeEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未找到身体数据类型");
    }

    BodyTypeEnum(Integer code, String name, String unit) {
        this.code = code;
        this.name = name;
        this.unit = unit;
    }

    private final Integer code;

    private final String name;

    private final String unit;
}
