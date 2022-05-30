package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.personalrecords.OrgEmployeePersonalRecordsEmpReq;
import com.iotinall.canteen.dto.personalrecords.OrgEmployeePersonalRecordsReq;
import com.iotinall.canteen.dto.personalrecords.OrgEmployeePersonalRecordsResp;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.OrgEmployeePersonalRecords;
import com.iotinall.canteen.repository.OrgEmployeePersonalRecordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人员档案信息
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class OrgEmployeePersonalRecordsService {

    @Resource
    private OrgEmployeePersonalRecordsRepository recordsRepository;

    /**
     * 查询人员档案信息
     *
     * @param employeeId
     * @return
     */
    public List<OrgEmployeePersonalRecordsResp> findByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            throw new BizException("", "人员ID不能为空");
        }
        return this.recordsRepository.findAllByEmployeeId(employeeId).stream().map(item -> new OrgEmployeePersonalRecordsResp()
                .setId(item.getId())
                .setName(item.getName())
                .setUrl(item.getUrl())
                .setHaveDate(item.getHaveDate())).collect(Collectors.toList());
    }

    /**
     * 修改档案信息
     * @param recordsEmpReq
     */
    public void update(OrgEmployeePersonalRecordsEmpReq recordsEmpReq){
        Long employeeId = recordsEmpReq.getEmployeeId();

        this.recordsRepository.deleteAllByEmployeeId(employeeId);
        for (OrgEmployeePersonalRecordsReq recordsReq : recordsEmpReq.getRecordsReqs()) {
            OrgEmployee employee = new OrgEmployee();
            employee.setId(employeeId);
            OrgEmployeePersonalRecords records = new OrgEmployeePersonalRecords()
                .setHaveDate(recordsReq.getHaveDate())
                .setName(recordsReq.getName())
                .setUrl(recordsReq.getUrl())
                .setEmployee(employee);
            records.setId(recordsReq.getId());
            this.recordsRepository.save(records);
        }
    }

}
