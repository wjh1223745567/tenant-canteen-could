package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.constants.BodySizeEnum;
import com.iotinall.canteen.constants.BodyTypeEnum;
import com.iotinall.canteen.dto.sharingscale.SharingScaleDto;
import com.iotinall.canteen.dto.sharingscale.SharingScaleMeasureDto;
import com.iotinall.canteen.dto.sharingscale.StandardRemarkDto;
import com.iotinall.canteen.dto.sharingscale.StandardSubRemarkDto;
import com.iotinall.canteen.dto.stature.StatureAddReq;
import com.iotinall.canteen.dto.stature.StatureDTO;
import com.iotinall.canteen.entity.NutritionPersonRecord;
import com.iotinall.canteen.repository.NutritionPersonRecordRepository;
import com.kingnew.health.measure.calc.QNScaleDataDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 共享秤对接
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SharingScaleService {

    @Resource
    private NutritionStatureRecordService recordService;

    @Resource
    private NutritionPersonRecordService nutritionPersonRecordService;

    @Resource
    private NutritionPersonRecordRepository personRecordRepository;

    /**
     * 接收数据
     *
     * @param str
     */
    public void receiveData(String str) {
        NutritionPersonRecord personRecord = nutritionPersonRecordService.findPersonRecord();

        if(str.contains("?data=")){
            str = str.substring(str.indexOf("?data=") + 6);
        }

        if (personRecord == null) {
            throw new BizException("11", "未建立个人档案");
        }
        Integer nowage = (LocalDate.now().getYear() - personRecord.getBirthDate().getYear());
        log.info("共享秤参数：{},{},{},{},{}", str, personRecord.getPersonHeight(), Objects.equals(0, personRecord.getGender()) ? 2 : 1, nowage, 0);
        String data = QNScaleDataDecoder.generateBodyData(str, personRecord.getPersonHeight(), personRecord.getGender(), nowage, 0);
        log.info("共享秤数据接取：{}", data);
        SharingScaleDto sharingScaleDto = JSON.parseObject(data, SharingScaleDto.class);

        if (sharingScaleDto == null || sharingScaleDto.getMeasurement() == null || sharingScaleDto.getMeasurement().isEmpty()) {
            throw new BizException("", "获取共享秤失败");
        }

        SharingScaleMeasureDto weight = sharingScaleDto.getMeasurement().stream().filter(item -> Objects.equals(item.getType(), BodyTypeEnum.TYPE_WEIGHT.getCode())).findFirst().orElse(null);

        if(weight != null){
            personRecord.setPersonWeight(weight.getValue().intValue());
            this.personRecordRepository.save(personRecord);
        }

        StatureAddReq addReq = new StatureAddReq()
                .setDate(LocalDate.now())
                .setStatures(sharingScaleDto.getMeasurement().stream().map(item -> {
                    BodyTypeEnum typeEnum = BodyTypeEnum.findByCode(item.getType());
                    StatureDTO statureDTO = new StatureDTO()
                            .setValue(item.getValue())
                            .setCode(String.valueOf(item.getType()))
                            .setName(typeEnum.getName())
                            .setUnit(typeEnum.getCode().equals(BodyTypeEnum.TYPE_BODY_SHAPE.getCode()) ? BodySizeEnum.findByCode(item.getValue().intValue()).getName() : typeEnum.getUnit());
                    return statureDTO;
                }).collect(Collectors.toList()));

        if(personRecord.getPersonHeight() != null){
            StatureDTO height = new StatureDTO()
                    .setName(BodyTypeEnum.TYPE_HEIGHT.getName())
                    .setCode(String.valueOf(BodyTypeEnum.TYPE_HEIGHT.getCode()))
                    .setUnit(BodyTypeEnum.TYPE_HEIGHT.getUnit())
                    .setValue(personRecord.getPersonHeight() != null ? BigDecimal.valueOf(personRecord.getPersonHeight()) : null);

            addReq.getStatures().add(0, height);
        }

        //体脂量
        SharingScaleMeasureDto bodyfat = sharingScaleDto.getMeasurement().stream().filter(item -> Objects.equals(item.getType(), BodyTypeEnum.TYPE_BODYFAT.getCode())).findFirst().orElse(null);
        if(bodyfat != null && weight != null){
            StatureDTO height = new StatureDTO()
                    .setName(BodyTypeEnum.TYPE_FAT_MASS_INDEX.getName())
                    .setCode(String.valueOf(BodyTypeEnum.TYPE_FAT_MASS_INDEX.getCode()))
                    .setUnit(BodyTypeEnum.TYPE_FAT_MASS_INDEX.getUnit())
                    .setValue(bodyfat.getValue().multiply(weight.getValue()).divide(BigDecimal.valueOf(100)));
            addReq.getStatures().add(height);
        }

        //含水量
        SharingScaleMeasureDto water = sharingScaleDto.getMeasurement().stream().filter(item -> Objects.equals(item.getType(), BodyTypeEnum.TYPE_WATER.getCode())).findFirst().orElse(null);
        if(water != null && weight != null){
            StatureDTO height = new StatureDTO()
                    .setName(BodyTypeEnum.TYPE_WATER_CONTENT_INDEX.getName())
                    .setCode(String.valueOf(BodyTypeEnum.TYPE_WATER_CONTENT_INDEX.getCode()))
                    .setUnit(BodyTypeEnum.TYPE_WATER_CONTENT_INDEX.getUnit())
                    .setValue(water.getValue().multiply(weight.getValue()).divide(BigDecimal.valueOf(100)));
            addReq.getStatures().add(height);
        }

        //蛋白质
        SharingScaleMeasureDto protein = sharingScaleDto.getMeasurement().stream().filter(item -> Objects.equals(item.getType(), BodyTypeEnum.TYPE_PROTEIN.getCode())).findFirst().orElse(null);
        if(protein != null && weight != null){
            StatureDTO height = new StatureDTO()
                    .setName(BodyTypeEnum.TYPE_PROTEIN_MASS_INDEX.getName())
                    .setCode(String.valueOf(BodyTypeEnum.TYPE_PROTEIN_MASS_INDEX.getCode()))
                    .setUnit(BodyTypeEnum.TYPE_PROTEIN_MASS_INDEX.getUnit())
                    .setValue(protein.getValue().multiply(weight.getValue()).divide(BigDecimal.valueOf(100)));
            addReq.getStatures().add(height);
        }

        for (StatureDTO stature : addReq.getStatures()) {
            BodyTypeEnum typeEnum = BodyTypeEnum.findByCode(Integer.valueOf(stature.getCode()));
            String standard = weight != null ? getStandard(typeEnum, stature.getValue(), personRecord.getPersonHeight().doubleValue(), personRecord.getGender(), weight.getValue().doubleValue(), nowage).getName() : "";
            stature.setStandard(standard);
        }
        recordService.updateAndCreate(addReq);
    }

    /**
     * 获取是否达标
     * @param typeEnum
     * @param value
     * @param height
     * @return
     */
    public static StandardRemarkDto getStandard(BodyTypeEnum typeEnum, BigDecimal value, Double height, Integer gender, Double weight, Integer age){
        StandardRemarkDto remarkDto = new StandardRemarkDto()
                .setRemarks(new ArrayList<>())
                .setScaleValue(new ArrayList<>());
        Double doubValue = value.doubleValue();
        switch (typeEnum){
            case TYPE_MUSCLE_MASS:{
                StandardSubRemarkDto subRemarkDto = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("您缺少足够的肌肉，需要加强运动，增加肌肉。");
                StandardSubRemarkDto subRemarkDto1 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("您的肌肉比较发达，继续保持。");
                StandardSubRemarkDto subRemarkDto2 = new StandardSubRemarkDto()
                        .setName("充足")
                        .setRemark("您的肌肉比较发达，继续保持。");
                remarkDto.getRemarks().add(subRemarkDto);
                remarkDto.getRemarks().add(subRemarkDto1);
                remarkDto.getRemarks().add(subRemarkDto2);

                if(gender == 0){
                    if(height < 150){
                        if(doubValue < 29.1){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 34.7){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("充足");
                        }
                        remarkDto.setScaleValue(Arrays.asList(29.1, 34.7));
                    }else if(height <= 160){
                        if(doubValue < 32.9){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 37.5){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("充足");
                        }
                        remarkDto.setScaleValue(Arrays.asList(32.9, 37.5));
                    }else {
                        if(doubValue < 36.5){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 42.5){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("充足");
                        }
                        remarkDto.setScaleValue(Arrays.asList(36.5, 42.5));
                    }
                }else{
                    if(height < 160){
                        if(doubValue < 38.5){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 46.5){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("充足");
                        }
                        remarkDto.setScaleValue(Arrays.asList(38.5, 46.5));
                    }else if(height <= 170){
                        if(doubValue < 44){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 52.4){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("充足");
                        }
                        remarkDto.setScaleValue(Arrays.asList(44., 52.4));
                    }else {
                        if(doubValue < 49.4){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 59.4){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("充足");
                        }
                        remarkDto.setScaleValue(Arrays.asList(49.4, 59.4));
                    }
                }
                break;
            }

            case TYPE_BODY_SHAPE:{
                for (BodySizeEnum bodySizeEnum : BodySizeEnum.values()) {
                    StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                            .setName(bodySizeEnum.getName())
                            .setRemark(bodySizeEnum.getRemark());
                    remarkDto.getRemarks().add(remarkDto1);
                }

                remarkDto.setName(BodySizeEnum.findByCode(doubValue.intValue()) != null ? BodySizeEnum.findByCode(doubValue.intValue()).getName() : "");
                break;
            }

            case TYPE_BODY_AGE: {
                remarkDto.getRemarks().add(new StandardSubRemarkDto().setRemark("理想的体内年龄＝实际年龄 * ２ / ３，您还有年轻空间，加油！"));
                if(doubValue <= age){
                    remarkDto.setName("达标");
                }else{
                    remarkDto.setName("不达标");
                }

                break;
            }

            case TYPE_VISFAT:{
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark(doubValue.intValue() == 9 ? "虽然处于标准范围，但内脏脂肪已经开始堆积，请积极运动，改变久坐不动、饮食不均衡等不良习惯。" : "内脏脂肪指数标准，暂时没有太大风险。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("内脏脂肪指数偏高，持续保持均衡的饮食和适当的运动，以标准程度为目标，进行适当运动和限制卡路里。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("严重偏高")
                        .setRemark("内脏脂肪指数危险，罹患心脏病、高血压、高血脂和Ⅱ型糖尿病风险大，您迫切需要控制体重、积极运动和限制饮食。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(doubValue <= 9){
                    remarkDto.setName("标准");
                }else if(doubValue <= 14){
                    remarkDto.setName("偏高");
                }else{
                    remarkDto.setName("严重偏高");
                }

                remarkDto.setScaleValue(Arrays.asList(9., 14.));

                break;
            }

            case TYPE_PROTEIN: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("蛋白质不足会引起基础代谢减少，也会引起肌肉的数量减少。坚持长期运动，适当提高肌肉比例，辅助于蛋白质的补充，可以提升蛋白质比例。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("您的蛋白质处于标准水平。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("充足")
                        .setRemark("蛋白质比例充足。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(gender == 0){
                    if(doubValue < 14){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 16){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("充足");
                    }
                    remarkDto.setScaleValue(Arrays.asList(14., 16.));
                }else{
                    if(doubValue < 16){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 18){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("充足");
                    }
                    remarkDto.setScaleValue(Arrays.asList(16., 18.));
                }

                break;
            }

            case TYPE_BMR: {
                Double standardMetabolism;

                if(gender == 0){
                    if(age <= 29){
                        standardMetabolism = 23.6;
                    }else if(age <= 49){
                        standardMetabolism = 21.7;
                    }else if(age <= 69){
                        standardMetabolism = 20.7;
                    }else{
                        standardMetabolism = 20.7;
                    }
                }else{
                    if(age <= 29){
                        standardMetabolism = 24.;
                    }else if(age <= 49){
                        standardMetabolism = 22.3;
                    }else if(age <= 69){
                        standardMetabolism = 21.5;
                    }else{
                        standardMetabolism = 21.5;
                    }
                }

                double standardTotal = standardMetabolism * weight;

                if(doubValue >= standardTotal){
                    remarkDto.setName("达标");
                }else{
                    remarkDto.setName("不达标");
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("达标")
                        .setRemark("您的标准基础代谢率为 " + decimalFormat.format(standardTotal) + " kcal，处于达标状态。保持基础代谢率最有效的方式是每天都进行适量的运动。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("不达标")
                        .setRemark("您的标准基础代谢率为 " + decimalFormat.format(standardTotal) + " kcal,目前处于未达标状态。持续轻量运动能够提高身体的基础代谢率，而节食基础代 谢会大幅下降。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);

                break;
            }

            case TYPE_BONE: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                    .setName("偏低")
                    .setRemark("您的骨量水平偏低。长期低钙饮食、缺乏运动、过度减肥等都可能引起骨量偏低，多吃含钙高的食物，多晒太阳，多运动及时补钙。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("您的骨量水平标准。骨量在短期内不会出现明显的变化，您只要保证健康的饮食和适当的锻炼，就可以维持稳定健康的骨量水平。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("您的骨量水平偏高。说明骨骼中包含的钙等无机盐的含量非常充分，但要注意防范肾结石、低血压的风险，尽量避免高钙摄入。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(gender == 0){
                    if(weight <= 45){
                        if(doubValue < 1.6){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 2.0){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("偏高");
                        }
                        remarkDto.setScaleValue(Arrays.asList(1.6, 2.0));
                    }else if(weight < 60){
                        if(doubValue < 2.0){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 2.4){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("偏高");
                        }
                        remarkDto.setScaleValue(Arrays.asList(2.0, 2.4));
                    }else{
                        if(doubValue < 2.3){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 2.7){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("偏高");
                        }
                        remarkDto.setScaleValue(Arrays.asList(2.3, 2.7));
                    }
                }else{
                    if(weight <= 60){
                        if(doubValue < 2.3){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 2.7){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("偏高");
                        }
                        remarkDto.setScaleValue(Arrays.asList(2.3,2.7));
                    }else if(weight < 75){
                        if(doubValue < 2.7){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 3.1){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("偏高");
                        }
                        remarkDto.setScaleValue(Arrays.asList(2.7, 3.1));
                    }else{
                        if(doubValue < 3.0){
                            remarkDto.setName("偏低");
                        }else if(doubValue <= 3.4){
                            remarkDto.setName("标准");
                        }else{
                            remarkDto.setName("偏高");
                        }
                        remarkDto.setScaleValue(Arrays.asList(3.,3.4));
                    }
                }
                break;
            }

            case TYPE_WATER: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("体水分率偏低，规律的饮食习惯和每天喝足8杯水可以维持正常的体水分水平，充足的水分可以促进代谢，带走废物和身体毒素。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("身体水分率处于标准值，适量饮水，适当运动，均衡饮食，保持身体水分的平衡。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("充足")
                        .setRemark("身体水分含量高，细胞活性高。充足的水分能帮助您更好 地消化食物和吸收养分，并促进代谢，带走废物和毒素。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(gender == 0){
                    if(doubValue < 45){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 60){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("充足");
                    }
                    remarkDto.setScaleValue(Arrays.asList(45., 60.));
                }else{
                    if(doubValue < 55){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 65){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("充足");
                    }
                    remarkDto.setScaleValue(Arrays.asList(55., 65.));
                }

                break;
            }

            case TYPE_SCORE:{
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto();
                if(doubValue < 60){
                    remarkDto1.setRemark("您的身体状况简直不能忍...接受现实还是改变自己,做出选择吧！！");
                }else if(doubValue < 70){
                    remarkDto1.setRemark("良好的体型是健康表现之一，您可能要变形了！");
                }else if(doubValue < 80){
                    remarkDto1.setRemark(gender == 0 ? "您正在远离女神的体质了，运动起来吧！" : "您正在远离男神的体质了，运动起来吧！");
                }else if(doubValue < 90){
                    remarkDto1.setRemark(gender == 0 ? "您的体质已经接近女神了，继续努力就能梦想成真！！" : "您的体质已经接近男神了，继续努力就能梦想成真！！");
                }else if(doubValue < 95){
                    remarkDto1.setRemark("神体质、好身材，您值得拥有。go go go！！");
                }else {
                    remarkDto1.setRemark("您的身体状况已经超神，妥善保养就行了！！");
                }
                break;
            }

            case TYPE_SUBFAT: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("适量的皮下脂肪能够保护内脏和抵御寒冷，适量增加高蛋白、高热量食物可以增加脂肪。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("您的皮下脂肪率处于标准范围。适当的运动量和合理的饮食就能保持适量的皮下脂肪。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("皮下脂肪过多是外表肥胖的主要原因，除了有氧减脂以外，多进行增肌训练，肌肉的增加可以让您拥有更完美的体型。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(gender == 0){
                    if(doubValue < 18.5){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 26.7){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("偏高");
                    }
                    remarkDto.setScaleValue(Arrays.asList(18.5, 26.7));
                }else{
                    if(doubValue < 8.6){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 16.7){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("偏高");
                    }
                    remarkDto.setScaleValue(Arrays.asList(8.6, 16.7));
                }

                break;
            }

            case TYPE_MUSCLE:{
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("您的骨胳肌比率低于理想范围，跟多静态活动、不运动有 关，会导致基础代谢率降低，腰酸背痛，力量下降，外在表现是发胖，也容易诱发心血管疾病。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("您的骨胳肌比率处于标准范围。运动量过少或者节食会导致肌肉流失，请保持适当的运动量和合理的饮食。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("如果脂肪比例正常，您是一个比较喜欢运动的人，适当的 骨胳肌比率能够显示您健壮的体型，但过高的骨胳肌比率可能会影响您的灵活性。如果脂肪比例偏低，您的身材可能偏瘦，平衡身体各项参数，您就能拥有健康标准的身材。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(gender == 0){
                    if(doubValue < 40){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 50){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("偏高");
                    }
                    remarkDto.setScaleValue(Arrays.asList(40., 50.));
                }else{
                    if(doubValue < 49){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 59){
                        remarkDto.setName("标准");
                    }else{
                        remarkDto.setName("偏高");
                    }
                    remarkDto.setScaleValue(Arrays.asList(49., 59.));
                }

                break;
            }

            case TYPE_LBM: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setRemark("去脂体重是指除脂肪以外身体其他成分的重量，肌肉是其中的主要部分。通过该指标可以看出您锻炼的效果，也可以看出您减肥的潜力哦！");

                remarkDto.getRemarks().add(remarkDto1);
                break;
            }

            case TYPE_WEIGHT: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("严重偏低")
                        .setRemark("长期体重过轻会导致一系统列问题，如脱发、厌食症等，身体机能会下降，需要加强营养，多吃高蛋白食物，摄入更多的热量以增加体重。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("体重偏低，身体消瘦，建议加强营养，平衡饮食，多吃高蛋白食物，摄入更多的热量以增加体重。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("恭喜您拥有理想的体重，保持合理健康的生活方式，适量参加运动，您就可以维持标准体重了。");

                StandardSubRemarkDto remarkDto4 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("体重偏重，略显肥胖，建议一周进行３-５次有氧运动，减少主食（米饭面食等）的摄入，增加高纤维粗粮比例。");

                StandardSubRemarkDto remarkDto5 = new StandardSubRemarkDto()
                        .setName("严重偏高")
                        .setRemark("体重严重超标，建议低脂、低胆固醇、高纤维膳食，补充多种维生素，增加运动量进行体重控制。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);
                remarkDto.getRemarks().add(remarkDto4);
                remarkDto.getRemarks().add(remarkDto5);

                double banlace;
                if(gender == 0){
                    banlace = ((height * 1.37) - 110) * 0.45;
                }else{
                    banlace = (height - 80) * 0.7;
                }

                if(weight <= 0.8 * banlace){
                    remarkDto.setName("严重偏低");
                }else if(weight < 0.9 * banlace){
                    remarkDto.setName("偏低");
                }else if(weight <= 1.1 * banlace){
                    remarkDto.setName("标准");
                }else if(weight <= 1.2 * banlace){
                    remarkDto.setName("偏高");
                }else{
                    remarkDto.setName("严重偏高");
                }

                remarkDto.setScaleValue(Arrays.asList( 0.8 * banlace, 0.9 * banlace, 1.1 * banlace, 1.2 * banlace));

                break;
            }

            case TYPE_BODYFAT: {
                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("当身体摄取到优质营养，并且令到小肠绒毛正常运作，就可以达到正常的脂肪比例。为了增重，食物最好以易消化、高蛋白、高热量为原则。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("目前您的体脂率处于标准范围，保持好的饮食方式和生活习惯是保持健康身材的最佳途径。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("要匀称不显胖，每日有氧运动要持续30分钟，体脂率才 会开始燃烧，快走、慢跑、游泳、爬楼梯、骑自行车都是很好的选择。");

                StandardSubRemarkDto remarkDto4 = new StandardSubRemarkDto()
                        .setName("严重偏高")
                        .setRemark("您的体内囤积了太多脂肪，必须检测血压、血糖、肝功能 等情况，是否潜藏危害。赶快开始您的减肥大战，坚持饮食控制、运动及改变生活方式。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);
                remarkDto.getRemarks().add(remarkDto4);

                if(gender == 0){
                    if(doubValue < 21){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 31){
                        remarkDto.setName("标准");
                    }else if(doubValue <= 36){
                        remarkDto.setName("偏高");
                    }else{
                        remarkDto.setName("严重偏高");
                    }
                    remarkDto.setScaleValue(Arrays.asList(21., 31., 36.));
                }else{
                    if(doubValue < 11){
                        remarkDto.setName("偏低");
                    }else if(doubValue <= 21){
                        remarkDto.setName("标准");
                    }else if(doubValue <= 26){
                        remarkDto.setName("偏高");
                    }else{
                        remarkDto.setName("严重偏高");
                    }
                    remarkDto.setScaleValue(Arrays.asList(11., 21., 26.));
                }

                break;
            }

            case TYPE_BMI: {

                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("需要提升体能健康增重，适当多吃高热量、高蛋白、高脂 肪饮食，多做力量运动如举重、俯卧撑、仰卧起坐等。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark(gender == 0 ? "BMI达标，如果腰围也属于建议的尺寸女性（计算公式：身高(厘米) x 1/2 - 13=标准腰围(厘米)）就更加理想了。" : "BMI达标，如果腰围也属于建议的尺寸男性（计算公式：身高(厘米) x 1/2-10=标准腰围(厘米）。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("BMI超标，建议选择比较健康的方法减重，如控制饮食、 改变不良生活习惯和参加跑步、跳绳、打篮球、踢足球等 消耗体能的运动。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(doubValue < 18.5){
                    remarkDto.setName("偏低");
                }else if(doubValue <= 25){
                    remarkDto.setName("标准");
                }else{
                    remarkDto.setName("偏高");
                }

                remarkDto.setScaleValue(Arrays.asList(18.5, 25.));
                break;
            }

            case TYPE_HEART_RATE: {

                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("过低")
                        .setRemark("心率超过低于40次，大多见于心脏病病人，常有心悸、 胸闷、心前区不适，应及早检查，对症治疗。也有例外：心率低于40次的有的是很健康的人，长期从事重体力劳动和进行激烈运动的人，他们的心脏得到了锻炼，心跳次数比常人要少得多。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("窦性心动过缓。可见于长期从事体力劳动和运动员；病理性的见于甲状腺低下、颅内压增高、阻塞性黄胆、以及洋地黄、奎尼丁、或心得安类药物过量或中毒。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("健康成人的心率为60～100次/分，大多数为60～80次/分，女性稍快。");

                StandardSubRemarkDto remarkDto4 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("窦性心动过速。常见于正常人运动、兴奋、激动、吸烟、饮酒和喝浓茶后；也可见于发热、休克、贫血、甲亢、心力衰竭及应用阿托品、肾上腺素、麻黄素等。");

                StandardSubRemarkDto remarkDto5 = new StandardSubRemarkDto()
                        .setName("过高")
                        .setRemark("心率超过160次，大多见于心脏病病人，常有心悸、胸闷、心前区不适，应及早检查，对症治疗。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);
                remarkDto.getRemarks().add(remarkDto4);
                remarkDto.getRemarks().add(remarkDto5);

                if(doubValue <= 40){
                    remarkDto.setName("过低");
                }else if(doubValue <= 60){
                    remarkDto.setName("偏低");
                }else if(doubValue <= 100){
                    remarkDto.setName("标准");
                }else if(doubValue <= 160){
                    remarkDto.setName("偏高");
                }else{
                    remarkDto.setName("过高");
                }

                remarkDto.setScaleValue(Arrays.asList(40., 60., 100., 160.));

                break;
            }

            case TYPE_HEART_INDEX:{

                StandardSubRemarkDto remarkDto1 = new StandardSubRemarkDto()
                        .setName("偏低")
                        .setRemark("低于正常范围很多会引起脑供血严重不足,记忆力减退 等。具体的判断不能只依靠这一项的指标,需结合心电图 及胸片的判断。");

                StandardSubRemarkDto remarkDto2 = new StandardSubRemarkDto()
                        .setName("标准")
                        .setRemark("你的心脏指数处于标准水平。");

                StandardSubRemarkDto remarkDto3 = new StandardSubRemarkDto()
                        .setName("偏高")
                        .setRemark("高于正常值说明泵血过强,容易引起心慌。具体的判断不能只依靠这一项的指标,需结合心电图及胸片的判断。");

                remarkDto.getRemarks().add(remarkDto1);
                remarkDto.getRemarks().add(remarkDto2);
                remarkDto.getRemarks().add(remarkDto3);

                if(doubValue <= 2.5){
                    remarkDto.setName("偏低");
                }else if(doubValue <= 4.2){
                    remarkDto.setName("标准");
                }else{
                    remarkDto.setName("偏高");
                }

                remarkDto.setScaleValue(Arrays.asList(2.5, 4.2));

                break;
            }
        }

        return remarkDto;
    }

}
