package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignCraftDto;
import com.iotinall.canteen.repository.SysCraftRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class SysCraftService {

    @Resource
    private SysCraftRepository craftRepository;

    public FeignCraftDto findDtoById(String id){
        return this.craftRepository.findById(id).map(item -> {
            FeignCraftDto craftDto = new FeignCraftDto()
                    .setId(item.getId())
                    .setName(item.getName());
            return craftDto;
        }).orElse(null);
    }

}
