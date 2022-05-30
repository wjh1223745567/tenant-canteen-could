package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignFlavourDto;
import com.iotinall.canteen.repository.SysFlavourRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class SysFlavourService {

    @Resource
    private SysFlavourRepository flavourRepository;

    public FeignFlavourDto findDtoById(String id){
        return flavourRepository.findById(id).map(item -> {
            FeignFlavourDto feignFlavourDto = new FeignFlavourDto()
                    .setId(item.getId())
                    .setName(item.getName());
            return feignFlavourDto;
        }).orElse(null);
    }

}
