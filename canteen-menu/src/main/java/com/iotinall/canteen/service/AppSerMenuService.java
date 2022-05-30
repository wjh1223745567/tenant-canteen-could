package com.iotinall.canteen.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.dto.menu.MenuDto;
import com.iotinall.canteen.dto.menu.MenuMainDto;
import com.iotinall.canteen.dto.mess.CommentsDTO;
import com.iotinall.canteen.dto.messcookdetail.MenuCookDTO;
import com.iotinall.canteen.dto.messdaily.MessDailyMenuItemListDTO;
import com.iotinall.canteen.dto.messdaily.ProductEvaluationDetailsDto;
import com.iotinall.canteen.dto.messdaily.ProductEvaluationDetailsInfoDto;
import com.iotinall.canteen.dto.messdailymenu.AppMessDailyMenuAddReq;
import com.iotinall.canteen.dto.messprod.MessProductSimpleDTO;
import com.iotinall.canteen.dto.recommend.MessMenuRecommendReq;
import com.iotinall.canteen.dto.recommend.MessProductCommentAddReq;
import com.iotinall.canteen.dto.recommend.RecommendDto;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.nutritionenc.FeignCraftDto;
import com.iotinall.canteen.dto.nutritionenc.FeignFlavourDto;
import com.iotinall.canteen.dto.nutritionenc.FeignSimMessProdReq;
import com.iotinall.canteen.dto.nutritionenc.FeignSysMaterialReq;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignMessProductCookView;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.BigDecimalUtil;
import com.iotinall.canteen.utils.MessProductUtil;
import com.iotinall.canteen.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.JoinType;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/69:26
 */
@Slf4j
@Service
public class AppSerMenuService {
    @Resource
    private MessDailyMenuRepository messDailyMenuRepository;
    @Resource
    private MessDailyMenuItemRepository messDailyMenuItemRepository;
    @Resource
    private MessProductCommentRepository messProductCommentRepository;
    @Resource
    private MessProductRepository messProductRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private MessProductRecommendRecordRepository messProductRecommendRecordRepository;

    @Resource
    private FeignNutritionPersonRecordService feignNutritionPersonRecordService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private FeignChronicDiseaseService feignChronicDiseaseService;

    @Resource
    private BaiduMenuService baiduMenuService;

    @Resource
    private FeignNutritionStatureRecordService feignNutritionStatureRecordService;

    @Resource
    private FeignCraftService feignCraftService;

    @Resource
    private FeignFlavourService feignFlavourService;

    @Transactional(rollbackFor = Exception.class)
    public void addDailyMenu(AppMessDailyMenuAddReq req) {
        if (CollectionUtils.isEmpty(req.getMenuCookList())) {
            throw new BizException("", "请选择菜谱");
        }

        MessDailyMenu messDailyMenu = this.messDailyMenuRepository.findFirstByMenuDate(req.getMenuDate());
        if (null == messDailyMenu) {
            messDailyMenu = new MessDailyMenu();
            messDailyMenu.setCreateTime(LocalDateTime.now());
            messDailyMenu.setMenuDate(req.getMenuDate());
            messDailyMenu = this.messDailyMenuRepository.save(messDailyMenu);
        }

        if (!CollectionUtils.isEmpty(messDailyMenu.getMenuItems())) {
            List<MessDailyMenuItem> result = messDailyMenu.getMenuItems().stream().filter(item -> item.getMealType().equals(req.getMealType())).collect(Collectors.toList());
            this.messDailyMenuItemRepository.deleteAll(result);
        }

        Optional<MessProduct> product;
        MessDailyMenuItem dailyMenuItem;
        List<MessDailyMenuItem> menuItemList = new ArrayList<>();
        for (MenuCookDTO menuCook : req.getMenuCookList()) {
            product = this.messProductRepository.findById(menuCook.getMenuId());
            if (!product.isPresent()) {
                throw new BizException("", "请选择菜谱");
            }

            dailyMenuItem = this.messDailyMenuItemRepository.queryByMealTypeAndProductAndMenuId(req.getMealType(), product.get(), messDailyMenu.getId());
            if (dailyMenuItem != null) {
                continue;
            }

            dailyMenuItem = new MessDailyMenuItem();
            menuItemList.add(dailyMenuItem);
            dailyMenuItem.setMenuId(messDailyMenu.getId());
            dailyMenuItem.setProduct(product.get());
            dailyMenuItem.setMealType(req.getMealType());
            dailyMenuItem.setCookId(menuCook.getCookId());
        }

        this.messDailyMenuItemRepository.saveAll(menuItemList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delDailyMenu(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException("", "请选择需要删除的菜");
        }

        List<MessDailyMenuItem> messDailyMenuItemList = this.messDailyMenuItemRepository.findAllById(ids);
        if (!CollectionUtils.isEmpty(messDailyMenuItemList)) {
            MessDailyMenu messDailyMenu = this.messDailyMenuRepository.findById(messDailyMenuItemList.get(0).getMenuId()).orElseThrow(() -> new BizException("", "未找到菜单"));
            this.messDailyMenuItemRepository.deleteAll(messDailyMenuItemList);
        }

    }

    public MenuMainDto listMenu(LocalDate date) {
        MessDailyMenu dailyMenu = this.messDailyMenuRepository.findFirstByMenuDate(date);
        MenuMainDto menuMainDto = new MenuMainDto();
        if (dailyMenu == null || dailyMenu.getMenuItems().isEmpty()) {
            menuMainDto.setState(0);
            menuMainDto.setBreakfast(Collections.emptyList());
            menuMainDto.setDinner(Collections.emptyList());
            menuMainDto.setLunch(Collections.emptyList());
            return menuMainDto;
        }
        List<MessDailyMenuItem> menuItems = dailyMenu.getMenuItems();

        List<MenuDto> breakfast = new ArrayList<>();
        List<MenuDto> lunch = new ArrayList<>();
        List<MenuDto> dinner = new ArrayList<>();

        String disease = feignNutritionPersonRecordService.findDisease();

        for (MessDailyMenuItem item : menuItems) {

            FeignEmployeeDTO employeeDTO = item.getCookId() != null ? feignEmployeeService.findById(item.getCookId()) : null;

            MenuDto menuDto = new MenuDto();
            menuDto.setDailyMenuId(item.getId());
            menuDto.setCookId(employeeDTO != null ? employeeDTO.getId() : null);
            menuDto.setCookName(employeeDTO != null ? employeeDTO.getName() : null);
            menuDto.setCookAvatar(employeeDTO != null ? employeeDTO.getAvatar() : null);
            menuDto.setId(item.getProduct().getId());
            menuDto.setImage(item.getProduct().getImg());
            menuDto.setName(item.getProduct().getName());
            menuDto.setStars(item.getProduct().getAvgScore());
            menuDto.setIsRecommend(true);
            menuDto.setDishClass(item.getProduct().getDishClass());
            menuDto.setSuitable( StringUtils.isNotBlank(disease) ? this.isSuitable(disease, item.getProduct()) : null);
            switch (item.getMealType()) {
                case BREAKFAST:
                    breakfast.add(menuDto);
                    break;
                case LUNCH:
                    lunch.add(menuDto);
                    break;
                case DINNER:
                    dinner.add(menuDto);
                    break;
            }
        }
        menuMainDto.setState(1);
        menuMainDto.setBreakfast(breakfast);
        menuMainDto.setLunch(lunch);
        menuMainDto.setDinner(dinner);
        return menuMainDto;
    }

    /**
     * 疾病是否适用
     */
    private Boolean isTheDiseaseUsed(Set<String> personDisease, MessProduct product) {
        Set<Boolean> allDiseasesResult = new HashSet<>();
        for (String s : personDisease) {
            if (StringUtils.isBlank(s)) {
                continue;
            }
            FeignSimMessProdReq messProdReq = new FeignSimMessProdReq()
                    .setId(product.getId())
                    .setName(product.getName())
                    .setPersonDisease(s);
            Integer result = this.feignChronicDiseaseService.cacheIsTheDiseaseUsed(messProdReq);
            Boolean flag = (result == 0 ? null : result == 1 ? Boolean.TRUE : result == 2 ? Boolean.FALSE : null);
            if (flag != null && !flag) {
                return Boolean.FALSE;
            } else if (flag != null) {
                allDiseasesResult.add(true);
            }
        }

        if (CollectionUtils.isEmpty(allDiseasesResult)) {
            return null;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * 是否适用
     *
     * @author loki
     * @date 2020/12/08 19:21
     */
    public Boolean isSuitable(String disease, MessProduct product) {

        Set<String> personDisease = new HashSet<>(StringUtils.isNotBlank(disease) ? Arrays.asList(disease.split(";")) : Collections.singleton(""));

        Set<String> productContraindications = null;
        if (StringUtils.isNotBlank(product.getContraindications())) {
            //不适宜慢性病
            productContraindications = new HashSet<>(Arrays.asList(product.getContraindications().split(";")));

            productContraindications.retainAll(personDisease);

        }

        Set<String> productSuitableList = null;
        if (StringUtils.isNotBlank(product.getSuitableDisease())) {
            //适宜慢性病
            productSuitableList = new HashSet<>(Arrays.asList(product.getSuitableDisease().split(";")));
            productSuitableList.retainAll(personDisease);
        }

        if (!CollectionUtils.isEmpty(productContraindications)) {
            return false;
        } else if (!CollectionUtils.isEmpty(productSuitableList)) {
            return true;
        } else {
            //自动对比已有菜谱
            Boolean result = this.isTheDiseaseUsed(personDisease, product);
            if (result == null) {
                //根据体型身体元素，是否适宜菜谱
                if(StringUtils.isNotBlank(product.getMaterial())){
                    List<FeignSysMaterialReq> materialReqs = JSON.parseObject(product.getMaterial(), new TypeReference<List<FeignSysMaterialReq>>(){});
                    return this.feignNutritionStatureRecordService.isElementApplication(materialReqs);
                }else{
                    return null;
                }
            } else {
                return result;
            }
        }
    }

    public List<MessDailyMenuItemListDTO> listProductForKitchen(LocalDate menuDate, Integer mealType) {
        MessDailyMenu menu = messDailyMenuRepository.findFirstByMenuDate(menuDate);
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
        return menuItems.stream().map(item -> {
            MessDailyMenuItemListDTO listDTO = new MessDailyMenuItemListDTO();
            listDTO.setId(item.getProduct().getId());
            listDTO.setName(item.getProduct().getName());

            if(item.getCookId() != null){
                FeignEmployeeDTO employeeDTO = this.feignEmployeeService.findById(item.getCookId());
                listDTO.setCookId(employeeDTO != null ? employeeDTO.getId() : null);
                listDTO.setCookName(employeeDTO != null ? employeeDTO.getName() : null);
                listDTO.setCookAvatar(employeeDTO != null ? employeeDTO.getAvatar() : null);
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

    public ProductEvaluationDetailsDto getProductView(Long productId) {
        MessProduct product = this.messProductRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new BizException("notFindProduct", "未找到当前菜品");
        }

        String disease = this.feignNutritionPersonRecordService.findDisease();
        ProductEvaluationDetailsInfoDto evaluationDetailsInfoDto = new ProductEvaluationDetailsInfoDto()
                .setCuisines(product.getCuisines().stream().map(MessProductCuisine::getCuisineName).collect(Collectors.toSet()))
                .setTaste(product.getTaste());

        if(product.getCraftId() != null){
            FeignCraftDto feignCraftDto = this.feignCraftService.findDtoById(product.getCraftId());
            if(feignCraftDto != null){
                evaluationDetailsInfoDto.setCraft(feignCraftDto.getName());
            }
        }

        if(product.getFlavourId() != null){
            FeignFlavourDto feignFlavourDto = this.feignFlavourService.findDtoById(product.getFlavourId());
            if(feignFlavourDto != null){
                evaluationDetailsInfoDto.setFlavour(feignFlavourDto.getName());
            }
        }

        return new ProductEvaluationDetailsDto()
                .setDetailsInfoDto(evaluationDetailsInfoDto)
                .setPractice(product.getPractice())
                .setId(product.getId())
                .setSuitableDisease(product.getSuitableDisease() != null ? product.getSuitableDisease().replace(";", ",") : null)
                .setContraindications(product.getContraindications() != null ? product.getContraindications().replace(";", ",") : null)
                .setCatalog(product.getCatalog())
                .setImage(product.getImg())
                .setInfo(product.getIntro())
                .setName(product.getName())
                .setSuitable(StringUtils.isNotBlank(disease) ? this.isSuitable(disease, product) : null)
                .setMaterial(product.getMaterial())
                //.setIsRecommend(product.getRecommendedEmployId() != null ? Arrays.asList(product.getRecommendedEmployId().split(",")).contains(empId.toString()) : Boolean.FALSE)
                .setRecommendCount(product.getRecommendedCount())
                .setStar(product.getAvgScore().doubleValue());
    }

    public CursorPageDTO<RecommendDto> pageCommentByProduct(Long empid, Long productId, Long cursor) {
        Specification<MessProductComment> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("productId", productId))
                .where(Criterion.lt("createTime", cursor != null ? Instant.ofEpochMilli(cursor).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime() : null))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<MessProductComment> productComments = this.messProductCommentRepository.findAll(spec, pageRequest);

        List<RecommendDto> recommends = productComments.get().map(item -> {
            RecommendDto recommend = new RecommendDto();
            recommend.setId(item.getId())

                    .setInfo(item.getContent())
                    .setAnonymous(item.getAnonymous())
                    .setTags(item.getTags())
                    .setPraiseCount(item.getFavorCount())
                    .setTreadCount(item.getOppositeCount())
                    .setStar(item.getScore().doubleValue())

                    .setIsPraise(item.getFavorEmpId() != null ? Arrays.asList(item.getFavorEmpId().split(",")).contains(empid.toString()) : Boolean.FALSE)
                    .setIsTread(item.getOppositeEmpId() != null ? Arrays.asList(item.getOppositeEmpId().split(",")).contains(empid.toString()) : Boolean.FALSE)
                    .setTime(item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")))
                    .setCreateTime(item.getCreateTime() != null ? item.getCreateTime().atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli() : null);

            if (item.getEmployeeId() != null) {
                FeignEmployeeDTO feignEmployeeDTO = this.feignEmployeeService.findById(item.getEmployeeId());

                recommend.setIsUser(feignEmployeeDTO != null && feignEmployeeDTO.getId().equals(SecurityUtils.getUserId()))
                        .setImage(feignEmployeeDTO.getAvatar())
                        .setName(feignEmployeeDTO.getName());
            }

            return recommend;
        }).collect(Collectors.toList());
        long total = this.messProductCommentRepository.countAllByProductId(productId);
        return PageUtil.toCursorPageDTO(recommends, recommends.size() != 0 ? recommends.get(recommends.size() - 1).getCreateTime() : -1, total);
    }


    @Transactional(rollbackFor = Exception.class)
    public Long toggleRecommend(MessMenuRecommendReq req) {
        Long empId = SecurityUtils.getUserId();
        if (Objects.equals(req.getRecommend(), Boolean.TRUE)) {
            return this.addRecommend(req, empId);
        } else {
            return this.cancelRecommend(req, empId);
        }
    }

    /**
     * 推荐
     *
     * @param req   推荐信息
     * @param empId 用户id
     * @return 返回总的推荐人数
     */
    private Long addRecommend(MessMenuRecommendReq req, Long empId) {
        String key = RedisKeyUtil.getMessProductRecommendKey(req.getDate(), req.getMealType(), req.getProductId());
        String zsetKey = RedisKeyUtil.getMessProductRecommendZsetKey(req.getDate(), req.getMealType());

        Long i = redisTemplate.opsForSet().add(key, empId);

        //添加当前周推荐次数
        String inweekCountKey = RedisKeyUtil.getProductInWeekRecommendCountKey(req.getProductId());
        Object oldCount = redisTemplate.opsForValue().get(inweekCountKey);
        redisTemplate.opsForValue().set(inweekCountKey, (oldCount != null && (Integer) oldCount > 0 ? (Integer) oldCount + 1 : 1), 7, TimeUnit.DAYS);

        if (i > 0) {
            Set<?> members = redisTemplate.opsForSet().members(key);
            MessProductRecommendRecord recommendRecord = null;
            int size = members.size();
            if (size == 1) {
                recommendRecord = messProductRecommendRecordRepository.findByDateAndProductIdForUpdate(req.getDate(), req.getProductId());
                if (recommendRecord == null) {
                    recommendRecord = new MessProductRecommendRecord();
                    recommendRecord.setProductId(req.getProductId());
                    recommendRecord.setDate(req.getDate());
                    recommendRecord.setNegativeCount(0);
                    recommendRecord.setRecommendCount(1);
                    recommendRecord.setPraiseCount(0);
                    recommendRecord.setEmpList(String.valueOf(empId));
                    recommendRecord.setCreateTime(LocalDateTime.now());
                    messProductRecommendRecordRepository.save(recommendRecord);
                    redisTemplate.opsForZSet().incrementScore(zsetKey, req.getProductId(), 1);
                    messProductRepository.addRecommendedCount(req.getProductId(), 1);
                    return 1L;
                }
            }
            if (recommendRecord == null) {
                recommendRecord = messProductRecommendRecordRepository.findByDateAndProductId(req.getDate(), req.getProductId());
            }
            String empList = StringUtils.join(members, ",");
            messProductRecommendRecordRepository.updateRecommendEmp(size, empList, recommendRecord.getId());
            messProductRepository.addRecommendedCount(req.getProductId(), 1);
            redisTemplate.opsForZSet().incrementScore(zsetKey, req.getProductId(), 1);
            if (size % 2 == 1) {
                redisTemplate.expire(key, 14, TimeUnit.DAYS);
                redisTemplate.expire(zsetKey, 14, TimeUnit.DAYS); // 14天
            }

            return (long) size;
        } else {
            return redisTemplate.opsForSet().size(key);
        }
    }

    /**
     * 取消推荐
     *
     * @param req   推荐信息
     * @param empId 用户id
     * @return 返回总的推荐人数
     */
    public Long cancelRecommend(MessMenuRecommendReq req, Long empId) {
        String key = RedisKeyUtil.getMessProductRecommendKey(req.getDate(), req.getMealType(), req.getProductId());
        String zsetKey = RedisKeyUtil.getMessProductRecommendZsetKey(req.getDate(), req.getMealType());
        Long i = redisTemplate.opsForSet().remove(key, empId);

        String inweekCountKey = RedisKeyUtil.getProductInWeekRecommendCountKey(req.getProductId());
        Object oldCount = redisTemplate.opsForValue().get(inweekCountKey);
        redisTemplate.opsForValue().set(inweekCountKey, (oldCount != null && (Integer) oldCount > 0 ? (Integer) oldCount - 1 : 0), 7, TimeUnit.DAYS);

        if (i > 0) {
            MessProductRecommendRecord recommendRecord = messProductRecommendRecordRepository.findByDateAndProductId(req.getDate(), req.getProductId());
            Set<?> members = redisTemplate.opsForSet().members(key);
            String empList = StringUtils.join(members, ",");
            messProductRecommendRecordRepository.updateRecommendEmp(members.size(), empList, recommendRecord.getId());
            messProductRepository.addRecommendedCount(req.getProductId(), -1);
            redisTemplate.opsForZSet().incrementScore(zsetKey, req.getProductId(), -1);
            return (long) members.size();
        } else {
            return redisTemplate.opsForSet().size(key);
        }
    }

    /**
     * 添加评论
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void addComment(MessProductCommentAddReq req, Long empid) {
        Optional<MessProduct> optionalMessProduct = this.messProductRepository.findById(req.getProductId());
        if (optionalMessProduct.isPresent()) {
            MessProductComment messProductComment = new MessProductComment()
                    .setProductId(req.getProductId())
                    .setAnonymous(req.getAnonymous() != null ? req.getAnonymous() : Boolean.FALSE)
                    .setContent(req.getContent())
                    .setFavorCount(0)
                    .setOppositeCount(0)
                    .setScore(req.getScore())
                    .setTags(req.getTags())
                    .setEmployeeId(empid);
            messProductCommentRepository.saveAndFlush(messProductComment);
            MessProduct messProduct = optionalMessProduct.get();

            //平均分
            BigDecimal score = messProductCommentRepository.avgStarByProduct(req.getProductId());
            messProduct.setAvgScore(score);

            //好评率
            messProduct.setFavorRate(calculateFavorRate(messProduct.getId()));
            messProductRepository.save(messProduct);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void subComment(Long id) {
        MessProductComment productComment = this.messProductCommentRepository.findById(id).orElseThrow(() -> new BizException("", "未找到评论信息"));
        if (!Objects.equals(productComment.getEmployeeId(), SecurityUtils.getUserId())) {
            throw new BizException("", "非当前用户评论无法删除");
        }

        MessProduct messProduct = this.messProductRepository.findById(productComment.getProductId()).orElseThrow(() -> new BizException("", "未找到当前菜品信息"));
        //平均分
        BigDecimal score = messProductCommentRepository.avgStarByProduct(messProduct.getId());
        messProduct.setAvgScore(score);

        //好评率
        messProduct.setFavorRate(calculateFavorRate(messProduct.getId()));
        messProductRepository.save(messProduct);
        this.messProductCommentRepository.delete(productComment);
    }

    /**
     * 计算好评率
     * 评分>=3的评分数/总评分数
     *
     * @date 2020/04/24 13:41
     */
    private BigDecimal calculateFavorRate(Long messProductId) {
        Long totalCount = this.messProductCommentRepository.countAllByProductId(messProductId);
        if (0 == totalCount) {
            return BigDecimal.ZERO;
        }

        Long favourCount = this.messProductCommentRepository.countAllByProductIdAndScoreGreaterThanEqual(messProductId, new BigDecimal(3));

        return BigDecimalUtil.divide(new BigDecimal(favourCount), new BigDecimal(totalCount), 1);
    }

    /**
     * 赞评论
     *
     * @param commid
     * @param empId
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFavor(Long commid, Long empId) {
        MessProductComment comment = this.messProductCommentRepository.findById(commid).orElse(null);
        if (comment == null) {
            throw new BizException("notFoundComment", "未找到当前评论");
        }
        if (comment.getFavorEmpId() != null && Arrays.asList(comment.getFavorEmpId().split(",")).contains(empId.toString())) {
            throw new BizException("isFavor", "当前用户已赞");
        }
        this.messProductCommentRepository.addFavor(commid, empId);
    }

    /**
     * 取消赞
     *
     * @param commid
     */
    @Transactional(rollbackFor = Exception.class)
    public void subFavor(Long commid) {
        MessProductComment comment = this.messProductCommentRepository.findById(commid).orElse(null);
        if (comment == null) {
            throw new BizException("", "未找到当前评论");
        }
        if (comment.getFavorEmpId() == null || !Arrays.asList(comment.getFavorEmpId().split(",")).contains(SecurityUtils.getUserId().toString())) {
            throw new BizException("", "当前用户未赞");
        }
        comment.setFavorCount(comment.getFavorCount() - 1);
        comment.setFavorEmpId(comment.getFavorEmpId().replace("," + SecurityUtils.getUserId().toString(), ""));
        if (StringUtils.isBlank(comment.getFavorEmpId())) {
            comment.setFavorEmpId(null);
        }
        this.messProductCommentRepository.save(comment);
    }

    /**
     * 踩 评论
     *
     * @param commid
     * @param empId
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOpposite(Long commid, Long empId) {
        MessProductComment comment = this.messProductCommentRepository.findById(commid).orElse(null);
        if (comment == null) {
            throw new BizException("notFoundComment", "未找到当前评论");
        }
        if (comment.getOppositeEmpId() != null && Arrays.asList(comment.getOppositeEmpId().split(",")).contains(empId.toString())) {
            throw new BizException("isFavor", "当前用户已踩");
        }
        this.messProductCommentRepository.addOpposite(commid, empId);
    }

    /**
     * 查询商品去推荐
     *
     * @param date       日期
     * @param currUserID 当前用户id
     * @return 返回数据
     */
    public MenuMainDto listForRecommend(String date, Long currUserID) {
        MenuMainDto menuMainDto = SpringContextUtil.getBean(AppSerMenuService.class).listAllForRecommend();
        Integer intCurrUserID = currUserID.intValue();
        menuMainDto.getBreakfast().forEach(item -> {
            String key = RedisKeyUtil.getMessProductRecommendKey(date, MealTypeEnum.BREAKFAST, item.getId());
            Set<Object> members = redisTemplate.opsForSet().members(key);
            item.setRecommendCount(members.size());
            item.setIsRecommend(members.contains(intCurrUserID));
        });
        menuMainDto.getLunch().forEach(item -> {
            String key = RedisKeyUtil.getMessProductRecommendKey(date, MealTypeEnum.LUNCH, item.getId());
            Set<Object> members = redisTemplate.opsForSet().members(key);
            item.setRecommendCount(members.size());
            item.setIsRecommend(members.contains(intCurrUserID));
        });
        menuMainDto.getDinner().forEach(item -> {
            String key = RedisKeyUtil.getMessProductRecommendKey(date, MealTypeEnum.DINNER, item.getId());
            Set<Object> members = redisTemplate.opsForSet().members(key);
            item.setRecommendCount(members.size());
            item.setIsRecommend(members.contains(intCurrUserID));
        });
        return menuMainDto;
    }

    public MenuMainDto listAllForRecommend() {
        MenuMainDto menuMainDto = new MenuMainDto();
        String useFor = MessProductUtil.buildCatalogLikeStr(1);
        List<MessProduct> breakfast = messProductRepository.findByCatalogLikeAndUseForLike(MessProductUtil.buildCatalogLikeStr(MealTypeEnum.BREAKFAST.getCode()), useFor);
        List<MessProduct> lunch = messProductRepository.findByCatalogLikeAndUseForLike(MessProductUtil.buildCatalogLikeStr(MealTypeEnum.LUNCH.getCode()), useFor);
        List<MessProduct> dinner = messProductRepository.findByCatalogLikeAndUseForLike(MessProductUtil.buildCatalogLikeStr(MealTypeEnum.DINNER.getCode()), useFor);
        String disease = this.feignNutritionPersonRecordService.findDisease();
        menuMainDto.setBreakfast(translate(breakfast, disease));
        menuMainDto.setLunch(translate(lunch, disease));
        menuMainDto.setDinner(translate(dinner, disease));
        return menuMainDto;
    }

    private List<MenuDto> translate(List<MessProduct> products, String disease) {
        return products.stream().map(item -> {
            MenuDto dto = new MenuDto();
            dto.setSuitable(StringUtils.isNotBlank(disease) ? this.isSuitable(disease, item) : null);
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setStars(item.getAvgScore());
            dto.setImage(item.getImg());
            dto.setDishClass(item.getDishClass());
            dto.setNumberOfEvaluations(12);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<MessProductSimpleDTO> findMenuList(Integer type, LocalDate date) {
        //查询满足要求的菜谱ID
        MessDailyMenu dailyMenu = messDailyMenuRepository.findFirstByMenuDate(date);
        if (null == dailyMenu) {
            return null;
        }

        Set<Long> productIds = messDailyMenuRepository.queryMessDailyMenuItem(dailyMenu.getId(), type);
        if (CollectionUtils.isEmpty(productIds)) {
            return null;
        }

        List<MessProduct> messProducts = messProductRepository.queryByIdIn(productIds);
        return messProducts.stream().map(item -> new MessProductSimpleDTO()
                .setName(item.getName())
                .setImg(item.getImg())
                .setEnergy(item.getEnergy())
                .setId(item.getId())
        ).collect(Collectors.toList());
    }

    /**
     * 查询厨师信息
     *
     * @param itemId
     */
    public FeignMessProductCookView cookView(Long itemId) {
        Optional<MessDailyMenuItem> messDailyMenu = this.messDailyMenuItemRepository.findById(itemId);
        if (!messDailyMenu.isPresent()) {
            throw new BizException("", "未找到当前菜单");
        }
        Long employeeId = messDailyMenu.get().getCookId();
        if (employeeId == null) {
            throw new BizException("", "当前菜单未设置厨师");
        }
        return feignEmployeeService.findCookView(employeeId);
    }


}
