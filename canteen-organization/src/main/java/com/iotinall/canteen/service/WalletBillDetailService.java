package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.WalletBillReasonEnum;
import com.iotinall.canteen.dto.bill.WalletBillDetailDTO;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.WalletBillDetail;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.WalletBillDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 账单服务处理类
 *
 * @author loki
 * @date 2021/01/18 15:54
 */
@Service
public class WalletBillDetailService {
    @Resource
    private WalletBillDetailRepository walletBillDetailRepository;
    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;

    /**
     * 账单列表
     *
     * @author loki
     * @date 2021/01/18 19:22
     */
    public Object page(LocalDate billDate, Pageable page) {

        OrgEmployee employee = this.orgEmployeeRepository.findById(Objects.requireNonNull(SecurityUtils.getUserId())).orElseThrow(() -> new BizException("", "当前用户不存在"));
        if (employee.getWallet() == null) {
            return PageUtil.toPageDTO(Collections.emptyList(), 0L);
        }

        Page<WalletBillDetail> result;
        if (null != billDate) {
            LocalDate endDay = billDate.with(TemporalAdjusters.lastDayOfMonth());
            SpecificationBuilder builder = SpecificationBuilder.builder()
                    .where(Criterion.eq("walletId", employee.getWallet().getId()))
                    .where(Criterion.gte("createTime", LocalDateTime.of(billDate, LocalTime.of(0, 0, 0))))
                    .where(Criterion.lte("createTime", LocalDateTime.of(endDay, LocalTime.of(23, 59, 59))));

            result = this.walletBillDetailRepository.findAll(builder.build(), page);
        } else {
            result = this.walletBillDetailRepository.findAll(page);
        }

        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<WalletBillDetailDTO> billList = new ArrayList<>();
        WalletBillDetailDTO billDetail;
        for (WalletBillDetail detail : result.getContent()) {
            billDetail = new WalletBillDetailDTO();
            BeanUtils.copyProperties(detail, billDetail);
            billDetail.setReason(WalletBillReasonEnum.getByCode(detail.getReason()));
            billList.add(billDetail);
        }
        return PageUtil.toPageDTO(billList, result);
    }
}
