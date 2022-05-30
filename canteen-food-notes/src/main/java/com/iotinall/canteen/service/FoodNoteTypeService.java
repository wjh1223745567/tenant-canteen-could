package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeAddReq;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeDTO;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeEditReq;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeQueryCriteria;
import com.iotinall.canteen.entity.FoodNoteType;
import com.iotinall.canteen.repository.FoodNoteRepository;
import com.iotinall.canteen.repository.FoodNoteTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 美食笔记类型Service
 * @author: JoeLau
 * @time: 2021年07月06日 19:51:16
 */

@Slf4j
@Service
public class FoodNoteTypeService {
    @Resource
    private FoodNoteTypeRepository foodNoteTypeRepository;
    @Resource
    private FoodNoteRepository foodNoteRepository;

    /**
     * @return
     * @Author JoeLau
     * @Description 查询美食笔记类型分页
     * @Date 2021/7/7  10:30
     * @Param
     */
    public PageDTO<FoodNoteTypeDTO> page(FoodNoteTypeQueryCriteria criteria, Pageable pageable) {
        Specification<FoodNoteType> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("status", criteria.getStatus()),
                        Criterion.like("name", criteria.getKeywords()))
                .build();
        Page<FoodNoteType> page = foodNoteTypeRepository.findAll(specification, pageable);
        List<FoodNoteTypeDTO> list = page.getContent().stream().map(type -> {
            FoodNoteTypeDTO foodNoteTypeDTO = new FoodNoteTypeDTO();
            BeanUtils.copyProperties(type, foodNoteTypeDTO);
            foodNoteTypeDTO.setNoteNumber(this.foodNoteRepository.countByFoodNoteTypeAndStatus(type, true));
            return foodNoteTypeDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 获取美食笔记类型列表（不包括禁用的）
     * @Date 2021/7/7  10:29
     * @Param
     */
    public List<FoodNoteTypeDTO> getAll() {
        List<FoodNoteType> typeList = this.foodNoteTypeRepository.findAllByStatus(true);
        List<FoodNoteTypeDTO> typeDTOList = typeList.stream().map(type -> {
            FoodNoteTypeDTO foodNoteTypeDTO = new FoodNoteTypeDTO();
            BeanUtils.copyProperties(type, foodNoteTypeDTO);
            foodNoteTypeDTO.setNoteNumber(this.foodNoteRepository.countByFoodNoteTypeAndStatus(type, true));
            return foodNoteTypeDTO;
        }).collect(Collectors.toList());
        return typeDTOList;
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 添加一个新的美食笔记类型
     * @Date 2021/7/7  10:29
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(FoodNoteTypeAddReq req) {
        FoodNoteType type = this.foodNoteTypeRepository.findByName(req.getName());
        if (null != type) {
            throw new BizException("已存在同名的美食笔记类型");
        }
        FoodNoteType foodNoteType = new FoodNoteType();
        foodNoteType.setName(req.getName());
        foodNoteType.setStatus(req.getStatus());
        if (null != req.getRemark()) {
            foodNoteType.setRemark(req.getRemark());
        }
        this.foodNoteTypeRepository.save(foodNoteType);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 编辑一个美食笔记类型
     * @Date 2021/7/7  10:57
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(FoodNoteTypeEditReq req) {
        FoodNoteType foodNoteType = this.foodNoteTypeRepository.findById(req.getId()).orElseThrow(() -> new BizException("未找到该美食笔记类型"));
        FoodNoteType type = this.foodNoteTypeRepository.findByName(req.getName());
        if (null != type && !type.equals(foodNoteType)) {
            throw new BizException("已存在同名的美食笔记类型");
        }
        foodNoteType.setName(req.getName());
        foodNoteType.setStatus(req.getStatus());
        if (null != req.getRemark()) {
            foodNoteType.setRemark(req.getRemark());
        }
        this.foodNoteTypeRepository.save(foodNoteType);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 删除一个美食笔记类型
     * @Date 2021/7/7  11:11
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            FoodNoteType foodNoteType = this.foodNoteTypeRepository.findById(id).orElseThrow(() -> new BizException("未找到该美食笔记类型"));
            if (this.foodNoteRepository.existsByFoodNoteType(foodNoteType)) {
                throw new BizException("该类型下有美食笔记，无法删除");
            }
            this.foodNoteTypeRepository.delete(foodNoteType);
        }
    }

}
