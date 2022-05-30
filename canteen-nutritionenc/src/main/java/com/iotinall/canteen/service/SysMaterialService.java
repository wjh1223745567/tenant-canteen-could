package com.iotinall.canteen.service;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.repository.SysMaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysMaterialService {

    @Resource
    private SysMaterialRepository sysMaterialRepository;

    public List<NutritionNone> findByIds(List<String> ids){
        return this.sysMaterialRepository.findAllById(ids).stream().map(item -> (NutritionNone)item).collect(Collectors.toList());
    }
}
