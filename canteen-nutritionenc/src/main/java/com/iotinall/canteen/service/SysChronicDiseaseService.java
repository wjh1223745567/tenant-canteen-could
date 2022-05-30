package com.iotinall.canteen.service;

import com.iotinall.canteen.entity.ChronicDisease;
import com.iotinall.canteen.dto.nutritionenc.FeignChronicDiseaseDto;
import com.iotinall.canteen.dto.nutritionenc.FeignSimMessProdReq;
import com.iotinall.canteen.repository.SysChronicDiseaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysChronicDiseaseService {

    @Resource
    private SysChronicDiseaseRepository sysChronicDiseaseRepository;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    public FeignChronicDiseaseDto findDtoById(Long id){
        return this.sysChronicDiseaseRepository.findById(id).map(item -> new FeignChronicDiseaseDto()
                    .setId(item.getId())
                    .setName(item.getName())
        ).orElse(null);
    }

    /**
     * 根据疾病获取事宜菜谱
     * @param messProdReq
     * @return
     */
    public Integer cacheIsTheDiseaseUsed(FeignSimMessProdReq messProdReq){
        String key = "cacheIsTheDiseaseUsed" + messProdReq.getPersonDisease() + "," + messProdReq.getId().toString();
        Integer value = redisTemplate.opsForValue().get(key);
        if(value != null){
            return value;
        }

        ChronicDisease chronicDisease = this.sysChronicDiseaseRepository.findById(Long.valueOf(messProdReq.getPersonDisease())).orElse(null);
        if(chronicDisease == null || StringUtils.isBlank(chronicDisease.getCode())){
            redisTemplate.opsForValue().set(key, 0);
            return 0;
        }

        if(StringUtils.isNotBlank(chronicDisease.getProhibitionMatterDishesKey()) && Arrays.stream(chronicDisease.getProhibitionMatterDishesKey().split("/")).anyMatch(item -> messProdReq.getName().contains(item))){
            redisTemplate.opsForValue().set(key, 2);
            return 2;
        }

        if(StringUtils.isNotBlank(chronicDisease.getProhibitionMatterDishesKey()) && Arrays.stream(chronicDisease.getMatterDishesKey().split("/")).anyMatch(item -> messProdReq.getName().contains(item))){
            redisTemplate.opsForValue().set(key, 1);
            return 1;
        }

        redisTemplate.opsForValue().set(key, 0);
        return 0;
    }

    public List<FeignChronicDiseaseDto> findByIds(Set<Long> ids){
        if(ids.isEmpty()){
            return Collections.emptyList();
        }
        return this.sysChronicDiseaseRepository.findAllById(ids).stream().map(item -> {
            FeignChronicDiseaseDto chronicDiseaseDto = new FeignChronicDiseaseDto()
                    .setId(item.getId())
                    .setName(item.getName());
            return chronicDiseaseDto;
        }).collect(Collectors.toList());
    }

}
