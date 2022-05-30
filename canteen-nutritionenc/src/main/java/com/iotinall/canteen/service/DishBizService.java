package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.dto.messprod.MessProductMaterialDTO;
import com.iotinall.canteen.dto.dish.NutritionDTO;
import com.iotinall.canteen.entity.SysMaterial;
import com.iotinall.canteen.repository.SysMaterialRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统菜谱相关
 */
@Component
public class DishBizService {
    @Resource
    private SysMaterialRepository materialRepository;

    /**
     * 根据原材料营养元素计算菜品营养元素
     *
     * @author loki
     * @date 2020/04/20 13:52
     */
    public NutritionDTO generateNutrition(String material) {
        if (StringUtils.isBlank(material)) {
            return null;
        }
        List<MessProductMaterialDTO> materialList = JSON.parseObject(material, new TypeReference<List<MessProductMaterialDTO>>(){});
        return this.calculateNutrition(materialList);
    }

    public NutritionNone generateNutritionNutr(String material){
        NutritionDTO nutritionDTO = this.generateNutrition(material);
        if(nutritionDTO != null){
            NutritionNone nutritionNone = new NutritionNone();
            BeanUtils.copyProperties(nutritionDTO, nutritionNone);
            return nutritionNone;
        }else{
            return null;
        }
    }

    /**
     * 根据原材料的营养元素计算菜品的营养元素
     *
     * @author loki
     * @date 2020/04/20 14:22
     */
    private NutritionDTO calculateNutrition(List<MessProductMaterialDTO> materialList) {
        NutritionDTO nutrition = new NutritionDTO();
        SysMaterial sysMaterial;
        for (MessProductMaterialDTO material : materialList) {
            sysMaterial = materialRepository.findById(material.getId()).orElse(null);
            if (sysMaterial == null) {
                continue;
            }
            nutrition.setEnergy(nutrition.getEnergy() + sysMaterial.getEnergy());
            nutrition.setProtein(nutrition.getProtein() + sysMaterial.getProtein());
            nutrition.setFat(nutrition.getFat() + sysMaterial.getFat());
            nutrition.setCarbohydrate(nutrition.getCarbohydrate() + sysMaterial.getCarbohydrate());
            nutrition.setDietaryFiber(nutrition.getDietaryFiber() + sysMaterial.getDietaryFiber());
            nutrition.setVb9(nutrition.getVb9() + sysMaterial.getVb9());
            nutrition.setVa(nutrition.getVa() + sysMaterial.getVa());
            nutrition.setVc(nutrition.getVc() + sysMaterial.getVc());

            nutrition.setVd(nutrition.getVd() + sysMaterial.getVd());
            nutrition.setVe(nutrition.getVe() + sysMaterial.getVe());
            nutrition.setVk(nutrition.getVk() + sysMaterial.getVk());
            nutrition.setVb1(nutrition.getVb1() + sysMaterial.getVb1());
            nutrition.setVb2(nutrition.getVb2() + sysMaterial.getVb2());
            nutrition.setVb6(nutrition.getVb6() + sysMaterial.getVb6());
            nutrition.setVb12(nutrition.getVb12() + sysMaterial.getVb12());
            nutrition.setPanAcid(nutrition.getPanAcid() + sysMaterial.getPanAcid());

            nutrition.setDfe(nutrition.getDfe() + sysMaterial.getDfe());
            nutrition.setVh(nutrition.getVh() + sysMaterial.getVh());
            nutrition.setCarotene(nutrition.getCarotene() + sysMaterial.getCarotene());
            nutrition.setRiboflavin(nutrition.getRiboflavin() + sysMaterial.getRiboflavin());
            nutrition.setThiamine(nutrition.getThiamine() + sysMaterial.getThiamine());
            nutrition.setNiacin(nutrition.getNiacin() + sysMaterial.getNiacin());
            nutrition.setNiacinEquivalent(nutrition.getNiacinEquivalent() + sysMaterial.getNiacinEquivalent());
            nutrition.setCholine(nutrition.getCholine() + sysMaterial.getCholine());

            nutrition.setCholesterol(nutrition.getCholesterol() + sysMaterial.getCholesterol());
            nutrition.setRe(nutrition.getRe() + sysMaterial.getRe());
            nutrition.setCa(nutrition.getCa() + sysMaterial.getCa());
            nutrition.setP(nutrition.getP() + sysMaterial.getP());
            nutrition.setK(nutrition.getK() + sysMaterial.getK());
            nutrition.setNa(nutrition.getNa() + sysMaterial.getNa());
            nutrition.setMg(nutrition.getMg() + sysMaterial.getMg());
            nutrition.setI(nutrition.getI() + sysMaterial.getI());

            nutrition.setFe(nutrition.getFe() + sysMaterial.getFe());
            nutrition.setZn(nutrition.getZn() + sysMaterial.getZn());
            nutrition.setSe(nutrition.getSe() + sysMaterial.getSe());
            nutrition.setCu(nutrition.getCu() + sysMaterial.getCu());
            nutrition.setMn(nutrition.getMn() + sysMaterial.getMn());
            nutrition.setCr(nutrition.getCr() + sysMaterial.getCr());
            nutrition.setMo(nutrition.getMo() + sysMaterial.getMo());
            nutrition.setCl(nutrition.getCl() + sysMaterial.getCl());
        }

        return nutrition;
    }
}
