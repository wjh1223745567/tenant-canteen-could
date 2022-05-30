package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.ValidateCodeUtil;
import com.iotinall.canteen.dto.custom.CustomDishAddReq;
import com.iotinall.canteen.dto.custom.CustomDishDTO;
import com.iotinall.canteen.entity.NutritionCustomDish;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.dto.nutritionenc.FeignDishDto;
import com.iotinall.canteen.repository.NutritionCustomDishRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义食物逻辑处理类
 *
 * @author loki
 * @date 2020/04/13 13:55
 */
@Service
public class NutritionCustomDishService {
    @Resource
    private FeignDishService dishService;

    @Resource
    private FeignEmployeeService employeeService;

    @Resource
    private NutritionCustomDishRepository customDishRepository;

    @Resource
    private FeignMessProductService messProductService;

    public Long create(CustomDishAddReq req) {
        Long empId = SecurityUtils.getUserId();

        if (StringUtils.isBlank(req.getSysDishId())) {
            throw new BizException("", "请选择食物");
        }
        FeignDishDto feignDishDto = dishService.findDtoById(req.getSysDishId());
        FeignMessProdDto feignMessProdDto = null;
        if(feignDishDto == null && ValidateCodeUtil.isNumber(req.getSysDishId())){
            feignMessProdDto = messProductService.findDtoById(Long.valueOf(req.getSysDishId()));
        }

        if (feignDishDto == null && feignMessProdDto == null) {
            throw new BizException("", "该食物不存在");
        }

        NutritionCustomDish customDish = new NutritionCustomDish();
        if(feignDishDto != null){
            customDish.setDishId(req.getSysDishId());
        }else {
            customDish.setProductId(Long.valueOf(req.getSysDishId()));
        }

        //冗余name ,查询需要
        customDish.setName(feignDishDto != null ? feignDishDto.getName() : feignMessProdDto.getName());
        customDish.setImg(feignDishDto != null ? feignDishDto.getImg() : feignMessProdDto.getImg());
        customDish.setEmployeeId(empId);
        customDish.setCreateTime(LocalDateTime.now());
        customDishRepository.save(customDish);
        return customDish.getId();
    }

    public void delete(Long id) {
        this.customDishRepository.deleteById(id);
    }

    public List<CustomDishDTO> findAll() {
        Long userId = SecurityUtils.getUserId();
        List<NutritionCustomDish> customDishes = customDishRepository.findAllByEmployeeId(userId);

        return customDishes.stream().map(item -> new CustomDishDTO()
                .setId(item.getId())
                .setName(item.getName())
                .setEnergy(item.getNutrition().getEnergy())
                .setImg(item.getImg())
        ).collect(Collectors.toList());
    }
}
