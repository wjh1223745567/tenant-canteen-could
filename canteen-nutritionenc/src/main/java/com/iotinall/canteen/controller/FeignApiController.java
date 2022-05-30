package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.dto.nutritionenc.FeignCraftDto;
import com.iotinall.canteen.dto.nutritionenc.FeignCuisineDto;
import com.iotinall.canteen.dto.nutritionenc.FeignFlavourDto;
import com.iotinall.canteen.dto.nutritionenc.FeignMessProductCuisineDto;
import com.iotinall.canteen.service.*;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Api(value = "feign接口")
@RestController
@RequestMapping(value = "nutritionenc_feign")
public class FeignApiController {

    @Resource
    private SysFlavourService flavourService;

    @Resource
    private SysCraftService craftService;

    @Resource
    private SysMaterialService sysMaterialService;

    @Resource
    private MasterCuisineService masterCuisineService;

    @Resource
    private DishBizService dishBizService;


    @GetMapping(value = "flavour/findDtoById/{id}")
    public FeignFlavourDto findFlavourDtoById(@PathVariable(value = "id") String id){
        return flavourService.findDtoById(id);
    }

    @GetMapping(value = "craft/findDtoById/{id}")
    public FeignCraftDto findCraftDtoById(@PathVariable(value = "id") String id){
        return craftService.findDtoById(id);
    }

    @PostMapping(value = "material/findByIds")
    public List<NutritionNone> findNutriByIds(@RequestBody List<String> ids){
        return sysMaterialService.findByIds(ids);
    }

    @PostMapping(value = "cuisine/findAllChildrenId")
    public Set<String> findAllChildrenId(@RequestBody Set<String> ids){
        return masterCuisineService.findAllChildrenId(ids);
    }

    @PostMapping(value = "cuisine/findByIds")
    public List<FeignCuisineDto> findByIds(@RequestBody Set<String> ids){
        return masterCuisineService.findByIds(ids);
    }

    @PostMapping(value = "material/generateNutrition")
    public NutritionNone generateNutrition(@RequestParam(value = "material") String material){
        return this.dishBizService.generateNutritionNutr(material);
    }

    @GetMapping(value = "cuisine/getCuisineMessProductStat")
    public List<FeignMessProductCuisineDto> getCuisineMessProductStat(){
        return this.masterCuisineService.getCuisineMessProductStat();
    }
}
