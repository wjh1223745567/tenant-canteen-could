package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.dto.menubrief.*;
import com.iotinall.canteen.dto.mess.MessCookListDTO;
import com.iotinall.canteen.dto.messcookdetail.MessCookDetailDTO;
import com.iotinall.canteen.dto.messprod.*;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.nutritionenc.FeignCraftDto;
import com.iotinall.canteen.dto.nutritionenc.FeignFlavourDto;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.MessProductUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/2315:01
 */
@Service
public class AppMessProductService {

    @Resource
    private MessProductRepository messProductRepository;
    @Resource
    private MessProductCommentRepository messProductCommentRepository;

    @Resource
    private FeignEmployeeService feignEmployeeService;


    @Resource
    private BaiduMenuService baiduMenuService;

    @Resource
    private FeignFlavourService feignFlavourService;

    @Resource
    private FeignCraftService feignCraftService;

    @Resource
    private FinConsumeSettingRepository finConsumeSettingRepository;

    @Resource
    private MessDailyMenuRepository messDailyMenuRepository;

    public Object page(String keyword, Integer catalog, Pageable pageable) {
        Set<String> catalogList = new HashSet<>();

        if (catalog != null) {
            if (catalog == 1) {
                catalogList.add("0001");
                catalogList.add("0011");
                catalogList.add("0111");
                catalogList.add("0101");
            } else if (catalog == 2) {
                catalogList.add("0010");
                catalogList.add("0011");
                catalogList.add("0111");
                catalogList.add("0110");
            } else {
                catalogList.add("0100");
                catalogList.add("0101");
                catalogList.add("0110");
                catalogList.add("0111");
            }
        }

        SpecificationBuilder spec = SpecificationBuilder.builder()
                .where(Criterion.eq("name", keyword))
                .where(Criterion.in("catalog", catalogList));

        Page<MessProduct> result = this.messProductRepository.findAll(spec.build(), pageable);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<MessProductDto> messProductDtoList = new ArrayList<>();
        MessProductDto messProductDto;
        for (MessProduct product : result.getContent()) {
            messProductDto = new MessProductDto();
            messProductDtoList.add(messProductDto);
            messProductDto.setId(product.getId());
            messProductDto.setName(product.getName());
            messProductDto.setImage(product.getImg());
        }

        return PageUtil.toPageDTO(messProductDtoList, result);
    }



    public MessProductSportDto findSpotProd(Long id) {
        MessProduct messProduct = this.messProductRepository.findById(id).orElseThrow(() -> new BizException("", "未找到菜品信息"));

        MessProductSportDto sportDto = new MessProductSportDto()
                .setId(messProduct.getId())
                .setCarbohydrate(messProduct.getCarbohydrate())
                .setEnergy(messProduct.getEnergy())
                .setFat(messProduct.getFat())
                .setImg(messProduct.getImg())
                .setName(messProduct.getName())
                .setProtein(messProduct.getProtein());
        return sportDto;
    }

    public CursorPageDTO<MessCookListDTO> pageCooks(String name, Long cursor) {
        String useForLikeStr = MessProductUtil.buildCatalogLikeStr(2);
        Pageable pq = PageRequest.of(0, 10);
        Page<MessCookListDTO> page;
        if (!StringUtils.isBlank(name)) {
            page = messProductRepository.pageByNameLikeAndUseForLike("%" + name + "%", useForLikeStr, cursor, pq);
        } else {
            page = messProductRepository.pageByUseForLike(useForLikeStr, cursor, pq);
        }
        cursor = page.getContent().size() > 0 ? page.getContent().get(page.getContent().size() - 1).getId() : -1;
        return PageUtil.toCursorPageDTO(page.getContent(), cursor);
    }

    public MessCookDetailDTO getCookDetails(Long id) {
        MessProduct product = messProductRepository.findById(id).orElse(null);
        if (product == null) {
            throw new BizException("", "查看的菜品不存在");
        }
        MessCookDetailDTO detail = new MessCookDetailDTO();
        detail.setId(product.getId());
        detail.setName(product.getName());
        detail.setImg(product.getImg());
        detail.setNutrition(product.getIntro());
        detail.setPracticeTips(product.getPracticeTips());

        if (StringUtils.isNotBlank(product.getMaterial())) {
            List<MessCookDetailDTO.MessCookMaterialDTO> materials = JSON.parseObject(product.getMaterial(), new TypeReference<List<MessCookDetailDTO.MessCookMaterialDTO>>() {
            });
            detail.setMaterials(materials);
        }

        if (StringUtils.isNotBlank(product.getPractice())) {
            List<MessCookDetailDTO.MessCookStepDTO> practices = JSON.parseObject(product.getPractice(), new TypeReference<List<MessCookDetailDTO.MessCookStepDTO>>() {
            });
            detail.setSteps(practices);
        }

        return detail;
    }

    public MessProductDetailDTO detail(Long id) {
        MessProduct messProduct = messProductRepository.findById(id).orElse(null);
        if (null == messProduct) {
            throw new BizException("", "记录不存在");
        }
        MessProductDetailDTO detailDTO = new MessProductDetailDTO();
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

        if (StringUtils.isNotBlank(messProduct.getFlavourId())) {
            FeignFlavourDto feignFlavourDto = feignFlavourService.findDtoById(messProduct.getFlavourId());

            detailDTO.setFlavourId(feignFlavourDto.getId());
            detailDTO.setFlavourName(feignFlavourDto.getName());
        }

        if (StringUtils.isNotBlank(messProduct.getCraftId())) {
            FeignCraftDto feignCraftDto = feignCraftService.findDtoById(messProduct.getCraftId());
            detailDTO.setCraftId(feignCraftDto.getId());
            detailDTO.setCraftName(feignCraftDto.getName());
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
            List<SysMaterialDTO> materials = JSON.parseObject(messProduct.getMaterial(), new TypeReference<List<SysMaterialDTO>>() {
            });
            detailDTO.setMaterials(materials);
        }

        return detailDTO;
    }


    /**
     * 获取餐次类型
     *
     * @author loki
     * @date 2021/01/18 11:57
     */
    public Integer getMealType() {
        List<FinConsumeSetting> mealTimeList = finConsumeSettingRepository.findAll();
        if (CollectionUtils.isEmpty(mealTimeList)) {
            return MealTypeEnum.DINNER.getCode();
        } else {
            LocalDateTime now = LocalDateTime.now();
            Optional<FinConsumeSetting> lunch = mealTimeList.stream().filter(item -> item.getMealType().equals(MealTypeEnum.LUNCH)).findFirst();
            Optional<FinConsumeSetting> diner = mealTimeList.stream().filter(item -> item.getMealType().equals(MealTypeEnum.DINNER)).findFirst();

            /**
             * 1、午餐开始前算早餐
             * 2、晚餐开始前算午餐
             */
            String[] lunchTime = lunch.get().getBeginTime().split(":");
            LocalDateTime lunchBegin = now.withHour(Integer.parseInt(lunchTime[0])).withMinute(Integer.parseInt(lunchTime[1])).withSecond(0);

            String[] dinerTime = diner.get().getBeginTime().split(":");
            LocalDateTime dinerBegin = now.withHour(Integer.parseInt(dinerTime[0])).withMinute(Integer.parseInt(dinerTime[1])).withSecond(0);
            if (lunchBegin.compareTo(now) > 0) {
                return MealTypeEnum.BREAKFAST.getCode();
            } else if (dinerBegin.compareTo(now) >= 0) {
                return MealTypeEnum.LUNCH.getCode();
            } else {
                return MealTypeEnum.DINNER.getCode();
            }
        }
    }


    /**
     * 拍照搜索
     *
     * @param con
     * @return
     */
    public List<MessProductSimpleDTO> searchByPic(ProductPicSearchCon con) {
        LocalDate date = con.getDate() != null ? con.getDate() : LocalDate.now();

        MessDailyMenu dailyMenu = messDailyMenuRepository.findFirstByMenuDate(date);
        if (null == dailyMenu) {
            return Collections.emptyList();
        }

        Set<Long> productIds = null;
        if(con.getMenuType() != null){
            productIds = messDailyMenuRepository.queryMessDailyMenuItem(dailyMenu.getId(), con.getMenuType());
        }else{
            productIds = messDailyMenuRepository.queryDailyMenuItem(dailyMenu.getId());
        }

        if (CollectionUtils.isEmpty(productIds)) {
            return Collections.emptyList();
        }

        SearchDto searchDto = baiduMenuService.searchSelfMenu(con.getBase64());
        List<Dish> dishes = searchDto.getResult();
        List<DishProductDto> dishProductDtos = dishes.stream().map(item -> item.getDishes().isEmpty() ? null : item.getDishes().get(0)).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> menuBriefDtos = dishProductDtos.stream().map(item -> JSON.parseObject(item.getBrief(), MenuBriefDto.class)).map(MenuBriefDto::getId).collect(Collectors.toList());

        if(menuBriefDtos.isEmpty()){
            return Collections.emptyList();
        }

        Set<Long> haveIds = productIds.stream().filter(menuBriefDtos::contains).collect(Collectors.toSet());

        List<MessProduct> messProducts = messProductRepository.queryByIdIn(haveIds);
        return messProducts.stream().map(item -> new MessProductSimpleDTO()
                .setId(item.getId())
                .setName(item.getName())
                .setImg(item.getImg())
                .setEnergy(item.getEnergy())
        ).collect(Collectors.toList());
    }

    public AllSearchDto searchMenu(String str){
        return this.baiduMenuService.searchMenu(str);
    }
}
