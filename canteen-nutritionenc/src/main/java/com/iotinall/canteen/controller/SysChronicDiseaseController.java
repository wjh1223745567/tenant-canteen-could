package com.iotinall.canteen.controller;


import com.iotinall.canteen.dto.nutritionenc.FeignChronicDiseaseDto;
import com.iotinall.canteen.dto.nutritionenc.FeignSimMessProdReq;
import com.iotinall.canteen.service.SysChronicDiseaseService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Api(value = "疾病接口")
@RestController
@RequestMapping(value = "chronic_disease")
public class SysChronicDiseaseController {

    @Resource
    private SysChronicDiseaseService diseaseService;

    @GetMapping(value = "findDtoById/{id}")
    public FeignChronicDiseaseDto findDtoById(@PathVariable(value = "id") Long id){
        return diseaseService.findDtoById(id);
    }

    @PostMapping(value = "cacheIsTheDiseaseUsed")
    public Integer cacheIsTheDiseaseUsed(@Valid @RequestBody FeignSimMessProdReq req){
        return diseaseService.cacheIsTheDiseaseUsed(req);
    }

    @PostMapping(value = "findByIds")
    public List<FeignChronicDiseaseDto> findByIds(@RequestBody Set<Long> ids){
        return diseaseService.findByIds(ids);
    }
}
