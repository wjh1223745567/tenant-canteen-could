package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.entity.EmployeeWallet;
import com.iotinall.canteen.repository.EmployeeWalletRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 钱包
 *
 * @author loki
 * @date 2020/04/27 16:21
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class EmployeeWalletService {
    @Resource
    EmployeeWalletRepository walletRepository;

    public EmployeeWallet init() {
        EmployeeWallet wallet = new EmployeeWallet(BigDecimal.ZERO);
        return this.walletRepository.save(wallet);
    }

    public void addBalance(EmployeeWallet wallet, BigDecimal balance) {
        if (null == wallet) {
            wallet = new EmployeeWallet(BigDecimal.ZERO);
            wallet = this.walletRepository.save(wallet);
        }
        walletRepository.addBalance(wallet.getId(), balance);
    }

    public Integer subtractBalance(EmployeeWallet wallet, BigDecimal balance) {
        if (null == wallet) {
            wallet = new EmployeeWallet(BigDecimal.ZERO);
            wallet = this.walletRepository.save(wallet);
        }
        return walletRepository.subtractBalance(wallet.getId(), balance);
    }

    public void updatePayPassword(String oldPass, String newPass, EmployeeWallet wallet) {
        if (StringUtils.isNotBlank(wallet.getPayPassword()) && StringUtils.isBlank(oldPass)) {
            throw new BizException("", "请输入旧密码");
        }
        PasswordEncoder passwordEncoder = SpringContextUtil.getBean(PasswordEncoder.class);
        if (StringUtils.isNotBlank(wallet.getPayPassword()) && !passwordEncoder.matches(oldPass, wallet.getPayPassword())) {
            throw new BizException("", "旧密码错误，请重试");
        }
        wallet.setPayPassword(passwordEncoder.encode(newPass));
        this.walletRepository.save(wallet);
    }
}
