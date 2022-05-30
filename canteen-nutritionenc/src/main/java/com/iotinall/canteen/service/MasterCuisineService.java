package com.iotinall.canteen.service;


import com.iotinall.canteen.dto.disease.CuisineDTO;
import com.iotinall.canteen.entity.SysCuisine;
import com.iotinall.canteen.entity.SysMasterCuisine;
import com.iotinall.canteen.dto.nutritionenc.FeignCuisineDto;
import com.iotinall.canteen.dto.nutritionenc.FeignMessProductCuisineDto;
import com.iotinall.canteen.repository.SysCuisineRepository;
import com.iotinall.canteen.repository.SysMasterCuisineRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 食谱分类逻辑处理类
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
@Service
public class MasterCuisineService {
    @Resource
    private SysMasterCuisineRepository masterCuisineRepository;
    @Resource
    private SysCuisineRepository cuisineRepository;

    @Resource
    private FeignMessProductService feignMessProductService;

    /**
     * 获取食谱类型列表
     *
     * @author loki
     * @date 2020/12/10 14:22
     */
    @Cacheable(value = "CUISINE_LIST")
    public Object getCuisineList() {
        List<SysMasterCuisine> masterCuisineList = this.masterCuisineRepository.findAll(Sort.by(Sort.Direction.ASC, "seq"));
        if (CollectionUtils.isEmpty(masterCuisineList)) {
            return Collections.EMPTY_LIST;
        }

        List<CuisineDTO> masterCuisineDTOList = new ArrayList<>();
        CuisineDTO masterCuisineDTO;
        for (SysMasterCuisine masterCuisine : masterCuisineList) {
            masterCuisineDTO = new CuisineDTO();
            masterCuisineDTOList.add(masterCuisineDTO);
            masterCuisineDTO.setId(masterCuisine.getId());
            masterCuisineDTO.setCode(masterCuisine.getCode());
            masterCuisineDTO.setName(masterCuisine.getName());

            Set<SysCuisine> cuisineList = this.cuisineRepository.queryByMasterIdOrderBySeqAsc(masterCuisine.getId());
            if (!CollectionUtils.isEmpty(cuisineList)) {
                List<CuisineDTO> cuisineDTOList = new ArrayList<>();
                CuisineDTO cuisineDTO;
                for (SysCuisine cuisine : cuisineList) {
                    cuisineDTO = new CuisineDTO();
                    cuisineDTOList.add(cuisineDTO);
                    cuisineDTO.setId(cuisine.getId());
                    cuisineDTO.setCode(cuisine.getCode());
                    cuisineDTO.setName(cuisine.getName());
                }
                masterCuisineDTO.setCuisine(cuisineDTOList);
            }
        }
        return masterCuisineDTOList;
    }


    public List<FeignMessProductCuisineDto> getCuisineMessProductStat() {
        List<FeignMessProductCuisineDto> resp = new ArrayList<>();

        List<SysMasterCuisine> masterCuisineList = masterCuisineRepository.findAll();
        if (CollectionUtils.isEmpty(masterCuisineList)) {
            return null;
        }
        FeignMessProductCuisineDto master;
        for (SysMasterCuisine masterCuisine : masterCuisineList) {
            if (null == masterCuisine) {
                continue;
            }
            master = new FeignMessProductCuisineDto();
            master.setId(masterCuisine.getId());
            master.setName(masterCuisine.getName());
            master.setNum(0);

            Set<SysCuisine> childCuisines = masterCuisine.getCuisines();
            if (CollectionUtils.isEmpty(childCuisines)) {
                continue;
            }

            List<FeignMessProductCuisineDto> children = new ArrayList<>();
            FeignMessProductCuisineDto child;
            for (SysCuisine childCuisine : childCuisines) {
                if (null == childCuisine) {
                    continue;
                }
                child = new FeignMessProductCuisineDto();
                child.setId(childCuisine.getId());
                child.setName(childCuisine.getName());

                //统计这个类型下面菜谱数量
                Integer num = this.feignMessProductService.countCuisineMessProduct(childCuisine.getId());
                child.setNum(null == num ? 0 : num);
                children.add(child);
            }
            master.setChildren(children);

            resp.add(master);
        }
        return resp;
    }

    /**
     * 查询子分类
     * @param ids
     * @return
     */
    public Set<String> findAllChildrenId(Set<String> ids){
        Set<SysCuisine> sysCuisines = this.cuisineRepository.queryByMasterIdIn(ids);
        return sysCuisines.stream().map(SysCuisine::getId).collect(Collectors.toSet());
    }

    public List<FeignCuisineDto> findByIds(Set<String> ids){
        List<SysCuisine> sysCuisines = this.cuisineRepository.findAllById(ids);
        return sysCuisines.stream().map(item -> {
            FeignCuisineDto feignCuisineDto = new FeignCuisineDto()
                    .setId(item.getId())
                    .setName(item.getName());
            return feignCuisineDto;
        }).collect(Collectors.toList());
    }
}
