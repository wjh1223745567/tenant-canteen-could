package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.common.util.UUIDUtil;
import com.iotinall.canteen.dto.dish.*;
import com.iotinall.canteen.dto.messprod.MessProductMaterialDTO;
import com.iotinall.canteen.dto.dish.SysMaterialDTO;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.nutritionenc.FeignDishDto;
import com.iotinall.canteen.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
 * 菜单相关 ServiceImpl
 *
 * @author loki
 * @date 2020/03/25 14:52
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysDishService {

    @Resource
    SysDishRepository dishRepository;

    @Resource
    SysMaterialRepository materialRepository;

    @Resource
    SysCraftRepository craftRepository;

    @Resource
    SysFlavourRepository flavourRepository;

    @Resource
    SysMasterCuisineRepository masterCuisineRepository;

    @Resource
    SysCuisineRepository cuisineRepository;

    @Resource
    SysDishMaterialRepository dishMaterialRepository;

    @Resource
    SysDishCuisineRepository dishCuisineRepository;

    @Resource
    DishBizService dishBiz;

    @Resource
    private SysMaterialTypeRepository materialTypeRepository;

    @Cacheable(value = RedisCacheUtil.SYS_DISH_10)
    public List<SysDishDTO> findTop10Dish(String name) {
        List<SysDish> dishes = dishRepository.queryTop10DishByName(name);
        if (CollectionUtils.isEmpty(dishes)) {
            return Collections.emptyList();
        }

        return this.buildDishList(dishes);
    }

    /**
     * 构建菜谱
     */
    private List<SysDishDTO> buildDishList(List<SysDish> dishes) {
        List<SysDishDTO> dishDTOList = new ArrayList<>(dishes.size());
        SysDishDTO dishDTO;
        for (SysDish dish : dishes) {
            dishDTO = new SysDishDTO();
            BeanUtils.copyProperties(dish, dishDTO);

            //获取菜品的原材料,这样处理是因为关系表中存有数量等字段
            List<SysDishMaterialDTO> materials = this.findDishMaterialByDish(dish);
            if (!CollectionUtils.isEmpty(materials)) {
                dishDTO.setMaterials(materials);
            }

            //获取菜品类别
            if (!CollectionUtils.isEmpty(dish.getCuisines())) {
                List<SysCuisineDTO> cuisineDTOList = new ArrayList<>();
                SysCuisineDTO cuisineDTO;
                for (SysCuisine cuisine : dish.getCuisines()) {
                    cuisineDTO = new SysCuisineDTO();
                    cuisineDTO.setId(cuisine.getId());
                    cuisineDTO.setName(cuisine.getName());
                    cuisineDTO.setPid(cuisine.getMasterId());
                    cuisineDTOList.add(cuisineDTO);
                }
                dishDTO.setCuisines(cuisineDTOList);
            }

            if (null != dish.getFlavour()) {
                dishDTO.setFlavoursId(dish.getFlavour().getId());
                dishDTO.setFlavourName(dish.getFlavour().getName());
            }

            if (null != dish.getCraft()) {
                dishDTO.setCraftId(dish.getCraft().getId());
                dishDTO.setCraftName(dish.getCraft().getName());
            }
            dishDTOList.add(dishDTO);
        }

        return dishDTOList;
    }

    @Cacheable(value = RedisCacheUtil.SYS_MATERIAL_10)
    public List<MessProductMaterialDTO> findTop10Material(String name) {
        List<SysMaterial> materials = materialRepository.queryTop10MaterialByName(name);
        if (CollectionUtils.isEmpty(materials)) {
            return Collections.emptyList();
        }
        return materials.stream().map(material -> new MessProductMaterialDTO().setId(material.getId()).setName(material.getName()).setRestriction(material.getRestriction())).collect(Collectors.toList());
    }

    @Cacheable(value = RedisCacheUtil.SYS_CRAFT)
    public List<SysCraftDTO> findCraftList() {
        List<SysCraft> crafts = craftRepository.findAll();
        if (CollectionUtils.isEmpty(crafts)) {
            return null;
        }

        List<SysCraftDTO> craftDTOList = new ArrayList<>();
        SysCraftDTO craftDTO;
        for (SysCraft craft : crafts) {
            craftDTO = new SysCraftDTO();
            craftDTO.setId(craft.getId());
            craftDTO.setName(craft.getName());
            craftDTO.setDescription(craft.getDescription());
            craftDTOList.add(craftDTO);
        }
        return craftDTOList;
    }

    @Cacheable(value = RedisCacheUtil.SYS_FLAVOUR)
    public List<SysFlavourDTO> findFlavourList() {
        List<SysFlavour> flavours = flavourRepository.findAll();
        if (CollectionUtils.isEmpty(flavours)) {
            return null;
        }

        List<SysFlavourDTO> flavourDTOList = new ArrayList<>();
        SysFlavourDTO flavourDTO;
        for (SysFlavour flavour : flavours) {
            flavourDTO = new SysFlavourDTO();
            flavourDTO.setId(flavour.getId());
            flavourDTO.setName(flavour.getName());
            flavourDTO.setDescription(flavour.getDescription());
            flavourDTOList.add(flavourDTO);
        }

        return flavourDTOList;
    }

    @Cacheable(value = RedisCacheUtil.SYS_CUISINE)
    public List<SysMasterCuisineDTO> findCuisineList() {
        List<SysMasterCuisine> masterCuisines = masterCuisineRepository.findAll();
        if (CollectionUtils.isEmpty(masterCuisines)) {
            return null;
        }

        List<SysMasterCuisineDTO> masterCuisineDTOList = new ArrayList<>();
        SysMasterCuisineDTO masterCuisineDTO;
        for (SysMasterCuisine mCuisine : masterCuisines) {
            masterCuisineDTO = new SysMasterCuisineDTO();
            masterCuisineDTO.setId(mCuisine.getId());
            masterCuisineDTO.setName(mCuisine.getName());

            if (!CollectionUtils.isEmpty(mCuisine.getCuisines())) {
                List<SysCuisineDTO> cuisineDTOList = new ArrayList<>();
                SysCuisineDTO cuisineDTO;
                for (SysCuisine cuisine : mCuisine.getCuisines()) {
                    cuisineDTO = new SysCuisineDTO();
                    cuisineDTO.setId(cuisine.getId());
                    cuisineDTO.setName(cuisine.getName());
                    cuisineDTOList.add(cuisineDTO);
                }
                masterCuisineDTO.setCuisines(cuisineDTOList);
            }
            masterCuisineDTOList.add(masterCuisineDTO);
        }
        return masterCuisineDTOList;
    }

    public Object findCuisineTree() {
        List<SysMasterCuisineDTO> cuisineList = this.findCuisineList();

        //转成树
        return new CuisineTree().listToTree(cuisineList);
    }

    public List<SysDishMaterialDTO> findDishMaterialByDish(SysDish dish) {
        List<SysDishMaterial> materials = dishMaterialRepository.queryByDish(dish);
        if (CollectionUtils.isEmpty(materials)) {
            return Collections.EMPTY_LIST;
        }

        List<SysDishMaterialDTO> dishMaterialDTOList = new ArrayList<>();
        SysDishMaterialDTO dishMaterialDTO;
        for (SysDishMaterial m : materials) {
            dishMaterialDTO = new SysDishMaterialDTO();
            dishMaterialDTO.setId(m.getId());
            dishMaterialDTO.setAmount(m.getAmount());
            dishMaterialDTO.setDishId(m.getDish().getId());
            dishMaterialDTO.setDishName(m.getDish().getName());
            dishMaterialDTO.setMaterialId(m.getMaterial().getId());
            dishMaterialDTO.setMaterialName(m.getMaterial().getName());
            dishMaterialDTO.setMaster(m.getMaster());
            dishMaterialDTO.setRestriction(m.getMaterial().getRestriction());
            dishMaterialDTOList.add(dishMaterialDTO);
        }

        return dishMaterialDTOList;
    }

    public void updateImgUrl(String type) {
        if (type.equals("dish")) {
            List<SysDish> dishes = dishRepository.findAll();
            for (SysDish dish : dishes) {
                if (StringUtils.isBlank(dish.getImg())) {
                    continue;
                }

                String pre = "group1/M00/05/05/dishes/";
                String path = pre + dish.getImg();
                dish.setImg(path);

            }
            dishRepository.saveAll(dishes);
        } else if (type.equals("material")) {
            List<SysMaterial> materials = materialRepository.findAll();
            for (SysMaterial m : materials) {
                if (StringUtils.isBlank(m.getImg())) {
                    continue;
                }

                String pre = "group1/M00/05/05/materials/";
                String path = pre + m.getImg();
                m.setImg(path);
            }
            materialRepository.saveAll(materials);
        }
    }

    /**
     * 更新原料中的字段，可删除
     *
     * @author loki
     * @date 2020/10/23 13:52
     */
    public void updatePractice() {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.like("practice", "description"));

        Specification<SysDish> spec = builder.build(true);
        PageRequest page = PageRequest.of(0, 1000);
        Page<SysDish> result = this.dishRepository.findAll(spec, page);
        if (result.getTotalPages() <= 0) {
            log.info("未查询到符合要求的数据");
            return;
        }

        List<SysDish> dishList = new ArrayList<>();
        for (SysDish dish : result.getContent()) {
            if (StringUtils.isNotBlank(dish.getPractice())) {
                dishList.add(dish);
                String s = "";
                List<DishPractice> dishPracticeList = JSONObject.parseArray(dish.getPractice(), DishPractice.class);
                if (!CollectionUtils.isEmpty(dishPracticeList)) {
                    StringBuilder sb = new StringBuilder();
                    for (DishPractice p : dishPracticeList) {
                        sb.append(p.getDescription()).append("<br/>");
                    }
                    s = sb.toString();
                    log.info("转换之前：{}", s);

                    int index = s.lastIndexOf("<br/>");
                    s = s.substring(0, index);
                    log.info("转换之后：{}", s);
                }
                dish.setPractice(s);
            }
        }

        this.dishRepository.saveAll(dishList);
    }

    /**
     * 系统菜谱列表
     *
     * @author loki
     * @date 2020/08/22 11:08
     */
    //@Cacheable(cacheNames = RedisCacheUtil.SYS_DISH)
    public Object getDishPage(SysDishQueryReq req, Pageable page) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.eq("flavour.id", req.getFlavoursId()))
                .where(Criterion.eq("craft.id", req.getCraftId()))
                .where(Criterion.like("name", req.getDishName()));

        if (null != req.getCuisineId()) {
            Optional<SysCuisine> cuisineOptional = cuisineRepository.findById(req.getCuisineId());
            cuisineOptional.ifPresent(sysCuisine -> builder.where(Criterion.in("cuisines.id", Collections.singleton(sysCuisine.getId()))));
        }

        if (null != req.getCatalog()) {
            if (req.getCatalog() == 1) {
                builder.where(Criterion.eq("breakfast", true));
            } else if (req.getCatalog() == 2) {
                builder.where(Criterion.eq("lunch", true));
            } else if (req.getCatalog() == 3) {
                builder.where(Criterion.eq("dinner", true));
            } else {
                throw new BizException("", "请求参数异常");
            }
        }

        Specification<SysDish> spec = builder.build(true);
        PageRequest sort = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Order.desc("createdTime"), Sort.Order.desc("name")));
        Page<SysDish> result = this.dishRepository.findAll(spec, sort);

        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        return PageUtil.toPageDTO(this.buildDishList(result.getContent()), result);
    }

    /**
     * 自动构建禁忌食谱
     */
    @Transactional(rollbackFor = Exception.class)
    public void buildTabooRecipes(){
        //查询禁忌疾病分类
        SysMasterCuisine sysMasterCuisine = this.masterCuisineRepository.findAllByCode("meals_taboo");
        if(sysMasterCuisine == null){
            sysMasterCuisine = new SysMasterCuisine();
            sysMasterCuisine.setId(UUIDUtil.generateUuid());
            sysMasterCuisine.setCode("meals_taboo");
            sysMasterCuisine.setName("疾病禁忌食谱");
            sysMasterCuisine.setCreatedTime(LocalDateTime.now());
            sysMasterCuisine.setUpdatedTime(LocalDateTime.now());
            this.masterCuisineRepository.saveAndFlush(sysMasterCuisine);
            String[] names = {"慢性胃炎","胃及十二指肠溃疡","慢性肠炎","痔疮","慢性支气管炎","哮喘","慢性肺炎","头痛","神经衰弱",
                    "冠心病","高血压","心律失常","高血脂症","糖尿病","痛风","甲亢","贫血","慢性前列腺炎","慢性肾炎","尿路结石","慢性盆腔炎","子宫脱垂",
                    "更年期综合征","骨质疏松","肩周炎","风湿关节炎","慢性咽炎","皮肤瘙痒","痤疮","湿疹"};
            String[] codes = {"nol_manxinweiyan","nol_weichangkuiyang","nol_manxingchangyan","nol_zhichuang","nol_zhiqiguanyan","nol_xiaochuan","nol_manxingfeiyan","nol_toutong","nol_shenjingshuairuo","nol_guanxinbing","nol_gaoxueya","nol_xinlvshichang","nol_gaoxuezhi","nol_tangniaobing","nol_tongfeng","nol_jiakang","nol_pinxue","nol_manxingqianliexianyan","nol_manxingshenyan","nol_niaolujieshi","nol_manxingpenqiangyan","nol_zigongtuochui","nol_gengnianqi","nol_guzhishusong","nol_jianzhouyan","nol_fengshiguanjieyan","nol_manxingyanyan","nol_pifusaoyang","nol_cuochuang","nol_shizheng"};
            //禁忌疾病关键字
            String[] tabooDishes = {"油条/煎饼/芸豆/肥肉/螃蟹/牛奶/白酒/咖啡/浓茶/炸薯条/冰激凌/辣椒",
                    "糯米/螃蟹/红薯/芹菜/韭菜/柠檬/山楂/苹果/橘子/李子/巧克力/冰激凌/白酒/咖啡/浓茶/醋/辣椒/大蒜",
                    "排骨/肥肉/红薯/土豆/白萝卜/西瓜/黄瓜/香蕉/杏仁/牛奶/蜂蜜/白酒",
                    "油条/羊肉/螃蟹/芥菜/莼菜/荔枝/桂圆/榴莲/大葱/生姜/榨菜/辣椒",
                    "糯米/肥肉/香肠/螃蟹/虾/丝瓜/石榴/荸荠/白酒/辣椒/桂皮/薄荷",
                    "黄豆/肥肉/带鱼/螃蟹/虾/红薯/韭菜/冰激凌/白酒/大葱/蒜/辣椒",
                    "油条/肥肉/石榴/桃子/杏/李子/咖啡/浓茶/可乐/冰激凌/辣椒/芥末",
                    "肥肉/香肠/黄瓜/莼菜/芹菜/香蕉/西瓜/苹果/松花蛋/白酒/浓茶/冰激凌",
                    "肥肉/烤肉/香肠/白萝卜/蚕豆/白糖/浓茶/咖啡/白酒/辣椒/生姜/大蒜",
                    "肥肉/猪肝/鹅肉/螃蟹/墨鱼/咖啡/浓茶/白酒/糖果/蛋黄/奶油/猪油",
                    "方便面/肥肉/牛髓/羊肉/狗肉/鸡肉/火腿/雪里蕻/椰子/榴莲/柚子/松花蛋/薯片/苏打饼干/白酒/浓茶/巧克力/牛油",
                    "鸡肉/肥肉/螃蟹/鱼子/包菜/韭菜/洋葱/浓茶/咖啡/蛋黄/牛油/辣椒",
                    "糯米/锅巴/猪脑/猪肝/鸡肉/腊肉/鲍鱼/鱼子/榴莲/柚子/椰子/鸭蛋/鹌鹑蛋/开心果/白酒/咖啡/猪油/比萨",
                    "糯米/油条/肥肉/猪肝/鲍鱼/红薯/土豆/雪里蕻/芋头/韭菜/柿子/甘蔗/荔枝/开心果/薯片/白酒/牛油/蜂蜜",
                    "豆腐/狗肉/羊肉/鹅肉/鸡汤/螃蟹/虾/桂圆/杏/白酒/啤酒/胡椒",
                    "肥肉/羊肉/狗肉/鹅肉/带鱼/海带/紫菜/白酒/猪油/辣椒/大蒜/人参",
                    "馒头/海藻/马蹄/白酒/浓茶/冰激凌",
                    "狗肉/羊肉/韭菜/白酒/冰激凌/辣椒",
                    "黄豆/肥肉/红薯/韭菜/芹菜/香蕉/蘑菇/白酒/咖啡/浓茶/榨菜/皮蛋",
                    "黄豆/羊肉/芹菜/青椒/菠菜/葡萄/白酒/红茶/咖啡/牛奶/奶油/巧克力",
                    "油条/肥肉/羊肉/狗肉/鹅肉/田螺/螃蟹/白酒/浓茶/咖啡/冰激凌/辣椒",
                    "蚌肉/田螺/螃蟹/甲鱼/冬瓜/黄瓜/苦瓜/西瓜/白萝卜/竹笋/浓茶/辣椒",
                    "爆米花/炒花生/炒蚕豆/炒黄豆/浓茶/咖啡/白酒/辣椒/胡椒/葱/蒜/芥末",
                    "猪肝/白糖/咸菜/白酒/咖啡/可乐",
                    "油条/豆腐/绿豆/肥肉/鹅肉/海带/红薯/香蕉/柿子/西瓜/奶油/冰激凌",
                    "肥肉/牛肉/鹅肉/猪肝/带鱼/螃蟹/虾/海带/柿子/咖啡/奶油/冰激凌",
                    "油条/肥肉/白酒/浓茶/咖啡/薯片/葵花子/冰激凌/生姜/大葱/大蒜/辣椒",
                    "糯米/羊肉/驴肉/鸡肉/鹅肉/虾/螃蟹/带鱼/鲥鱼/鲢鱼/香菜/茄子",
                    "肥肉/羊肉/咸肉/虾/螃蟹/带鱼/榴莲/芒果/白酒/咖啡/浓茶/辣椒",
                    "糯米/羊肉/鸡肉/带鱼/鲤鱼/黄鳝/虾/螃蟹/茄子/芥菜/樱桃/荔枝/鸡蛋/鸭蛋/大葱"
            };
            for (int i = 0; i < codes.length; i++) {
                String code = codes[i];
                String name = names[i];
                String tabooDishe = tabooDishes[i];
                SysCuisine sysCuisine = this.cuisineRepository.queryByCode(code);
                if(sysCuisine == null){
                    sysCuisine = new SysCuisine();
                    sysCuisine.setId(UUIDUtil.generateUuid());
                    sysCuisine.setCreatedTime(LocalDateTime.now());
                    sysCuisine.setUpdatedTime(LocalDateTime.now());
                }
                sysCuisine.setCode(code);
                sysCuisine.setName(name);
                sysCuisine.setMasterId(sysMasterCuisine.getId());
                this.cuisineRepository.saveAndFlush(sysCuisine);

                List<Criterion> criteria = Arrays.stream(tabooDishe.split("/")).filter(StringUtils::isNotBlank).map(item -> Criterion.like("name", item)).collect(Collectors.toList());
                Specification<SysDish> specification = SpecificationBuilder.builder()
                        .whereByOr(criteria)
                        .build();
                List<SysDish> sysDishes = this.dishRepository.findAll(specification);
                for (SysDish sysDish : sysDishes) {
                    SysDishCuisine oldDishCuisine = this.dishCuisineRepository.queryByDishAndCuisine(sysDish, sysCuisine);
                    if(oldDishCuisine == null ) {
                        SysDishCuisine dishCuisine = new SysDishCuisine();
                        dishCuisine.setDish(sysDish);
                        dishCuisine.setCuisine(sysCuisine);
                        dishCuisine.setCreatedTime(LocalDateTime.now());
                        dishCuisine.setUpdatedTime(LocalDateTime.now());
                        this.dishCuisineRepository.saveAndFlush(dishCuisine);
                    }
                }
            }
        }
        log.info("执行获取禁忌食谱完成");
    }

    /**
     * 添加菜谱
     *
     * @author loki
     * @date 2020/08/22 14:52
     */
    @CacheEvict(cacheNames = {RedisCacheUtil.SYS_DISH, RedisCacheUtil.SYS_DISH_10}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Object addDish(SysDishAddReq req) {
        SysDish dish = new SysDish();

        //计算营养价值
        NutritionDTO nutrition = dishBiz.generateNutrition(req.getMaterials());
        if (null != nutrition) {
            BeanUtils.copyProperties(nutrition, dish);
        }

        dish.setName(req.getName());
        dish.setImg(req.getImg());
        dish.setPracticeTips(req.getPracticeTips());
        dish.setHealthTips(req.getHealthTips());
        dish.setDescription(req.getIntro());
        dish.setTaste(req.getTaste());
        dish.setIntro(req.getIntro());
        dish.setPractice(req.getPractice());
        dish.setRestriction(req.getRestriction());

        if (StringUtils.isNotBlank(req.getFlavoursId())) {
            Optional<SysFlavour> flavourOptional = flavourRepository.findById(req.getFlavoursId());
            if (flavourOptional.isPresent()) {
                dish.setFlavour(flavourOptional.get());
            }
        }

        if (StringUtils.isNotBlank(req.getCraftId())) {
            Optional<SysCraft> craftOptional = craftRepository.findById(req.getCraftId());
            if (craftOptional.isPresent()) {
                dish.setCraft(craftOptional.get());
            }
        }

        dish.setBreakfast(req.getBreakfast());
        dish.setLunch(req.getLunch());
        dish.setDinner(req.getDinner());
        dish.setCreatedTime(LocalDateTime.now());
        dish.setUpdatedTime(LocalDateTime.now());

        dish = this.dishRepository.save(dish);

        if (StringUtils.isNotBlank(req.getCuisines())) {
            List<String> cuisines = Arrays.asList(req.getCuisines().split(","));
            List<SysCuisine> cuisineList = cuisineRepository.findAllById(cuisines);

            List<SysDishCuisine> dishCuisineList = new ArrayList<>();
            SysDishCuisine dishCuisine;
            for (SysCuisine cuisine : cuisineList) {
                dishCuisine = new SysDishCuisine();
                dishCuisine.setDish(dish);
                dishCuisine.setCuisine(cuisine);
                dishCuisine.setCreatedTime(LocalDateTime.now());
                dishCuisine.setUpdatedTime(LocalDateTime.now());
                dishCuisineList.add(dishCuisine);
            }

            this.dishCuisineRepository.saveAll(dishCuisineList);
        }

        if (StringUtils.isNotBlank(req.getMaterials())) {
            List<MessProductMaterialDTO> materialList = JSON.parseObject(req.getMaterials(), new TypeReference<List<MessProductMaterialDTO>>() {
            });

            if (!CollectionUtils.isEmpty(materialList)) {
                SysDishMaterial dishMaterial;
                SysMaterial sysMaterial;
                for (MessProductMaterialDTO materialDTO : materialList) {
                    sysMaterial = this.materialRepository.findById(materialDTO.getId()).orElse(null);
                    if (sysMaterial == null) {
                        throw new BizException("", "原料不存在");
                    }

                    dishMaterial = new SysDishMaterial();
                    dishMaterial.setDish(dish);
                    dishMaterial.setMaterial(sysMaterial);
                    dishMaterial.setAmount(new BigDecimal(materialDTO.getAmount()));
                    dishMaterial.setMaster(materialDTO.getMaster());
                    dishMaterial.setCreatedTime(LocalDateTime.now());
                    dishMaterial.setUpdatedTime(LocalDateTime.now());
                    this.dishMaterialRepository.save(dishMaterial);
                }
            }
        }

        return dish;
    }


    /**
     * 编辑菜谱
     *
     * @author loki
     * @date 2020/08/22 14:52
     */
    @CacheEvict(cacheNames = {RedisCacheUtil.SYS_DISH, RedisCacheUtil.SYS_DISH_10}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Object editDish(SysDishEditReq req) {
        SysDish dish = this.dishRepository.findById(req.getId()).orElse(null);
        if (null == dish) {
            throw new BizException("", "菜谱不存在");
        }

        //计算营养价值
        NutritionDTO nutrition = dishBiz.generateNutrition(req.getMaterials());
        if (null != nutrition) {
            BeanUtils.copyProperties(nutrition, dish);
        }

        dish.setName(req.getName());
        dish.setImg(req.getImg());
        dish.setPracticeTips(req.getPracticeTips());
        dish.setHealthTips(req.getHealthTips());
        dish.setDescription(req.getIntro());
        dish.setTaste(req.getTaste());
        dish.setPractice(req.getPractice());
        dish.setIntro(req.getIntro());
        dish.setRestriction(req.getRestriction());

        if (StringUtils.isNotBlank(req.getFlavoursId())) {
            Optional<SysFlavour> flavourOptional = flavourRepository.findById(req.getFlavoursId());
            if (flavourOptional.isPresent()) {
                dish.setFlavour(flavourOptional.get());
            }
        }

        if (StringUtils.isNotBlank(req.getCraftId())) {
            Optional<SysCraft> craftOptional = craftRepository.findById(req.getCraftId());
            if (craftOptional.isPresent()) {
                dish.setCraft(craftOptional.get());
            }
        }

        dish.setBreakfast(req.getBreakfast());
        dish.setLunch(req.getLunch());
        dish.setDinner(req.getDinner());
        dish.setUpdatedTime(LocalDateTime.now());

        dish = this.dishRepository.save(dish);

        //删除菜谱对应类型
        this.dishCuisineRepository.deleteAllByDish(dish);

        if (StringUtils.isNotBlank(req.getCuisines())) {
            List<String> cuisines = Arrays.asList(req.getCuisines().split(","));
            List<SysCuisine> cuisineList = cuisineRepository.findAllById(cuisines);

            List<SysDishCuisine> dishCuisineList = new ArrayList<>();
            SysDishCuisine dishCuisine;
            for (SysCuisine cuisine : cuisineList) {
                dishCuisine = new SysDishCuisine();
                dishCuisine.setDish(dish);
                dishCuisine.setCuisine(cuisine);
                dishCuisine.setCreatedTime(LocalDateTime.now());
                dishCuisine.setUpdatedTime(LocalDateTime.now());
                dishCuisineList.add(dishCuisine);
            }

            this.dishCuisineRepository.saveAll(dishCuisineList);
        }

        //删除改菜谱原材料信息
        this.dishMaterialRepository.deleteAllByDish(dish);

        if (StringUtils.isNotBlank(req.getMaterials())) {
            List<MessProductMaterialDTO> materialList = JSON.parseObject(req.getMaterials(), new TypeReference<List<MessProductMaterialDTO>>() {});

            if (!CollectionUtils.isEmpty(materialList)) {
                SysDishMaterial dishMaterial;
                SysMaterial sysMaterial;
                for (MessProductMaterialDTO materialDTO : materialList) {
                    sysMaterial = this.materialRepository.findById(materialDTO.getId()).orElse(null);
                    if (sysMaterial == null) {
                        throw new BizException("", "原料不存在");
                    }

                    dishMaterial = new SysDishMaterial();
                    dishMaterial.setDish(dish);
                    dishMaterial.setMaterial(sysMaterial);
                    dishMaterial.setAmount(new BigDecimal(materialDTO.getAmount()));
                    dishMaterial.setMaster(materialDTO.getMaster());
                    dishMaterial.setCreatedTime(LocalDateTime.now());
                    dishMaterial.setUpdatedTime(LocalDateTime.now());
                    this.dishMaterialRepository.save(dishMaterial);
                }
            }
        }
        return dish;
    }

    /**
     * 删除菜谱
     *
     * @author loki
     * @date 2020/08/22 15:13
     */
    @CacheEvict(cacheNames = {RedisCacheUtil.SYS_DISH, RedisCacheUtil.SYS_DISH_10}, allEntries = true)
    public Object delDish(String[] ids) {
        if (ids.length <= 0) {
            throw new BizException("", "请选择需要删除的项");
        }

        List<SysDish> dishList = new ArrayList<>();
        SysDish dish;
        for (String id : ids) {
            dish = this.dishRepository.findById(id).orElseThrow(() -> new BizException("", "食谱不存在"));
            dishList.add(dish);
        }

        this.dishRepository.deleteAll(dishList);
        return dishList;
    }

    /**
     * 系统菜谱列表
     *
     * @author loki
     * @date 2020/08/22 11:08
     */
    @Cacheable(cacheNames = RedisCacheUtil.SYS_MATERIAL)
    public Object getDishMaterialPage(SysMaterialQueryReq req, Pageable page) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.eq("materialType.id", req.getMaterialTypeId()))
                .whereByOr(Criterion.like("name", req.getKeyword()), Criterion.like("alias", req.getKeyword()));


        Specification<SysMaterial> spec = builder.build();
        Page<SysMaterial> result = this.materialRepository.findAll(spec, page);

        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<SysMaterial> materialList = result.getContent();
        SysMaterialDTO materialDTO;
        List<SysMaterialDTO> materialDTOList = new ArrayList<>(materialList.size());
        for (SysMaterial material : materialList) {
            materialDTO = new SysMaterialDTO();
            BeanUtils.copyProperties(material, materialDTO);

            if (null != material.getMaterialType()) {
                materialDTO.setMaterialTypeId(material.getMaterialType().getId());
                materialDTO.setMaterialTypeName(material.getMaterialType().getName());
            }
            materialDTOList.add(materialDTO);
        }

        return PageUtil.toPageDTO(materialDTOList, result);
    }

    /**
     * 添加菜谱
     *
     * @author loki
     * @date 2020/08/22 14:52
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_MATERIAL, RedisCacheUtil.SYS_MATERIAL_10}, allEntries = true)
    public Object addDishMaterial(SysMaterialAddReq req) {
        SysMaterial material = new SysMaterial();
        BeanUtils.copyProperties(req, material);
        material.setCreatedTime(LocalDateTime.now());
        material.setUpdatedTime(LocalDateTime.now());

        if (StringUtils.isNotBlank(req.getMaterialTypeId())) {
            SysMaterialType materialType = materialTypeRepository.findById(req.getMaterialTypeId()).orElse(null);
            if (null == materialType) {
                throw new BizException("", "原料类型不存在");
            }
            material.setMaterialType(materialType);
        }

        return this.materialRepository.save(material);
    }


    /**
     * 编辑菜谱
     *
     * @author loki
     * @date 2020/08/22 14:52
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_MATERIAL, RedisCacheUtil.SYS_MATERIAL_10}, allEntries = true)
    public Object editDishMaterial(SysMaterialEditReq req) {
        SysMaterial material = this.materialRepository.findById(req.getId()).orElse(null);
        if (null == material) {
            throw new BizException("", "原料不存在");
        }

        BeanUtils.copyProperties(req, material);

        if (StringUtils.isNotBlank(req.getMaterialTypeId())) {
            SysMaterialType materialType = materialTypeRepository.findById(req.getMaterialTypeId()).orElse(null);
            if (null == materialType) {
                throw new BizException("", "原料类型不存在");
            }
            material.setMaterialType(materialType);
        }

        return this.materialRepository.save(material);
    }

    /**
     * 删除菜谱
     *
     * @author loki
     * @date 2020/08/22 15:13
     */
    @CacheEvict(value = {RedisCacheUtil.SYS_MATERIAL, RedisCacheUtil.SYS_MATERIAL_10}, allEntries = true)
    public Object delDishMaterial(String[] ids) {
        if (ids.length <= 0) {
            throw new BizException("", "请选择需要删除的项");
        }

        List<SysMaterial> materialList = new ArrayList<>();
        SysMaterial material;
        for (String id : ids) {
            material = this.materialRepository.findById(id).orElseThrow(() -> new BizException("", "原材料不存在"));


            List<SysDishMaterial> dishMaterialList = this.dishMaterialRepository.queryByMaterial(material);
            if (!CollectionUtils.isEmpty(dishMaterialList)) {
                throw new BizException("", "原材料已被使用，不能删除");
            }

            materialList.add(material);
        }
        this.materialRepository.deleteAll(materialList);

        return materialList;
    }

    /**
     * 获取原料类型列表
     *
     * @author loki
     * @date 2020/08/22 20:01
     */
    public Object getMaterialTypeList() {
        return materialTypeRepository.findAll();
    }

    /**
     * 查询营养元素
     * @param id
     * @return
     */
    public NutritionNone findById(String id){
        if(id == null){
            throw new BizException("", "DISH ID不能为空");
        }
        return this.dishRepository.findById(id).orElse(null);
    }

    public FeignDishDto findDtoById(String id){
        if(id == null){
            throw new BizException("", "DISH ID不能为空");
        }
        Optional<SysDish> sysDishOptional = this.dishRepository.findById(id);

        return sysDishOptional.map(item ->
                new FeignDishDto()
                        .setId(item.getId())
                        .setName(item.getName())
                        .setImg(item.getImg())
        ).orElse(null);
    }
}