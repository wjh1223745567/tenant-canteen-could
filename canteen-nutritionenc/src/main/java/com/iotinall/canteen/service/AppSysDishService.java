package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.dish.*;
import com.iotinall.canteen.dto.nutritionenc.FeignSimMessProdReq;
import com.iotinall.canteen.dto.dish.SysCuisineDTO;
import com.iotinall.canteen.dto.dish.SysDishRandomImageResp;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单相关 ServiceImpl
 *
 * @author loki
 * @date 2020/03/25 14:52
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "app_dish")
public class AppSysDishService {

    @Resource
    SysDishRepository dishRepository;

    @Resource
    SysCuisineRepository cuisineRepository;

    @Resource
    SysDishMaterialRepository materialRepository;

    @Resource
    private SysMaterialRepository sysMaterialRepository;

    @Resource
    private FeignNutritionPersonRecordService personRecordService;

    @Resource
    private SysChronicDiseaseRepository sysChronicDiseaseRepository;


    public Object findTop10Dish(String name) {
        return dishRepository.queryTop10DishByName(name);
    }

    public Object findNutritionConditionDish(String name, String code, Pageable pageable) {
        SysCuisine cuisine = cuisineRepository.queryByCode(code);
        if (null == cuisine) {
            throw new BizException("", "该类别不存在");
        }
        Page<SysDish> dishes = dishRepository.queryNutritionConditionDishPage(name, cuisine.getId(), pageable);
        if (CollectionUtils.isEmpty(dishes.getContent())) {
            PageUtil.toPageDTO(null, dishes);
        }

        List<DishSimpleDTO> dishSimpleDTOS = dishes.stream().filter(Objects::nonNull).map(item ->
                new DishSimpleDTO()
                        .setImg(item.getImg())
                        .setId(item.getId())
                        .setName(item.getName()))
        .collect(Collectors.toList());
        return PageUtil.toPageDTO(dishSimpleDTOS, dishes);
    }

    public Object findNutritionConditionDish(String name, Pageable pageable) {
        Specification<SysDish> sysDishSpecification = SpecificationBuilder.builder()
                .where(Criterion.like("name", name)).build();
        Page<SysDish> dishes = dishRepository.findAll(sysDishSpecification, pageable);
        if (CollectionUtils.isEmpty(dishes.getContent())) {
            PageUtil.toPageDTO(null, dishes);
        }

        List<DishListDTO> dishSimpleDTOS = dishes.stream().map(item -> new DishListDTO()
                .setImg(item.getImg())
                .setId(item.getId())
                .setName(item.getName())
                .setCraftName(item.getCraft() != null ? item.getCraft().getName() : null)
                .setFlavourName(item.getFlavour() != null ? item.getFlavour().getName() : null)
        ).collect(Collectors.toList());
        return PageUtil.toPageDTO(dishSimpleDTOS, dishes);
    }

    public Object findNutritionConditionDishDetail(String id) {
        SysDish dish = this.dishRepository.findById(id).orElse(null);
        if (null == dish) {
            throw new BizException("", "菜谱不存在");
        }

        DishDetailDTO detailDTO = new DishDetailDTO();
        BeanUtils.copyProperties(dish, detailDTO);
        //菜品类别
        if (!CollectionUtils.isEmpty(dish.getCuisines())) {
            List<SysCuisineDTO> cuisineDTOS = new ArrayList<>();
            SysCuisineDTO cuisineDTO;
            for (SysCuisine c : dish.getCuisines()) {
                cuisineDTO = new SysCuisineDTO();
                cuisineDTO.setId(c.getId());
                cuisineDTO.setName(c.getName());
                cuisineDTOS.add(cuisineDTO);
            }
            detailDTO.setCuisines(cuisineDTOS.stream().map(SysCuisineDTO::getName).collect(Collectors.joining(" ,")));
        }

        detailDTO.setCraftName(null != dish.getCraft() ? dish.getCraft().getName() : "");
        detailDTO.setFlavourName(null != dish.getFlavour() ? dish.getFlavour().getName() : "");

        //原材料
        List<SysDishMaterial> dishMaterials = materialRepository.queryByDish(dish);
        if (!CollectionUtils.isEmpty(dishMaterials)) {
            detailDTO.setMaterials(dishMaterials.stream().map(item -> new SysMaterialSimDTO().setId(item.getId()).setName(item.getMaterial().getName()).setAmount(item.getAmount()).setMaster(item.getMaster())).collect(Collectors.toList()));
        }
        return detailDTO;
    }

    /**
     * 营养档案，轮播图
     *
     * @author loki
     * @date 2020/12/08 17:45
     */
    public List<SysDishRandomImageResp> findRandomImages() {
        //2020-12-08修改 用户选择了慢性病，则根据慢性病推荐，否则随机推荐
        /**
         * 查询当前用户疾病
         */
        String disease = this.personRecordService.findDisease();

        List<SysDish> dishes;
        if (StringUtils.isNotBlank(disease)) {
            Set<Long> personDisease = Arrays.stream(disease.split(";")).map(Long::valueOf).collect(Collectors.toSet());
            List<ChronicDisease> chronicDiseasesList = this.sysChronicDiseaseRepository.findAllById(personDisease);
            Set<SysCuisine> cuisineSet = new HashSet<>();
            for (ChronicDisease chronicDisease : chronicDiseasesList) {
                cuisineSet.addAll(chronicDisease.getCuisine());
            }
            if (CollectionUtils.isEmpty(cuisineSet)) {
                return Collections.emptyList();
            }

            dishes = this.dishRepository.queryByCuisinesIn(cuisineSet);
            if (CollectionUtils.isEmpty(dishes)) {
                dishes = this.dishRepository.findAll(PageRequest.of(1, 200)).getContent();
            }
        } else {
            dishes = this.dishRepository.findAll(PageRequest.of(1, 200)).getContent();
        }

        if (CollectionUtils.isEmpty(dishes)) {
            return Collections.emptyList();
        }

        List<SysDishRandomImageResp> resps = new ArrayList<>(5);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            SysDish sysDish = dishes.get(random.nextInt(dishes.size()));
            SysDishRandomImageResp resp = new SysDishRandomImageResp()
                    .setId(sysDish.getId())
                    .setName(sysDish.getName())
                    .setUrl(sysDish.getImg());
            resps.add(resp);
        }

        return resps;
    }

    /**
     * 检索名字
     *
     * @param name
     * @return
     */
    public List<String> productNames(String name) {
        Specification<SysDish> sysDishSpecification = SpecificationBuilder.builder()
                .where(Criterion.like("name", name)).build();
        List<SysDish> sysDishes = this.dishRepository.findAll(sysDishSpecification, PageRequest.of(1, 100)).getContent();
        return sysDishes.stream().map(SysDish::getName).collect(Collectors.toList());
    }

    public Object findDishListByNutritionRank(String type, String name, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, type));
        Specification<SysDish> spec = SpecificationBuilder.builder()
                .where(Criterion.like("name", name))
                .build();
        Page<SysDish> pageResult = this.dishRepository.findAll(spec, pageRequest);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.emptyList(), pageResult);
        }

        List<DishSimpleDTO> dishs = pageResult.getContent().stream().map(item -> {
            DishSimpleDTO simpleDTO = new DishSimpleDTO();

            BeanUtils.copyProperties(item, simpleDTO);
            return simpleDTO.setId(item.getId())
                    .setName(item.getName())
                    .setImg(item.getImg());
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(dishs, pageResult);
    }

    public Object getMaterialByNutritionRank(String type, String name, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, type));
        Specification<SysMaterial> spec = SpecificationBuilder.builder()
                .where(Criterion.like("name", name))
                .build();
        Page<SysMaterial> pageResult = this.sysMaterialRepository.findAll(spec, pageRequest);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.emptyList(), pageResult);
        }

        List<DishSimpleDTO> dishs = pageResult.getContent().stream().map(item -> {
            DishSimpleDTO simpleDTO = new DishSimpleDTO();

            BeanUtils.copyProperties(item, simpleDTO);
            return simpleDTO.setId(item.getId())
                    .setName(item.getName())
                    .setImg(item.getImg());
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(dishs, pageResult);
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public SysMaterialView getMaterialView(String id) {
        Optional<SysMaterial> optionalSysMaterial = this.sysMaterialRepository.findById(id);
        if (!optionalSysMaterial.isPresent()) {
            throw new BizException("", "未找到原材料信息");
        }

        SysMaterial material = optionalSysMaterial.get();
        SysMaterialView materialView = new SysMaterialView();
        BeanUtils.copyProperties(material, materialView);
        return materialView;
    }

}