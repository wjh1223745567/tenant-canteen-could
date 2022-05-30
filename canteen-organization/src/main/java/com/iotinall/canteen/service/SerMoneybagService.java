package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constant.*;
import com.iotinall.canteen.dto.bill.BillListDto;
import com.iotinall.canteen.dto.invest.InvestMoneyDto;
import com.iotinall.canteen.dto.invest.InvestMoneyReq;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.escp.EscpRechargeService;
import com.iotinall.canteen.repository.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务 钱包
 *
 * @author WJH
 * @date 2019/11/69:23
 */
@Slf4j
@Service
public class SerMoneybagService {

    @Resource
    private FinRechargeRecordRepository rechargeRecordRepository;

    @Resource
    private FinTransactionRecordRepository transactionRecordRepository;

    @Resource
    private FinRechargeOrderRepository orderRepository;

    @Resource
    private OrgEmployeeRepository employeeRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private EmployeeWalletService walletService;

    @Resource
    private FinRechargeOrderRepository finRechargeOrderRepository;

    @Resource
    private WalletBillDetailRepository walletBillDetailRepository;

    @Resource
    private EmployeeWalletRepository walletRepository;

    @Resource
    private FeignWxService feignWxService;

    @Resource
    private FinTransactionRecordRepository finTransactionRecordRepository;

    @Resource
    private EscpRechargeService escpRechargeService;

    @Resource
    private OrgEmployeeCardRepository orgEmployeeCardRepository;

    public BigDecimal getUserBalance(Long id) {
        OrgEmployee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            throw new BizException("", "用户不存在");
        }
        return employee.getWallet().getBalance();
    }

    /**
     * 充值接口
     *
     * @param req
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public InvestMoneyDto addBalance(InvestMoneyReq req) {
        OrgEmployee employee = this.employeeRepository.findById(SecurityUtils.getUserId()).orElseThrow(() -> new BizException("notFindUser", "未找到当前用户"));

        String openId = employee.getOpenid();
        PayTypeEnum payType = PayTypeEnum.WECHAT;
        String orderCode = UUID.randomUUID().toString().replace("-", "");
        //生成订单
        FinRechargeOrder order = new FinRechargeOrder()
                .setOrderNo(orderCode)
                .setCardNo(employee.getCardNo())
                .setEmployee(employee)
                .setPayType(payType)
                .setTransactionNo("")
                .setAmount(req.getAmount())
                .setStatus(OrderType.TOBEPAID);
        this.orderRepository.save(order);

        InvestMoneyDto moneyDto = new InvestMoneyDto();

        Map<String, String> data = feignWxService.doUnifiedOrder(orderCode, "续费充值", req.getAmount(), openId);
        moneyDto.setData(data);
        return moneyDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void tackOutWxPayPayPassword(String payPassword, String oldPayPassword) {
        if (payPassword.length() != 6) {
            throw new BizException("", "请输入六位数字");
        }
        Long empid = SecurityUtils.getUserId();
        OrgEmployee orgEmployee = this.employeeRepository.findById(empid).orElse(null);
        if (orgEmployee == null) {
            throw new BizException("", "未找到当前用户");
        }
        if (orgEmployee.getWallet() == null) {
            EmployeeWallet employeeWallet = new EmployeeWallet(BigDecimal.ZERO);
            this.walletRepository.saveAndFlush(employeeWallet);
            orgEmployee.setWallet(employeeWallet);
            this.employeeRepository.saveAndFlush(orgEmployee);
        }

        walletService.updatePayPassword(oldPayPassword, payPassword, orgEmployee.getWallet());
    }

    /**
     * 钱包支付
     *
     * @param password
     * @param amount
     */
    @Transactional(rollbackFor = Exception.class)
    public void serMoneybagPay(String password, BigDecimal amount) {
        Long empid = SecurityUtils.getUserId();
        OrgEmployee orgEmployee = this.employeeRepository.findById(empid).orElse(null);
        if (orgEmployee == null) {
            throw new BizException("", "未找到用户");
        }
        if (StringUtils.isBlank(password)) {
            throw new BizException("", "钱包支付密码不能为空！");
        }
        EmployeeWallet wallet = orgEmployee.getWallet();
        if (StringUtils.isBlank(wallet.getPayPassword())) {
            throw new BizException("6598", "当前用户未设置钱包支付密码，请先设置支付密码！");
        }
        if (!passwordEncoder.matches(password, wallet.getPayPassword())) {
            throw new BizException("", "支付密码错误，请重新输入！");
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new BizException("", "余额不足，请充值！");
        }
        this.walletService.subtractBalance(wallet, amount);
    }

    /**
     * 添加余额变动记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRecord(BigDecimal amount, Long tackOutOrderId, LocalDateTime orderCreateTime) {
        Optional<OrgEmployee> orgEmployeeOptional = this.employeeRepository.findById(SecurityUtils.getUserId());
        if (!orgEmployeeOptional.isPresent()) {
            throw new BizException("", "未找到当前用户");
        }
        OrgEmployee employee = orgEmployeeOptional.get();
        FinTransactionRecord record = new FinTransactionRecord()
                .setEatType(MealTypeEnum.TAKEOUT.getCode())
                .setCardNo(employee.getCardNo())
                .setEmployee(employee)
                .setTyp(0)
                .setState(1)
                .setAmount(amount)
                .setPayType(PayTypeEnum.WALLETPAY.getCode())
                .setTransactionTime(orderCreateTime)
                .setTackOutOrderId(tackOutOrderId);
        record = this.finTransactionRecordRepository.save(record);

        //余额变动记录
        WalletBillDetail walletBillDetail = new WalletBillDetail();
        walletBillDetail.setWalletId(record.getEmployee().getWallet().getId());
        walletBillDetail.setRecordId(record.getId());
        walletBillDetail.setRecordType(1);
        walletBillDetail.setCreateTime(LocalDateTime.now());
        walletBillDetail.setType(0);
        walletBillDetail.setAmount(record.getAmount());
        walletBillDetail.setReason(walletBillDetail.getBillDetailReason(record.getEatType()));
        this.walletBillDetailRepository.save(walletBillDetail);
    }

    @CacheEvict(value = RedisCacheUtil.FIN_RECHARGE_RECORDS, allEntries = true)
    public void addBalanceResult(String out_trade_no, boolean success) {
        FinRechargeOrder order = finRechargeOrderRepository.findByOrderNo(out_trade_no);
        if (success && order.getStatus().equals(OrderType.TOBEPAID)) {
            FinRechargeRecord finRechargeRecord = new FinRechargeRecord()
                    .setOrderNo(order.getOrderNo())
                    .setRechargeTime(order.getCreateTime())
                    .setCardNo(order.getCardNo())
                    .setState(RechargeStateEnum.NORMAL.getCode())
                    .setEmployee(order.getEmployee())
                    .setTyp(0)
                    .setAmount(order.getAmount())
                    .setPayType(order.getPayType());
            walletService.addBalance(finRechargeRecord.getEmployee().getWallet(), finRechargeRecord.getAmount());
            this.rechargeRecordRepository.save(finRechargeRecord);
            order.setStatus(OrderType.PAYSUCCESS);

            //余额变动记录
            WalletBillDetail walletBillDetail = new WalletBillDetail();
            walletBillDetail.setWalletId(finRechargeRecord.getEmployee().getWallet().getId());
            walletBillDetail.setRecordId(finRechargeRecord.getId());
            walletBillDetail.setRecordType(0);
            walletBillDetail.setCreateTime(LocalDateTime.now());
            walletBillDetail.setType(1);
            walletBillDetail.setAmount(finRechargeRecord.getAmount());
            walletBillDetail.setReason(Constants.WALLET_BILL_DETAIL_REASON_RECHARGE_WX);
            this.walletBillDetailRepository.save(walletBillDetail);

            //同步到新开普系统
            if (!CollectionUtils.isEmpty(order.getEmployee().getCardList())) {
                Boolean syncResult = escpRechargeService.recharge(
                        order.getEmployee().getCardList().get(0).getCardNo(),
                        order.getEmployee().getPersonCode(),
                        order.getEmployee().getName(),
                        order.getAmount(),
                        order.getOrderNo()
                );

                finRechargeRecord.setSyncToEscp(syncResult);
                finRechargeRecord.setSyncToEscpTimes(1);
                this.rechargeRecordRepository.save(finRechargeRecord);
            }

        } else if (order.getStatus().equals(OrderType.TOBEPAID) && !success) {
            order.setStatus(OrderType.PAYFAILE);
        }
        this.finRechargeOrderRepository.save(order);
    }

    public CursorPageDTO<BillListDto> findBillList(LocalDateTime time, Integer type, String cursor) {
        SpecificationBuilder rechargeBuilder = SpecificationBuilder.builder();
        SpecificationBuilder transactionBuilder = SpecificationBuilder.builder();

        if (StringUtils.isNotBlank(cursor) && cursor.split(",").length == 2) {
            rechargeBuilder.where(Criterion.lt("id", cursor.split(",")[0]));
            transactionBuilder.where(Criterion.lt("id", cursor.split(",")[1]));
        }

        rechargeBuilder.where(Criterion.eq("employee", SecurityUtils.getUserId()));
        transactionBuilder.where(Criterion.eq("employee", SecurityUtils.getUserId()));
        if (time != null) {
            rechargeBuilder.where(Criterion.gte("rechargeTime", time));
            rechargeBuilder.where(Criterion.lte("rechargeTime", time.plusMonths(1)));

            transactionBuilder.where(Criterion.gte("createTime", time));
            transactionBuilder.where(Criterion.lte("createTime", time.plusMonths(1)));
        }

        List<FinRechargeRecord> rechargeRecords = new ArrayList<>();

        List<FinTransactionRecord> transactionRecords = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        if (type != null) {
            switch (type) {
                case 1:
                    rechargeRecords = this.rechargeRecordRepository.findAll(rechargeBuilder.build(), pageRequest).getContent();
                    break;
                case 2:
                    transactionRecords = this.transactionRecordRepository.findAll(transactionBuilder.build(), pageRequest).getContent();
                    break;
                default:
                    rechargeRecords = this.rechargeRecordRepository.findAll(rechargeBuilder.build(), pageRequest).getContent();
                    transactionRecords = this.transactionRecordRepository.findAll(transactionBuilder.build(), pageRequest).getContent();
                    break;
            }
        } else {
            rechargeRecords = this.rechargeRecordRepository.findAll(rechargeBuilder.build(), pageRequest).getContent();
            transactionRecords = this.transactionRecordRepository.findAll(transactionBuilder.build(), pageRequest).getContent();
        }
        String resultCursor = (rechargeRecords.size() != 0 ? String.valueOf(rechargeRecords.get(rechargeRecords.size() - 1).getId()) : "-1") + "," + (transactionRecords.size() != 0 ? String.valueOf(transactionRecords.get(transactionRecords.size() - 1).getId()) : "-1");

        List<BillListDto> result = new ArrayList<>();
        //充值记录
        result.addAll(rechargeRecords.stream().map(item -> {
            BillListDto listDto = new BillListDto();
            if (item.getTyp() == 0) {
                switch (item.getPayType()) {
                    case ALIPAY:
                        listDto.setType(BillType.ALIPAY);
                        break;
                    case WECHAT:
                        listDto.setType(BillType.WECHAT);
                        break;
                }
            } else if (item.getTyp() == 1) {
                listDto.setType(BillType.SYSTEM);
            }

            listDto.setAmount(item.getAmount())
                    .setTime(item.getRechargeTime() != null ? item.getRechargeTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")) : null);
            return listDto;
        }).collect(Collectors.toList()));

        //消费记录
        result.addAll(transactionRecords.stream().map(item -> {
            BillListDto listDto = new BillListDto();
            if (item.getEatType() != null) {
                switch (item.getEatType()) {
                    case 0:
                        listDto.setType(BillType.BREAKFAST);
                        break;
                    case 1:
                        listDto.setType(BillType.LUNCH);
                        break;
                    case 2:
                        listDto.setType(BillType.DINNER);
                        break;
                    case 3:
                        listDto.setType(BillType.AFTERNOONTEA);
                        break;
                    case 4:
                        listDto.setType(BillType.WALLETPAYMENT);
                        break;
                    case 8:
                        listDto.setType(BillType.WALLETPAYMENT);
                        break;
                }
            } else {
                listDto.setType(BillType.AFTERNOONTEA);
            }
            listDto.setAmount(item.getAmount().negate());
            listDto.setTime(item.getCreateTime() != null ? item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")) : null);
            return listDto;
        }).collect(Collectors.toList()));
        result.sort(Comparator.comparing(BillListDto::getTime).reversed());
        return PageUtil.toCursorPageDTO(result, resultCursor);
    }

    public Boolean haveTackOutWxPayPayPassword() {
        Long empid = SecurityUtils.getUserId();
        OrgEmployee orgEmployee = this.employeeRepository.findById(empid).orElse(null);
        if (orgEmployee == null) {
            throw new BizException("", "未找到当前用户");
        }
        EmployeeWallet wallet = orgEmployee.getWallet();
        if (null == wallet || StringUtils.isBlank(wallet.getPayPassword())) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}
