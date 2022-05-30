package com.iotinall.canteen.service;

import com.alibaba.excel.EasyExcel;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.orgemployee.OrgEmployeeAddReq;
import com.iotinall.canteen.dto.orgemployee.OrgMemberExcelDto;
import com.iotinall.canteen.entity.Org;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.excellistener.OrgMemberExcelListener;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.utils.StringUtils;
import com.iotinall.canteen.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * excel导入
 */

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class ExcelService {

    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;

    @Resource
    private OrgEmployeeService orgEmployeeService;

    @Resource
    private OrgService orgService;

    /**
     * @param file
     */
    public void addMemberFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), OrgMemberExcelDto.class,
                    new OrgMemberExcelListener()).sheet().headRowNumber(1).autoTrim(true).doRead();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }


    /**
     * 添加人员
     *
     * @param excelDtoList
     */
    public void addMember(List<OrgMemberExcelDto> excelDtoList) {
        for (OrgMemberExcelDto orgMemberExcelDto : excelDtoList) {
            OrgEmployeeAddReq addReq = new OrgEmployeeAddReq();
            addReq.setName(orgMemberExcelDto.getName());
            addReq.setCardNo(UUIDUtil.generateShortUuid());
            addReq.setEnabled(Boolean.TRUE);
            addReq.setEntryDate(orgMemberExcelDto.getEntryDate());
            addReq.setIdNo(orgMemberExcelDto.getIdNo());
            addReq.setRole(orgMemberExcelDto.getRole());
            addReq.setMobile(orgMemberExcelDto.getMobile());
            addReq.setPwd("666888");

            addReq.setGender(StringUtils.getGender(orgMemberExcelDto.getIdNo()));
            //后厨人员
            addReq.setPersonnelType(1);

            Org org = this.orgService.getOrgByName(orgMemberExcelDto.getOrgName(), orgMemberExcelDto.getSubOrgName());
            addReq.setOrgId(org.getId());
            OrgEmployee orgEmployee = this.orgEmployeeService.add(addReq);
            this.orgEmployeeRepository.saveAndFlush(orgEmployee);
        }
    }
}
