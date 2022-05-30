package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.dto.mess.CommentsDTO;
import com.iotinall.canteen.dto.messdaily.MessDailyMenuItemListDTO;
import com.iotinall.canteen.dto.messdailymenu.*;
import com.iotinall.canteen.dto.messprod.MessProductMaterialDTO;
import com.iotinall.canteen.dto.organization.FeignCookDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.recommend.MessProductRecommendDTO;
import com.iotinall.canteen.dto.stock.FeignProcurementDto;
import com.iotinall.canteen.dto.stock.MasterMaterialDTO;
import com.iotinall.canteen.entity.FinConsumeSetting;
import com.iotinall.canteen.entity.MessDailyMenu;
import com.iotinall.canteen.entity.MessDailyMenuItem;
import com.iotinall.canteen.entity.MessProduct;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.LocalDateUtil;
import com.iotinall.canteen.utils.MessProductUtil;
import com.iotinall.canteen.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.persistence.criteria.JoinType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * mess_daily_menu ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMessDailyMenuService {
    @Resource
    private MessDailyMenuRepository messDailyMenuRepository;
    @Resource
    private MessDailyMenuItemRepository messDailyMenuItemRepository;
    @Resource
    private MessProductRepository messProductRepository;
    @Resource
    private MessProductCommentRepository messProductCommentRepository;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private FinConsumeSettingRepository finConsumeSettingRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignStockService feignStockService;

    public MessDailyMenuDTO getDailyMenu(LocalDate date) {
        MessDailyMenu messDailyMenu = messDailyMenuRepository.findByMenuDate(date);
        MessDailyMenuDTO result = new MessDailyMenuDTO();
        result.setMenuDate(date);
        List<MessDailyMenuDTO.MenuProd> breakfast;
        List<MessDailyMenuDTO.MenuProd> lunch;
        List<MessDailyMenuDTO.MenuProd> dinner;
        if (messDailyMenu != null) {
            breakfast = new ArrayList<>();
            lunch = new ArrayList<>();
            dinner = new ArrayList<>();
            for (MessDailyMenuItem item : messDailyMenu.getMenuItems()) {
                Optional<MessProduct> messProduct = this.messProductRepository.findById(item.getProduct().getId());
                if (!messProduct.isPresent() || !messProduct.get().getEnabled()) {
                    continue;
                }

                MessDailyMenuDTO.MenuProd prod = new MessDailyMenuDTO.MenuProd();
                prod.setId(item.getProduct().getId());
                prod.setItemId(item.getId());
                if (item.getCookId() != null) {
                    FeignEmployeeDTO feignEmployeeDTO = feignEmployeeService.findById(item.getCookId());
                    prod.setCookId(feignEmployeeDTO != null ? feignEmployeeDTO.getId() : null);
                    prod.setCookName(feignEmployeeDTO != null ? feignEmployeeDTO.getName() : null);
                    prod.setCookImg(feignEmployeeDTO != null ? feignEmployeeDTO.getAvatar() : null);
                }

                prod.setImg(item.getProduct().getImg());
                prod.setName(item.getProduct().getName());
                prod.setDishClass(item.getProduct().getDishClass());
                Long socre = messProductCommentRepository.messProductAvgStar(item.getProduct().getId());
                prod.setScore(null == socre ? 0 : socre);
                if (item.getMealType() == MealTypeEnum.BREAKFAST) {
                    breakfast.add(prod);
                } else if (item.getMealType() == MealTypeEnum.LUNCH) {
                    lunch.add(prod);
                } else if (item.getMealType() == MealTypeEnum.DINNER) {
                    dinner.add(prod);
                }
            }
            result.setId(messDailyMenu.getId());
            result.setRemark(messDailyMenu.getRemark());
        } else {
            breakfast = Collections.emptyList();
            lunch = Collections.emptyList();
            dinner = Collections.emptyList();
        }
        result.setBreakfast(breakfast);
        result.setLunch(lunch);
        result.setDinner(dinner);
        result.setMealType(getMealType());

        return result;
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
     * 后厨模块获取商品
     *
     * @param menuDate
     * @param mealType
     * @return
     */
    public List<MessDailyMenuItemListDTO> listProductForKitchen(LocalDate menuDate, Integer mealType) {
        MessDailyMenu menu = messDailyMenuRepository.findByMenuDate(menuDate);
        if (menu == null) {
            return Collections.emptyList();
        }
        Specification<MessDailyMenuItem> spec = SpecificationBuilder.builder()
                .fetch("product", JoinType.LEFT)
                .where(Criterion.eq("menuId", menu.getId()))
                .where(Criterion.eq("mealType", mealType == null ? null : MealTypeEnum.byCode(mealType)))
                .build();
        List<MessDailyMenuItem> menuItems = messDailyMenuItemRepository.findAll(spec);
        final LocalDateTime begin = menuDate.atTime(LocalTime.MIN);
        final LocalDateTime end = menuDate.atTime(LocalTime.MAX);
        Set<Long> allCook = menuItems.stream().map(MessDailyMenuItem::getCookId).collect(Collectors.toSet());
        Map<Long, FeignEmployeeDTO> cookMap = feignEmployeeService.findByIds(allCook);
        return menuItems.stream().map(item -> {
            MessDailyMenuItemListDTO listDTO = new MessDailyMenuItemListDTO();
            listDTO.setId(item.getProduct().getId());
            listDTO.setName(item.getProduct().getName());

            if (cookMap.containsKey(item.getCookId())) {
                FeignEmployeeDTO cookItem = cookMap.get(item.getCookId());
                listDTO.setCookName(cookItem != null ? cookItem.getName() : null);
                listDTO.setCookId(cookItem != null ? cookItem.getId() : null);
            }

            listDTO.setImg(item.getProduct().getImg());

            CommentsDTO commentsDTO = messProductCommentRepository.findByProductIdAndTime(item.getProduct().getId(), begin, end);
            listDTO.setFiveStar(commentsDTO.getFiveStar() == null ? BigDecimal.ZERO : commentsDTO.getFiveStar());
            listDTO.setFourStar(commentsDTO.getFourStar() == null ? BigDecimal.ZERO : commentsDTO.getFourStar());
            listDTO.setThreeStar(commentsDTO.getThreeStar() == null ? BigDecimal.ZERO : commentsDTO.getThreeStar());
            listDTO.setTwoStar(commentsDTO.getTwoStar() == null ? BigDecimal.ZERO : commentsDTO.getTwoStar());
            listDTO.setOneStar(commentsDTO.getOneStar() == null ? BigDecimal.ZERO : commentsDTO.getOneStar());
            listDTO.setPraiseCount(listDTO.getFiveStar().add(listDTO.getFourStar()).add(listDTO.getThreeStar()).intValue());
            listDTO.setNegativeCount(listDTO.getTwoStar().add(listDTO.getOneStar()).intValue());
            return listDTO;
        }).collect(Collectors.toList());
    }

    public List<MessProductRecommendDTO> listProductForMenu(String date, Integer catalog) {
        MealTypeEnum mealType = MealTypeEnum.byCode(1);
        String zsetKey = RedisKeyUtil.getMessProductRecommendZsetKey(date, mealType);
        redisTemplate.opsForZSet().reverseRangeWithScores(zsetKey, 0, -1);
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(zsetKey, 0, -1);
        Map<String, Integer> countMap = new HashMap<>(typedTuples.size());
        typedTuples.forEach(item -> {
            countMap.put(String.valueOf(item.getValue()), item.getScore().intValue());
        });

        String catalogStr = MessProductUtil.buildCatalogLikeStr(catalog);
        String useForStr = MessProductUtil.buildCatalogLikeStr(1);
        List<MessProduct> productDTOS = messProductRepository.listForMenu(catalogStr, useForStr);
        List<LocalDate> weekDate = LocalDateUtil.getCurrentWeekDate();
        List<MessProductRecommendDTO> products = productDTOS.stream().map((item) -> {
            MessProductRecommendDTO recommendDTO = new MessProductRecommendDTO();
            recommendDTO.setId(item.getId());
            recommendDTO.setName(item.getName());
            recommendDTO.setImg(item.getImg());
            recommendDTO.setDishClass(item.getDishClass());

            String inweekCountKey = RedisKeyUtil.getProductInWeekRecommendCountKey(item.getId());
            Object oldCount = redisTemplate.opsForValue().get(inweekCountKey);
            recommendDTO.setRecommendCount(oldCount == null ? 0 : (Integer) oldCount);

            //本周已排次数
            List<MessDailyMenu> dailyMenuList = this.messDailyMenuRepository.queryByMenuDateBetween(weekDate.get(0), weekDate.get(1));
            if (CollectionUtils.isEmpty(dailyMenuList)) {
                recommendDTO.setWeekTimes(0);
            } else {
                Set<MessDailyMenuItem> menuItems = new HashSet<>();
                for (MessDailyMenu menu : dailyMenuList) {
                    menuItems.addAll(menu.getMenuItems());
                }

                List<MessProduct> productList = menuItems.stream().map(MessDailyMenuItem::getProduct).filter(d -> d.getId().equals(item.getId())).collect(Collectors.toList());
                recommendDTO.setWeekTimes(CollectionUtils.isEmpty(productList) ? 0 : productList.size());
            }

            //好评率
            recommendDTO.setFavourRate(null == item.getFavorRate() ? BigDecimal.ZERO : item.getFavorRate());
            return recommendDTO;
        }).collect(Collectors.toList());
        products.sort((o1, o2) -> o2.getRecommendCount() - o1.getRecommendCount());
        return products;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addMenuItem(MessDailyMenuAddReq req) {
        LocalDateTime now = LocalDateTime.now();
        MessDailyMenu messDailyMenu = messDailyMenuRepository.findByMenuDate(req.getMenuDate());
        if (messDailyMenu == null) {
            messDailyMenu = new MessDailyMenu();
            messDailyMenu.setMenuDate(req.getMenuDate());
            messDailyMenu.setCreateTime(now);
            messDailyMenu.setRemark(req.getRemark());
            messDailyMenu = messDailyMenuRepository.save(messDailyMenu);
        }

        MessProduct product = messProductRepository.findById(req.getProductId()).orElseThrow(() -> new BizException("", "菜品不存在"));
        MessDailyMenuItem messDailyMenuItem = this.messDailyMenuItemRepository.findByMenuIdAndProductAndMealType(messDailyMenu.getId(), product, req.getMealType());
        if (null != messDailyMenuItem) {
            return messDailyMenuItem.getId();
        }

        messDailyMenuItem = new MessDailyMenuItem();
        messDailyMenuItem.setProduct(product);
        messDailyMenuItem.setMenuId(messDailyMenu.getId());
        messDailyMenuItem.setMealType(req.getMealType());
        messDailyMenuItem = this.messDailyMenuItemRepository.save(messDailyMenuItem);
        return messDailyMenuItem.getId();
    }

    /**
     * 删除排菜
     *
     * @param id
     */
    public void delMenuItem(Long id) {
        MessDailyMenuItem messDailyMenuItem = this.messDailyMenuItemRepository.findById(id).orElse(null);
        if (null != messDailyMenuItem) {
            this.messDailyMenuRepository.findById(messDailyMenuItem.getMenuId()).orElseThrow(() -> new BizException("", "未找到菜单"));
            this.messDailyMenuItemRepository.delete(messDailyMenuItem);
        }
    }

    /**
     * 添加厨师
     */
    public void addCook(MessDailyMenuCookAddReq req) {
        MessDailyMenuItem messDailyMenuItem = this.messDailyMenuItemRepository.findById(req.getItemId()).orElse(null);
        if (messDailyMenuItem == null) {
            throw new BizException("", "菜单不存在或已被删除");
        }

        messDailyMenuItem.setCookId(req.getCookId());
        this.messDailyMenuItemRepository.save(messDailyMenuItem);
    }

    /**
     * 菜谱排序
     *
     * @date 2021/01/23 17:10
     */
    @Transactional(rollbackFor = Exception.class)
    public void sortMenuItem(List<Long> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }

        List<MessDailyMenuItem> itemList = this.messDailyMenuItemRepository.findAllById(itemIds);
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }

        Integer index = 0;
        for (Long itemId : itemIds) {
            for (MessDailyMenuItem item : itemList) {
                if (Objects.equals(itemId, item.getId())) {
                    item.setSeq(++index);
                }
            }
        }

        this.messDailyMenuItemRepository.saveAll(itemList);
    }

    /**
     * 复制菜谱
     *
     * @author HJJ
     * @date 2021/5/6 18:16
     **/
    @Transactional(rollbackFor = Exception.class)
    public void copyMenu(MessDailyMenuCopyReq req) {
        MessDailyMenu messDailyMenu = this.messDailyMenuRepository.findByMenuDate(req.getCopyDate());
        if (messDailyMenu == null) {
            throw new BizException("未找到当天记录");
        }

        MessDailyMenu dailyMenu = this.messDailyMenuRepository.findByMenuDate(req.getMenuDate());
        MessDailyMenu menu = new MessDailyMenu();

        List<MessDailyMenuItem> messDailyMenuItems = this.messDailyMenuItemRepository.findByMenuIdAndMealType(messDailyMenu.getId(), req.getCode());
        List<MessDailyMenuItem> items = new ArrayList<>();
        for (MessDailyMenuItem messDailyMenuItem : messDailyMenuItems) {
            if (messDailyMenuItem.getCookId() == null || messDailyMenuItem.getProduct() == null) {
                continue;
            }
            Optional<MessProduct> product = this.messProductRepository.findById(messDailyMenuItem.getProduct().getId());
            if (!product.isPresent()) {
                continue;
            }
            if (!product.get().getEnabled()) {
                continue;
            }
            MessDailyMenuItem item = new MessDailyMenuItem();
            item.setProduct(messDailyMenuItem.getProduct());
            item.setCookId(messDailyMenuItem.getCookId());
            item.setMealType(messDailyMenuItem.getMealType());
            if (dailyMenu == null) {
                menu.setMenuDate(req.getMenuDate());
                menu.setCreateTime(LocalDateTime.now());
                this.messDailyMenuRepository.save(menu);
                item.setMenuId(menu.getId());
            } else {
                item.setMenuId(dailyMenu.getId());
            }

            MessDailyMenuItem dailyMenuItem = this.messDailyMenuItemRepository.findByMenuIdAndProductAndMealType(item.getMenuId(), item.getProduct(), item.getMealType());
            if (null != dailyMenuItem) {
                continue;
            }

            items.add(item);
            this.messDailyMenuItemRepository.save(item);
        }

    }

    /**
     * 查询所有厨师
     *
     * @return
     */
    public List<FeignCookDto> findAllCook() {
        return feignEmployeeService.findAllCook();
    }

    /**
     * 智慧采购获取菜单
     *
     * @param date
     * @return
     */
    public FeignProcurementDto findProductProcurement(@RequestParam LocalDate date) {
        MessDailyMenu messDailyMenu = messDailyMenuRepository.findByMenuDate(date);

        FeignProcurementDto result = new FeignProcurementDto();
        result.setBreakfast(new ArrayList<>());
        result.setDinner(new ArrayList<>());
        result.setLunch(new ArrayList<>());

        if (messDailyMenu == null || CollectionUtils.isEmpty(messDailyMenu.getMenuItems())) {
            return result;
        }

        Set<Long> cookIds = messDailyMenu.getMenuItems().stream().map(MessDailyMenuItem::getCookId).collect(Collectors.toSet());
        Map<Long, FeignEmployeeDTO> cookMap = feignEmployeeService.findByIds(cookIds);

        for (MessDailyMenuItem menuItem : messDailyMenu.getMenuItems()) {
            if (menuItem.getMealType() == null) {
                continue;
            }

            FeignProcurementDto.MenuProd menuProd = new FeignProcurementDto.MenuProd();
            MessProduct messProduct = menuItem.getProduct();
            if (messProduct == null) {
                continue;
            }

            menuProd.setId(messProduct.getId())
                    .setName(messProduct.getName())
                    .setRawMaterial(messProduct.getMaterial())
                    .setCookId(menuItem.getCookId());

            if (cookMap.containsKey(menuItem.getCookId())) {
                FeignEmployeeDTO feignEmployeeDTO = cookMap.get(menuItem.getCookId());
                menuProd.setCookName(feignEmployeeDTO.getName())
                        .setCookImg(feignEmployeeDTO.getAvatar());
            }

            switch (menuItem.getMealType()) {
                case BREAKFAST: {
                    result.getBreakfast().add(menuProd);
                    break;
                }
                case LUNCH: {
                    result.getLunch().add(menuProd);
                    break;
                }
                case DINNER: {
                    result.getDinner().add(menuProd);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取菜品溯源链
     *
     * @author loki
     * @date 2021/7/29 15:48
     **/
    public MenuChain getMenuSourceChain(Long menuId) {
        MessDailyMenu dailyMenu = messDailyMenuRepository.findByMenuDate(LocalDate.now());
        if (null == dailyMenu || CollectionUtils.isEmpty(dailyMenu.getMenuItems())) {
            return null;
        }
        List<MessDailyMenuItem> dailyMenuItemList = dailyMenu.getMenuItems().stream().filter(item -> item.getProduct().getId().equals(menuId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dailyMenuItemList)) {
            return null;
        }

        MessDailyMenuItem menuItem = dailyMenuItemList.get(0);
        MenuChain menuChain = new MenuChain();
        menuChain.setProductName(menuItem.getProduct().getName());
        menuChain.setCookName(menuItem.getCookName());
        menuChain.setMaterialList(this.getMenuMaterialChain(menuId));

        return menuChain;
    }

    /**
     * 获取菜品溯源链
     *
     * @author loki
     * @date 2021/7/29 15:35
     **/
    private List<MasterMaterialDTO> getMenuMaterialChain(Long menuId) {
        MessProduct product = this.messProductRepository.findById(menuId).orElse(null);
        if (null == product || StringUtils.isBlank(product.getMaterial())) {
            return Collections.EMPTY_LIST;
        }


        List<MessProductMaterialDTO> materials;
        try {
            materials = JSONObject.parseArray(product.getMaterial(), MessProductMaterialDTO.class);
        } catch (Exception ex) {
            log.info("解析商品原材料异常");
            return Collections.EMPTY_LIST;
        }

        if (CollectionUtils.isEmpty(materials)) {
            return Collections.EMPTY_LIST;
        }

        return feignStockService.getSourceChain(materials.stream().filter(item -> item.getMaster() == 0).map(MessProductMaterialDTO::getId).collect(Collectors.toSet()));
    }
}