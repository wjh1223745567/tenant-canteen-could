package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.constants.ErrCode;
import com.iotinall.canteen.dto.stature.StatureDTO;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailAddReq;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailDTO;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailEditReq;
import com.iotinall.canteen.entity.SysDictDetail;
import com.iotinall.canteen.repository.SysDictDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * sys_dict_detail ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-23 11:35:24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysDictDetailService {

    @Resource
    private SysDictDetailRepository sysDictDetailRepository;

    public List<SysDictDetailDTO> listAll(String groupCode) {
        Specification<SysDictDetail> spec = SpecificationBuilder.builder()
                .where(Criterion.in("groupCode", Arrays.asList(groupCode.split(","))))
                .build();
        List<SysDictDetail> dictDetails =  sysDictDetailRepository.findAll(spec, Sort.by(Sort.Order.asc("sort")));
        return dictDetails.stream().map(item -> {
            SysDictDetailDTO detailDTO = new SysDictDetailDTO();
            BeanUtils.copyProperties(item, detailDTO);
            return detailDTO;
        }).collect(Collectors.toList());
    }

    public SysDictDetail detail(Long id) {
        Optional<SysDictDetail> optional = sysDictDetailRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        return optional.get();
    }


    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisCacheUtil.SYS_DICT, allEntries = true, key = "#req.groupCode")
    public SysDictDetail add(SysDictDetailAddReq req) {
        SysDictDetail sysDictDetail = sysDictDetailRepository.findByGroupCodeAndValue(req.getGroupCode(), req.getValue());
        if (sysDictDetail != null) {
            throw new BizException(ErrCode.DICT_DETAIL_REPEAT, "名称重复");
        }
        sysDictDetail = new SysDictDetail();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, sysDictDetail);
        LocalDateTime now = LocalDateTime.now();
        sysDictDetail.setTyp(0);
        sysDictDetail.setCreateTime(now);
        sysDictDetail.setUpdateTime(now);
        sysDictDetailRepository.save(sysDictDetail);
        return sysDictDetail;
    }

    @Transactional(rollbackFor = Exception.class)
    public SysDictDetail add(String label, String groupCode) {
        SysDictDetail sysDictDetail = sysDictDetailRepository.findByGroupCodeAndValue(groupCode, label);
        if (sysDictDetail != null) {
            throw new BizException(ErrCode.DICT_DETAIL_REPEAT, "名称重复");
        }
        sysDictDetail = new SysDictDetail();
        sysDictDetail.setLabel(label);
        sysDictDetail.setValue(label);
        sysDictDetail.setGroupCode(groupCode);
        sysDictDetail.setSort(0);
        sysDictDetail.setTyp(0);
        LocalDateTime now = LocalDateTime.now();
        sysDictDetail.setCreateTime(now);
        sysDictDetail.setUpdateTime(now);
        sysDictDetailRepository.save(sysDictDetail);
        return sysDictDetail;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_DICT}, allEntries = true)
    public void update(SysDictDetailEditReq req) {
        Optional<SysDictDetail> optional = sysDictDetailRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        SysDictDetail sysDictDetail = sysDictDetailRepository.findByGroupCodeAndValue(req.getGroupCode(), req.getValue());
        if (sysDictDetail != null && !sysDictDetail.getId().equals(optional.get().getId())) {
            throw new BizException(ErrCode.DICT_DETAIL_REPEAT, "类型重复");
        }
        sysDictDetail = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, sysDictDetail);
        LocalDateTime now = LocalDateTime.now();
        sysDictDetail.setUpdateTime(now);
        sysDictDetailRepository.save(sysDictDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_DICT}, allEntries = true)
    public void delete(Long id) {
        Optional<SysDictDetail> optional = sysDictDetailRepository.findById(id);
        if (optional.isPresent()) {
            sysDictDetailRepository.deleteById(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_DICT}, allEntries = true)
    public void batchDelete(Long[] ids) {
        sysDictDetailRepository.deleteByIdIn(ids);
    }


    public List<SysDictDetail> listAll(List<String> groupCodes) {
        return sysDictDetailRepository.findByGroupCodeIn(groupCodes);
    }

    public List<StatureDTO> findOptions() {
        List<SysDictDetailDTO> statures = this.listAll(Constants.DICT_GROUP_NUTRITION_STATURE);
        List<SysDictDetailDTO> statureUnits = this.listAll(Constants.DICT_GROUP_NUTRITION_STATURE_UNIT);
        if (CollectionUtils.isEmpty(statures)) {
            return null;
        }

        List<StatureDTO> statureDTOList = new ArrayList<>(statures.size());
        StatureDTO statureDTO;
        SysDictDetailDTO dictDetailDTO;
        for (SysDictDetailDTO dict : statures) {
            if (null == dict) {
                continue;
            }
            statureDTO = new StatureDTO();
            statureDTOList.add(statureDTO);
            statureDTO.setCode(dict.getLabel());
            statureDTO.setName(dict.getValue());
            dictDetailDTO = statureUnits.stream().filter(item -> item.getLabel().equals(dict.getLabel() + Constants.STATURE_UNIT)).findFirst().orElse(null);
            statureDTO.setUnit(null == dictDetailDTO ? "" : dictDetailDTO.getValue());
        }
        return statureDTOList;
    }
}