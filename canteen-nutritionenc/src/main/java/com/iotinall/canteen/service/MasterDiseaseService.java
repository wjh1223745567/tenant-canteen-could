package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.disease.ChronicDiseaseDTO;
import com.iotinall.canteen.dto.disease.MasterDiseaseDTO;
import com.iotinall.canteen.entity.ChronicDisease;
import com.iotinall.canteen.entity.MasterDisease;
import com.iotinall.canteen.repository.MasterDiseaseRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 慢性疾病逻辑处理类
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
@Service
public class MasterDiseaseService {
    @Resource
    private MasterDiseaseRepository masterDiseaseRepository;

    /**
     * 获取疾病列表
     *
     * @author loki
     * @date 2020/12/09 20:29
     */
    public Object getAppDiseaseList() {
        List<MasterDisease> masterDiseaseList = this.masterDiseaseRepository.findAll(SpecificationBuilder.builder().build(), Sort.by(Sort.Direction.ASC, "seq"));
        return this.convert(masterDiseaseList);
    }

    /**
     * 获取疾病列表
     *
     * @author loki
     * @date 2020/12/09 20:29
     */
    @Cacheable(value = "DISEASE_LIST")
    public Object getDiseaseList() {
        List<MasterDisease> masterDiseaseList = this.masterDiseaseRepository.findAll(Sort.by(Sort.Direction.ASC, "seq"));

        return this.convert(masterDiseaseList);
    }

    private Object convert(List<MasterDisease> masterDiseaseList) {
        if (CollectionUtils.isEmpty(masterDiseaseList)) {
            return Collections.EMPTY_LIST;
        }

        List<MasterDiseaseDTO> masterDiseaseDTOList = new ArrayList<>(masterDiseaseList.size());
        MasterDiseaseDTO masterDiseaseDTO;
        for (MasterDisease masterDisease : masterDiseaseList) {
            masterDiseaseDTO = new MasterDiseaseDTO();
            masterDiseaseDTOList.add(masterDiseaseDTO);

            masterDiseaseDTO.setId(masterDisease.getId());
            masterDiseaseDTO.setName(masterDisease.getName());
            masterDiseaseDTO.setFemale(masterDisease.getFemale());
            masterDiseaseDTO.setMale(masterDisease.getMale());
            if (!CollectionUtils.isEmpty(masterDisease.getChronicDisease())) {
                List<ChronicDiseaseDTO> chronicDiseaseDTOList = new ArrayList<>(masterDisease.getChronicDisease().size());
                ChronicDiseaseDTO chronicDiseaseDTO;
                for (ChronicDisease chronicDisease : masterDisease.getChronicDisease()) {
                    chronicDiseaseDTO = new ChronicDiseaseDTO();
                    chronicDiseaseDTOList.add(chronicDiseaseDTO);
                    chronicDiseaseDTO.setId(chronicDisease.getId());
                    chronicDiseaseDTO.setName(chronicDisease.getName());
                    chronicDiseaseDTO.setFemale(chronicDisease.getFemale());
                    chronicDiseaseDTO.setMale(chronicDisease.getMale());
                }
                masterDiseaseDTO.setChronicDisease(chronicDiseaseDTOList);
            }
        }

        return masterDiseaseDTOList;
    }
}
