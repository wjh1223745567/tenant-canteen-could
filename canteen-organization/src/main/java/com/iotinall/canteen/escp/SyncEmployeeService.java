package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSONArray;
import com.iotinall.canteen.constant.PersonalType;
import com.iotinall.canteen.dto.escp.EmployeeListDTO;
import com.iotinall.canteen.dto.escp.EmployeeListReq;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.entity.Org;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.repository.OrgRepository;
import com.iotinall.canteen.service.EmployeeWalletService;
import com.iotinall.canteen.utils.LocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新开普同步人员
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Service
public class SyncEmployeeService extends BaseSyncService {
    @Resource
    private EmployeeWalletService employeeWalletService;
    @Resource
    private OrgRepository orgRepository;

    /**
     * 同步人员
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    private void sync() {
        EmployeeListReq req = new EmployeeListReq()
                .setCount(10000)
                .setType(2)
                .setVer(1000L);

        RespDTO result = execute(escpProperty.getApiUrl(API_EMP), req);

        String data = decode(result.getData());
        if (StringUtils.isNotBlank(data)) {
            List<EmployeeListDTO> employeeList = JSONArray.parseArray(data, EmployeeListDTO.class);
            if (!CollectionUtils.isEmpty(employeeList)) {
                OrgEmployee employee;
                for (EmployeeListDTO escpEmployee : employeeList) {
                    employee = new OrgEmployee();
                    employee.setWallet(employeeWalletService.init());
                    employee.setDeleted(false);
                    employee.setEntryDate(LocalDateUtil.strToLocalDate(escpEmployee.getInSchoolDate()));
                    employee.setGender(escpEmployee.getSex());
                    employee.setIdNo(escpEmployee.getIdSerial2());
                    employee.setPersonnelType(convertPersonalType(escpEmployee.getZipCode()));
                    employee.setPwd(passwordEncoder.encode("123456"));
                    employee.setName(escpEmployee.getUsername());
                    employee.setTelephone(escpEmployee.getTel());
                    employee.setMobile(escpEmployee.getTel());
                    employee.setEnabled(true);

                    //默认角色
//                    employee.setRoles();

                    //部门
                    if (null != escpEmployee.getOrganCode()) {
                        Org org = this.orgRepository.findByOrgCode(escpEmployee.getOrganCode());
                        if (null != org) {
                            employee.setOrg(org);
                        }
                    }
                }
            }
        }
    }

    /**
     * 转换人员类型
     *
     * @author loki
     * @date 2021/6/22 17:48
     **/
    private Integer convertPersonalType(Integer personalType) {
        return PersonalType.WORKER.getCode();
    }
}
