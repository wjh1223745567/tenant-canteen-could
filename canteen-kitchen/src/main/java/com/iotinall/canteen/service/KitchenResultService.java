package com.iotinall.canteen.service;

import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constant.ItemAssessEnum;
import com.iotinall.canteen.dto.assessrecord.AssessItemDTO;
import com.iotinall.canteen.entity.KitchenResult;
import com.iotinall.canteen.repository.KitchenResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KitchenResultService {
    @Resource
    private KitchenResultRepository kitchenResultRepository;

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100L);

    public Map<String, AssessItemDTO> buildMap() {
        Map<String, AssessItemDTO> map = new LinkedHashMap<>(Constants.ITEM_ASSESS_ARRAY.length);
        for (String itemType : Constants.ITEM_ASSESS_ARRAY) {
            AssessItemDTO dto = new AssessItemDTO();
            dto.setQualifiedCount(0);
            dto.setTotalCount(0);
            dto.setAssessType(itemType);
            dto.setAssessTypeName(ItemAssessEnum.getByCode(itemType));
            dto.setRate(HUNDRED);
            map.put(dto.getAssessType(), dto);
        }
        return map;
    }

    public void addOrUpdate(Long recordId, String itemType, Integer state, LocalDateTime recordTime) {
        KitchenResult result = kitchenResultRepository.findByRecordIdAndItemType(recordId, itemType);
        if (result == null) {
            result = new KitchenResult();
        }
        LocalDate localDate = recordTime.toLocalDate();
        result.setItemType(itemType);
        result.setRecordId(recordId);
        result.setResultDate(localDate);
        result.setState(state);
        kitchenResultRepository.save(result);
    }

    public void delByRecordIdAndItemType(Long recordId, String itemType) {
        kitchenResultRepository.deleteByRecordIdAndItemType(recordId, itemType);
    }

    public void batchDeleteByRecordId(Long[] recordId, String itemType) {
        kitchenResultRepository.deleteByRecordIdInAndItemType(recordId, itemType);
    }
}
