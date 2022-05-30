package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Sets;
import com.iotinall.canteen.annotations.FreshenMess;
import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.dto.messprod.*;
import com.iotinall.canteen.dto.nutritionenc.FeignChronicDiseaseDto;
import com.iotinall.canteen.dto.nutritionenc.FeignCraftDto;
import com.iotinall.canteen.dto.nutritionenc.FeignCuisineDto;
import com.iotinall.canteen.dto.nutritionenc.FeignFlavourDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.stock.FeignProcurementDto;
import com.iotinall.canteen.dto.stock.FeignStockProdDto;
import com.iotinall.canteen.entity.MessProduct;
import com.iotinall.canteen.entity.MessProductComment;
import com.iotinall.canteen.entity.MessProductCuisine;
import com.iotinall.canteen.repository.MessProductCommentRepository;
import com.iotinall.canteen.repository.MessProductCuisineRepository;
import com.iotinall.canteen.repository.MessProductRepository;
import com.iotinall.canteen.utils.MessProductUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜品 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMessProductService {
    @Resource
    private MessProductRepository messProductRepository;
    @Resource
    private MessProductCommentRepository messProductCommentRepository;
    @Resource
    private FeignFlavourService feignFlavourService;
    @Resource
    private FeignCraftService feignCraftService;
    @Resource
    private FeignCuisineService feignCuisineService;

    @Resource
    private FeignChronicDiseaseService feignChronicDiseaseService;

    @Resource
    private BaiduMenuService baiduMenuService;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    @Resource
    private FeignMaterialService feignMaterialService;

    @Resource
    private MessProductCuisineRepository messProductCuisineRepository;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    @Resource
    private FeignStockService feignStockService;

    public PageDTO<SysMessProductDTO> pageMessProduct(MessProductQueryCriteria criteria, Pageable pageable) {
        Set<Long> messProductIds = new HashSet<>();
        //分类(包括子分类)下面所有的菜品ID
        if (StringUtils.isNotBlank(criteria.getCuisineIds())) {
            Set<String> cuisineIds = Sets.newHashSet(criteria.getCuisineIds());

            Set<String> childrenIds = feignCuisineService.findAllChildrenId(cuisineIds);
            cuisineIds.addAll(childrenIds);

            messProductIds = messProductRepository.queryCuisineMessProduct(cuisineIds);
        }
        Specification<MessProduct> specification = SpecificationBuilder.builder()
                .where(
                        Criterion.like("name", criteria.getName()),
                        Criterion.eq("enabled", criteria.getEnabled()),
                        Criterion.like("useFor", MessProductUtil.buildCatalogLikeStr(criteria.getUseFor())),
                        Criterion.like("catalog", MessProductUtil.buildCatalogLikeStr(criteria.getMealType())),
                        Criterion.in("id", messProductIds)
                ).build();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<MessProduct> page = messProductRepository.findAll(specification, pageable);
        if (CollectionUtils.isEmpty(page.getContent())) {
            return PageUtil.empPage();
        }

        Page<SysMessProductDTO> dtoPage = page.map(item -> {
            SysMessProductDTO productDTO = new SysMessProductDTO();
            BeanUtils.copyProperties(item, productDTO);

            productDTO.setSuitableDisease(item.getSuitableDisease());
            productDTO.setContraindications(item.getContraindications());
            //刘俊编辑于2020-03-26
            //菜品类别
            if (!CollectionUtils.isEmpty(item.getCuisines())) {
                List<SysCuisineDTO> cuisineDTOS = new ArrayList<>();
                SysCuisineDTO cuisineDTO;
                for (MessProductCuisine c : item.getCuisines()) {
                    cuisineDTO = new SysCuisineDTO();
                    cuisineDTO.setId(c.getCuisineId());
                    cuisineDTO.setName(c.getCuisineName());
                    cuisineDTOS.add(cuisineDTO);
                }
                productDTO.setCuisines(cuisineDTOS);
            }

            productDTO.setCraftId(item.getCraftId());
            productDTO.setCraftName(item.getCraftId());
            productDTO.setFlavourId(item.getFlavourId());

            if(item.getFlavourId() != null){
                FeignFlavourDto feignFlavourDto = feignFlavourService.findDtoById(item.getFlavourId());
                productDTO.setFlavourName(null != feignFlavourDto ? feignFlavourDto.getName() : "");
            }

            //做法
            if (StringUtils.isNotBlank(item.getPractice())) {
                List<MessProductPracticeDTO> practices = JSON.parseObject(item.getPractice(), new TypeReference<List<MessProductPracticeDTO>>() {});
                productDTO.setPractices(practices);
            }

            //原材料
            if (StringUtils.isNotBlank(item.getMaterial())) {
                List<MessProductMaterialDTO> materials = JSON.parseObject(item.getMaterial(), new TypeReference<List<MessProductMaterialDTO>>() {});
                productDTO.setMaterials(materials);
            }

            //适合疾病
            if (!StringUtils.isBlank(item.getSuitableDisease())) {
                Set<Long> diseaseIdList = Arrays.stream(item.getSuitableDisease().split(";")).map(Long::valueOf).collect(Collectors.toSet());
                List<FeignChronicDiseaseDto> diseaseList = feignChronicDiseaseService.findByIds(diseaseIdList);
                productDTO.setSuitableDiseaseName(diseaseList.stream().map(FeignChronicDiseaseDto::getName).collect(Collectors.joining(",")));
            }

            //禁忌疾病
            if (!StringUtils.isBlank(item.getContraindications())) {
                Set<Long> diseaseIdList = Arrays.stream(item.getContraindications().split(";")).map(Long::valueOf).collect(Collectors.toSet());
                List<FeignChronicDiseaseDto> diseaseList = feignChronicDiseaseService.findByIds(diseaseIdList);
                productDTO.setContraindicationsName(diseaseList.stream().map(FeignChronicDiseaseDto::getName).collect(Collectors.joining(",")));
            }

            return productDTO;
        });

        return PageUtil.toPageDTO(dtoPage);
    }

    public SysMessProductDetailDTO detail(Long id) {
        MessProduct messProduct = messProductRepository.findById(id).orElse(null);
        if (null == messProduct) {
            throw new BizException("", "记录不存在");
        }
        SysMessProductDetailDTO detailDTO = new SysMessProductDetailDTO();
        BeanUtils.copyProperties(messProduct, detailDTO);

        detailDTO.setCreateTime(messProduct.getCreateTime());
        List<Map<String, Number>> list = messProductCommentRepository.statScoreCounts(id);
        if (!CollectionUtils.isEmpty(list)) {
            Map<Integer, Integer> starMap = new LinkedHashMap<>(list.size());
            list.forEach(item -> {
                starMap.put(item.get("score").intValue(), item.get("count").intValue());
            });
            detailDTO.setStarCounts(starMap);
        } else {
            detailDTO.setStarCounts(Collections.emptyMap());
        }

        if (null != messProduct.getFlavourId()) {
            FeignFlavourDto feignFlavourDto = feignFlavourService.findDtoById(messProduct.getFlavourId());
            if(feignFlavourDto != null){
                detailDTO.setFlavourId(feignFlavourDto.getId());
                detailDTO.setFlavourName(feignFlavourDto.getName());
            }
        }

        if (null != messProduct.getCraftId()) {
            FeignCraftDto feignCraftDto = feignCraftService.findDtoById(messProduct.getCraftId());
            if(feignCraftDto != null){
                detailDTO.setCraftId(feignCraftDto.getId());
                detailDTO.setCraftName(feignCraftDto.getName());
            }
        }

        //菜品类别
        if (!CollectionUtils.isEmpty(messProduct.getCuisines())) {
            List<SysCuisineDTO> cuisineDTOS = new ArrayList<>();
            SysCuisineDTO cuisineDTO;
            for (MessProductCuisine c : messProduct.getCuisines()) {
                cuisineDTO = new SysCuisineDTO();
                cuisineDTO.setId(c.getCuisineId());
                cuisineDTO.setName(c.getCuisineName());
                cuisineDTOS.add(cuisineDTO);
            }
            detailDTO.setCuisines(cuisineDTOS);
        }

        //做法
        if (StringUtils.isNotBlank(messProduct.getPractice())) {
            List<MessProductPracticeDTO> practices = JSON.parseObject(messProduct.getPractice(), new TypeReference<List<MessProductPracticeDTO>>() {
            });
            detailDTO.setPractices(practices);
        }

        //原材料
        if (StringUtils.isNotBlank(messProduct.getMaterial())) {
            List<MessProductMaterialDTO> materials = JSON.parseObject(messProduct.getMaterial(), new TypeReference<List<MessProductMaterialDTO>>() {
            });
            detailDTO.setMaterials(materials);
        }

        //适合疾病
        if (!StringUtils.isBlank(messProduct.getSuitableDisease())) {
            Set<Long> diseaseIdList = Arrays.stream(messProduct.getSuitableDisease().split(";")).map(Long::valueOf).collect(Collectors.toSet());
            List<FeignChronicDiseaseDto> diseaseList = feignChronicDiseaseService.findByIds(diseaseIdList);
            detailDTO.setSuitableDiseaseName(diseaseList.stream().map(FeignChronicDiseaseDto::getName).collect(Collectors.joining(",")));
        }

        //禁忌疾病
        if (!StringUtils.isBlank(messProduct.getContraindications())) {
            Set<Long> diseaseIdList = Arrays.stream(messProduct.getContraindications().split(";")).map(Long::valueOf).collect(Collectors.toSet());
            List<FeignChronicDiseaseDto> diseaseList = feignChronicDiseaseService.findByIds(diseaseIdList);
            detailDTO.setContraindicationsName(diseaseList.stream().map(FeignChronicDiseaseDto::getName).collect(Collectors.joining(",")));
        }

        return detailDTO;
    }

    public CursorPageDTO<SysMessProductCommentDTO> pageComments(Long productId, Long cursor, String tags) {
        Specification<MessProductComment> spec = SpecificationBuilder.builder()
                .where(Criterion.lt("id", cursor))
                .where(Criterion.eq("productId", productId))
                .where(Criterion.like("tags", tags))
                .build();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize, Sort.Direction.DESC, "id");
        Page<MessProductComment> commentPage = messProductCommentRepository.findAll(spec, pageable);
        Map<Long, FeignEmployeeDTO> empMap = feignEmployeeService.findByIds(commentPage.getContent().stream().map(MessProductComment::getEmployeeId).collect(Collectors.toSet()));
        List<SysMessProductCommentDTO> list = commentPage.getContent().stream().map(item -> {
            SysMessProductCommentDTO comment = new SysMessProductCommentDTO();
            BeanUtils.copyProperties(item, comment);
            comment.setCreateTime(item.getCreateTime());
            if (!item.getAnonymous()) { // 不是匿名
                if (item.getEmployeeId() != null && empMap.containsKey(item.getEmployeeId())) {
                    FeignEmployeeDTO feignEmployeeDTO = empMap.get(item.getEmployeeId());
                    comment.setEmpId(feignEmployeeDTO.getId());
                    comment.setEmpName(feignEmployeeDTO.getName());
                    comment.setEmpAvatar(feignEmployeeDTO.getAvatar());
                }
            }
            return comment;
        }).collect(Collectors.toList());
        return PageUtil.toCursorPageDTO(list, list.size() < pageSize ? 0 : list.get(list.size() - 1).getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public Object add(SysMessProductAddReq messProductAddReq) {
        MessProduct messProduct = new MessProduct();
        if (messProductRepository.findFirstByName(messProductAddReq.getName()) != null) {
            throw new BizException("", "已存在该名称的菜品");
        }

        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(messProductAddReq, messProduct);
        LocalDateTime now = LocalDateTime.now();
        messProduct.setRecommendedCount(0);
        messProduct.setSoldCount(0);
        messProduct.setAvgScore(BigDecimal.ZERO);
        messProduct.setFavorRate(BigDecimal.ZERO);
        messProduct.setCreateTime(now);
        messProduct.setUpdateTime(now);
        messProduct.setDeleted(Boolean.FALSE);
        messProduct.setSuitableDisease(messProductAddReq.getSuitableDisease());
        messProduct.setContraindications(messProductAddReq.getContraindications());
        messProduct.setCraftId(messProductAddReq.getCraftId());
        //口味
        messProduct.setFlavourId(messProductAddReq.getFlavourId());

        //计算营养元素
        NutritionNone nutrition = this.feignMaterialService.generateNutrition(messProductAddReq.getMaterial());
        if (null != nutrition) {
            BeanUtils.copyProperties(nutrition, messProduct);
        }
        messProduct = this.messProductRepository.save(messProduct);

        //类别
        if (StringUtils.isNotBlank(messProductAddReq.getCuisineIds())) {
            List<FeignCuisineDto> cuisines = this.feignCuisineService.findByIds(new HashSet<>(Arrays.asList(messProductAddReq.getCuisineIds().split(","))));
            Set<MessProductCuisine> productCuisines = new HashSet<>(cuisines.size());
            for (FeignCuisineDto cuisine : cuisines) {
                MessProductCuisine messProductCuisine = new MessProductCuisine()
                        .setProduct(messProduct)
                        .setCuisineId(cuisine.getId())
                        .setCuisineName(cuisine.getName());
                productCuisines.add(messProductCuisine);
                this.messProductCuisineRepository.save(messProductCuisine);
            }
            messProduct.setCuisines(productCuisines);
        }

        //添加到百度菜谱
        this.baiduMenuService.addMenu(messProduct);

        return messProduct;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object update(SysMessProductEditReq messProductEditReq) {
        Optional<MessProduct> optional = messProductRepository.findById(messProductEditReq.getId());
        if (!optional.isPresent()) {
            throw new BizException("", "记录不存在");
        }
        MessProduct messProduct = optional.get();

        this.baiduMenuService.deletedMenu(messProduct);

        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(messProductEditReq, messProduct);
        LocalDateTime now = LocalDateTime.now();
        messProduct.setUpdateTime(now);
        messProduct.setSuitableDisease(messProductEditReq.getSuitableDisease());
        messProduct.setContraindications(messProductEditReq.getContraindications());

        messProduct.setCraftId(messProductEditReq.getCraftId());
        //口味
        messProduct.setFlavourId(messProductEditReq.getFlavourId());

        //计算营养元素
        NutritionNone nutrition = this.feignMaterialService.generateNutrition(messProductEditReq.getMaterial());
        if (null != nutrition) {
            BeanUtils.copyProperties(nutrition, messProduct);
        }

        messProduct = messProductRepository.save(messProduct);

        //类别
        if (StringUtils.isNotBlank(messProductEditReq.getCuisineIds())) {
            List<FeignCuisineDto> cuisines = this.feignCuisineService.findByIds(new HashSet<>(Arrays.asList(messProductEditReq.getCuisineIds().split(","))));
            Set<MessProductCuisine> productCuisines = new HashSet<>(cuisines.size());
            for (FeignCuisineDto cuisine : cuisines) {
                MessProductCuisine messProductCuisine = new MessProductCuisine()
                        .setProduct(messProduct)
                        .setCuisineId(cuisine.getId())
                        .setCuisineName(cuisine.getName());
                productCuisines.add(messProductCuisine);
            }
            this.messProductCuisineRepository.saveAll(productCuisines);
            messProduct.getCuisines().clear();
            messProduct.getCuisines().addAll(productCuisines);
        }

        this.baiduMenuService.addMenu(messProduct);

        return messProduct;
    }

    /**
     * 推送所有菜谱到百度菜谱
     */
    public void pushAllToBaidu() {
        List<MessProduct> messProducts = this.messProductRepository.findAll();
        for (MessProduct messProduct : messProducts) {
            this.baiduMenuService.addMenu(messProduct);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Object delete(Long id) {
        Optional<MessProduct> optional = messProductRepository.findById(id);
        if (optional.isPresent()) {
            this.baiduMenuService.deletedMenu(optional.get());
            messProductRepository.deleteById(id);
        }
        return optional.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择删除的菜品");
        }

        List<MessProduct> messProductList = new ArrayList<>();
        MessProduct messProduct;
        for (Long id : ids) {
            messProduct = this.messProductRepository.findById(id).orElseThrow(() -> new BizException("", "菜品不存在"));
            messProductList.add(messProduct);
        }

        for (MessProduct product : messProductList) {
            this.baiduMenuService.deletedMenu(product);
        }

        this.messProductRepository.deleteAll(messProductList);

    }

    @Transactional(rollbackFor = Exception.class)
    public Object toggle(MessProductToggleReq req) {
        MessProduct messProduct = this.messProductRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "菜品不存在"));
        messProductRepository.toggleProduct(req.getId(), req.getEnabled(), LocalDateTime.now());
        return messProduct;
    }

    public List<MessProductTagCommentDTO> countProductComments(Long productId) {
        List<String> tags = messProductCommentRepository.findAllCommentTag(productId);
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }
        Map<String, Integer> map = new HashMap<>();
        tags.forEach(item -> {
            Arrays.stream(item.split(",")).forEach(tag -> {
                if (map.containsKey(tag)) {
                    map.put(tag, map.get(tag) + 1);
                } else {
                    map.put(tag, 1);
                }
            });
        });
        List<MessProductTagCommentDTO> collect = map.entrySet().stream().map(item -> {
            MessProductTagCommentDTO dto = new MessProductTagCommentDTO();
            dto.setLabel(item.getKey());
            dto.setCount(item.getValue());
            return dto;
        }).collect(Collectors.toList());
        return collect;
    }

    public NutritionNone findById(Long id){
        return this.messProductRepository.findById(id).map(item -> {
            NutritionNone nutrition = new NutritionNone();
            BeanUtils.copyProperties(item, nutrition);
            return nutrition;
        }).orElse(null);
    }

    public FeignMessProdDto findDtoById(Long id){
        return this.messProductRepository.findById(id).map(item -> {
            FeignMessProdDto messProdDto = new FeignMessProdDto()
                    .setId(item.getId())
                    .setImg(item.getImg())
                    .setName(item.getName());
            messProdDto.setEnergy(item.getEnergy());
            return messProdDto;
        }).orElse(null);
    }

    public Integer countCuisineMessProduct(String cuisineId){
        return this.messProductRepository.countCuisineMessProduct(cuisineId);
    }

    /**
     * 获取餐厅使用库存的数据源
     * @return
     */
    public List<SimpDataSource> simpDataSources(){
        return feignTenantOrganizationService.findStockDataSource();
    }

    public List<FeignStockProdDto> findTop10Material(String key, Long typeId){
        return feignStockService.findTop10Material(key, typeId);
    }

    /**
     * 获取菜谱商品
     * @param id
     * @return
     */
    public FeignProcurementDto.MenuProd traceabilityChain(Long id){
        Optional<MessProduct> optionalMessProduct = this.messProductRepository.findById(id);
        if(optionalMessProduct.isPresent()){
            MessProduct messProduct = optionalMessProduct.get();
            FeignProcurementDto.MenuProd menuProd = new FeignProcurementDto.MenuProd()
                    .setId(messProduct.getId())
                    .setName(messProduct.getName())
                    .setRawMaterial(messProduct.getMaterial());
            return menuProd;
        }else{
            return null;
        }
    }
}