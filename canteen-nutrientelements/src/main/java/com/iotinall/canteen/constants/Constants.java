package com.iotinall.canteen.constants;

public abstract class Constants {

    /**
     * 0-早餐 1-午餐 2-晚餐 3-加餐
     */
    public final static int BREAKFAST = 0;
    public final static int LUNCH = 1;
    public final static int DINNER = 2;
    public final static int SNACK = 3;

    /**
     * 0-系统中食物 1-自定义食物
     */
    public final static int SYS_DISH = 0;

    /**
     * 0-女性 1-男性
     */
    public final static int MALE = 0;
    public final static int FEMALE = 1;


    /**
     * 身体目标类型 0-减脂 1-增肌 2-保持体形
     */
    public final static int FAT_REDUCTION = 0;
    public final static int INCREASING_MUSCLE = 1;
    public final static int KEEP = 2;

    /**
     * 身体目标类型 0-年龄 1-怀孕 2-乳母
     */
    public final static int DEFAULT = 0;
    public final static int PREGNANT = 1;
    public final static int WET_NURSE = 2;

    /**
     * 计算类型
     * 1-能量
     * 2-蛋白质
     * 3-脂肪
     * 4-碳水化合物
     * 5-摄入膳食纤维
     */
    public final static int CALCULATE_TYPE_ENERGY = 1;
    public final static int CALCULATE_TYPE_PROTEIN = 2;
    public final static int CALCULATE_TYPE_FAT = 3;
    public final static int CALCULATE_TYPE_CARBS = 4;
    public final static int CALCULATE_TYPE_DIETARY_FIBER = 5;

}
