package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSONArray;
import com.iotinall.canteen.dto.escp.BalanceListDTO;
import com.iotinall.canteen.dto.escp.BalanceListReq;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.entity.EmployeeWallet;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.repository.EmployeeWalletRepository;
import com.iotinall.canteen.utils.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 新开普同步人员余额
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Slf4j
@Service
public class SyncBalanceService extends BaseSyncService {
    @Resource
    private EmployeeWalletRepository employeeWalletRepository;

    /**
     * 同步人员余额
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    private void sync() {
        BalanceListReq req = new BalanceListReq()
                .setCount(10000)
                .setVer(1000L);

        RespDTO result = execute(escpProperty.getApiUrl(API_BALANCE), req);

        String data = decode(result.getData());
        if (StringUtils.isNotBlank(data)) {
            List<BalanceListDTO> escpBalanceList = JSONArray.parseArray(data, BalanceListDTO.class);
            if (!CollectionUtils.isEmpty(escpBalanceList)) {
                EmployeeWallet wallet;
                OrgEmployee employee;
                for (BalanceListDTO escpBalance : escpBalanceList) {
                    employee = orgEmployeeRepository.queryByPersonCodeAndDeletedFalse(escpBalance.getIdSerial());
                    if (null == employee) {
                        continue;
                    }

                    BigDecimal balance = MoneyUtil.fen2Yuan(escpBalance.getTotalOddFare());
                    log.info("余额同步，人员：{}，金额：{}", employee.getName(), balance);
                    if (null == employee.getWallet()) {
                        wallet = new EmployeeWallet(balance);
                        wallet.setPayPassword(passwordEncoder.encode("123456"));
                        this.employeeWalletRepository.save(wallet);

                        employee.setWallet(wallet);
                        this.employeeWalletRepository.save(wallet);
                    } else {
                        this.employeeWalletRepository.updateBalance(employee.getWallet().getId(), balance);
                    }
                }
            }
        }
    }
}
