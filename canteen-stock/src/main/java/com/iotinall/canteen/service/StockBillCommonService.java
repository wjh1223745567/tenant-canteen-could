package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.DateTimeFormatters;
import com.iotinall.canteen.utils.LocalDateUtil;
import com.iotinall.canteen.utils.ThumbnailatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockBillCommonService {
    @Resource
    protected StockSupplierService stockSupplierService;
    @Resource
    protected StockBillAuthorityRepository stockBillAuthorityRepository;
    @Resource
    protected StockGoodsRepository stockGoodsRepository;
    @Resource
    protected StockBillOperateLogRepository stockBillOperateLogRepository;
    @Resource
    protected StockTodoRepository stockTodoListRepository;
    @Resource
    protected StockDetailRepository stockDetailRepository;
    @Resource
    protected StockService stockService;
    @Resource
    protected StockDetailChangeRecordRepository stockDetailChangeRecordRepository;
    @Resource
    protected StockRepository stockRepository;
    @Resource
    protected StockBillRepository stockBillRepository;
    @Resource
    protected StockBillDetailRepository stockBillDetailRepository;
    @Resource
    protected StockFlwConfigService stockFlwConfigService;
    @Resource
    protected StockFlwConfigRepository stockFlwConfigRepository;
    @Resource
    protected FeignEmployeeService feignEmployeeService;
    @Resource
    protected StockGoodsTypeService stockGoodsTypeService;
    @Resource
    protected StockWarehouseService stockWarehouseService;
    @Resource
    protected FeignOrgService feignOrgService;
    @Resource
    protected StockStatRepository stockStatRepository;
    @Resource
    protected StockFlwTaskRepository stockFlwTaskRepository;
    @Resource
    protected StockWarningRepository stockWarningRepository;
    @Resource
    protected StockGoodsTypeRepository stockGoodsTypeRepository;
    @Resource
    protected StockCertificateAndInspectionReportRepository stockCertificateAndInspectionReportRepository;
    @Resource
    protected StockWarehouseRepository stockWarehouseRepository;
    @Resource
    protected FileHandler fileHandler;

    @Value("${canteen.resources.temp-path}")
    private String tempImgPath;

    /**
     * 获取流程任务
     *
     * @author loki
     * @date 2021/03/08 10:46
     */
    public StockFlwTask getFirstTask(String billType) {
        StockFlwTask nextTask = stockFlwConfigService.getFirstTask(billType);
        if (null == nextTask || StringUtils.isBlank(nextTask.getHandlerId())) {
            throw new BizException("流程未配置");
        }

        return nextTask;
    }

    /**
     * 获取下一个任务
     *
     * @author loki
     * @date 2021/03/08 10:46
     */
    public StockFlwTask getNextTask(StockFlwTask task) {
        StockFlwTask nextTask = stockFlwConfigService.getNextTask(task);
        if (null == nextTask || StringUtils.isBlank(nextTask.getHandlerId())) {
            throw new BizException("流程未配置");
        }

        return nextTask;
    }


    /**
     * 获取新单据状态
     *
     * @author loki
     * @date 2021/6/4 15:19
     **/
    protected Integer getNewBillStatus(StockFlwTask task) {
        return task.getTaskDefine().equals(Constants.TASK_DEFINE.AUDIT) ?
                Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0 :
                Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE;
    }

    /**
     * 获取审批的单据状态
     *
     * @author loki
     * @date 2021/6/4 19:51
     **/
    protected Integer getAuditBillStatus(StockFlwTask task) {
        if (task.getTaskDefine().equals(Constants.TASK_DEFINE_ACCEPTANCE)) {
            return Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE;
        } else {
            return Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1;
        }
    }

    /**
     * 处理单据权限
     *
     * @author loki
     * @date 2020/09/14 10:37
     */
    protected Set<StockBillAuthority> buildBillAuthority(Long billId, Long todoId, String roleIds) {
        String[] roles = roleIds.split(",");
        StockBillAuthority authority;
        Set<StockBillAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authority = new StockBillAuthority();
            authorities.add(authority);
            authority.setBillId(billId);
            authority.setTodoId(todoId);
            authority.setRoleId(Long.valueOf(role));
        }

        return authorities;
    }

    /**
     * 构建待办对象
     *
     * @author loki
     * @date 2021/6/4 15:46
     **/
    protected StockTodo buildTodoList(StockBill bill, StockFlwTask task) {
        return (StockTodo) new StockTodo()
                .setStockBill(bill)
                .setStatus(Constants.TODO_STATUS_INIT)
                .setOptType(task.getTaskDefine())
                .setTask(task)
                .setReceiverId(task.getHandlerId())
                .setApplyUserId(bill.getApplyUserId())
                .setApplyUserName(bill.getApplyUserName())
                .setRemark(bill.getRemark());
    }

    /**
     * 获取商品验收状态
     *
     * @author loki
     * @date 2021/6/4 16:13
     **/
    protected Boolean getBillDetailAcceptanceStatus(Set<StockBillDetail> details, Long goodsId) {
        for (StockBillDetail detail : details) {
            if (detail.getGoods() != null && detail.getGoods().getId().equals(goodsId)) {
                return detail.getAcceptance();
            }
        }

        return false;
    }

    /**
     * 获取单据信息
     *
     * @author loki
     * @date 2021/6/4 16:26
     **/
    protected StockBill getBillByNo(String billNo) {
        StockBill bill = this.stockBillRepository.findByBillNo(billNo);
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        return bill;
    }

    /**
     * 计算保质日期
     *
     * @author loki
     * @date 2020/09/11 17:18
     */
    protected LocalDate calculateShelfLife(StockBillDetail billDetail) {
        if (null == billDetail.getProductionDate()
                || null != billDetail.getShelfLifeUnit()
                || null != billDetail.getShelfLife()) {
            return null;
        }

        //0-年 1-月 2-日
        if (0 == billDetail.getShelfLifeUnit()) {
            return billDetail.getProductionDate().plusYears(billDetail.getShelfLife());
        } else if (1 == billDetail.getShelfLifeUnit()) {
            return billDetail.getProductionDate().plusMonths(billDetail.getShelfLife());
        } else if (2 == billDetail.getShelfLifeUnit()) {
            return billDetail.getProductionDate().plusDays(billDetail.getShelfLife());
        }
        return null;
    }


    /**
     * 保存验收图片
     *
     * @author loki
     * @date 2021/03/06 18:08
     */
    protected String saveAcceptanceImage(MultipartFile file,
                                         StockBill bill,
                                         StockBillDetail billDetail
    ) {
        if (null != file) {
            try {
                String goodsName = billDetail.getGoods().getName() + "(" + billDetail.getGoods().getUnit() + ")";
                String billDate = LocalDateUtil.format(bill.getBillDate(), DateTimeFormatters.STANDARD_DATE_FORMATTER);
                BufferedImage bufferedImage = ThumbnailatorUtils.createWaterMark(file,
                        bill.getBillType(),
                        goodsName,
                        billDate,
                        billDetail.getAmount(),
                        billDetail.getRealAmount(),
                        tempImgPath);
                return fileHandler.saveImage("group1", bufferedImage);
            } catch (Exception ex) {
                log.info("上传图片失败：{}", ex.getMessage());
            }
        }
        return null;
    }


    /**
     * 校验单据状态
     *
     * @author loki
     * @date 2021/6/4 17:09
     **/
    protected void checkBillStatus(String billType, StockBill bill, Long version) {
        if (!billType.equals(bill.getBillType())) {
            throw new BizException("单据类型不匹配");
        }

        if (!version.equals(bill.getVersion())) {
            throw new BizException("单据已被编辑");
        }

        if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_CANCEL == bill.getStatus()) {
            throw new BizException("单据已被取消");
        }
    }

    /**
     * 获取单据明细
     *
     * @author loki
     * @date 2021/6/4 17:12
     **/
    protected StockBillDetail getStockBillDetail(StockBill bill, StockGoods goods) {
        StockBillDetail billDetail = this.stockBillDetailRepository.findByBillAndGoods(bill, goods);
        if (null == billDetail) {
            throw new BizException("单据明细不存在");
        }

        if (null != billDetail.getAcceptance() && billDetail.getAcceptance()) {
            throw new BizException("商品已经验收");
        }

        return billDetail;
    }

    /**
     * 单据是否可以编辑
     *
     * @author loki
     * @date 2021/6/7 15:50
     **/
    public void canEdit(StockBill bill) {
        if (bill.getStatus() != Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE
                && bill.getStatus() != Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0
                && bill.getStatus() != Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1) {
            throw new BizException("单据已取消或者已验收");
        }
    }

    /**
     * 构建库存明细变动记录
     *
     * @author loki
     * @date 2021/6/7 17:08
     **/
    public StockDetailChangeRecord buildChangeRecordSub(BigDecimal changeAmount,
                                                        BigDecimal before,
                                                        BigDecimal after,
                                                        Integer seq,
                                                        StockDetail detail,
                                                        StockBillDetail billDetail) {
        //库存明细变动记录
        return new StockDetailChangeRecord()
                .setAmount(changeAmount)
                .setBeforeAmount(before)
                .setAfterAmount(after)
                .setType(1)
                .setSeq(seq)
                .setStockDetail(detail)
                .setBillDetail(billDetail);
    }

    /**
     * 构建库存明细变动记录
     *
     * @author loki
     * @date 2021/6/8 14:16
     **/
    public StockDetailChangeRecord buildChangeRecordAdd(BigDecimal changeAmount,
                                                        BigDecimal before,
                                                        BigDecimal after,
                                                        Integer seq,
                                                        StockDetail detail,
                                                        StockBillDetail billDetail) {
        //库存明细变动记录
        return new StockDetailChangeRecord()
                .setAmount(changeAmount)
                .setBeforeAmount(before)
                .setAfterAmount(after)
                .setType(0)
                .setSeq(seq)
                .setStockDetail(detail)
                .setBillDetail(billDetail);
    }

    /**
     * 初始化单据
     *
     * @author loki
     * @date 2021/6/8 9:36
     **/
    public StockBill initBill(StockFlwTask task) {
        return new StockBill()
                .setTask(task)
                .setApplyUserId(SecurityUtils.getUserId())
                .setApplyUserName(SecurityUtils.getUserName())
                .setStatus(this.getNewBillStatus(task))
                .setVersion(System.currentTimeMillis());
    }

    /**
     * 校验某次出库申请是否已经全额退库
     * 1、退库申请
     * 2、退库验收
     * <p>
     * true 表示还可以退库
     * false 表示已经退库所有,不能退库
     *
     * @author loki
     * @date 2020/09/25 9:19
     */
    protected Boolean checkOutBackAll(BigDecimal totalOutAmount, BigDecimal backAmount, Long outDetailId) {
        BigDecimal outBackTotalAmount = getOutBackTotal(outDetailId);

        outBackTotalAmount = (null == outBackTotalAmount ? BigDecimal.ZERO : outBackTotalAmount).add(backAmount);

        return totalOutAmount.compareTo(outBackTotalAmount) >= 0;
    }

    /**
     * 获取出库单已退库数量
     *
     * @author loki
     * @date 2021/7/2 17:23
     **/
    public BigDecimal getOutBackTotal(Long outDetailId) {
        return this.stockBillDetailRepository.statGoodsOutBackTotal(outDetailId);
    }

    /**
     * 单据状态：
     * 0-待审核（未审核）
     * 1-待审核（至少存在一次审核）
     * 2-待验收
     * 3-已完成
     * 4-已拒绝
     * 5-已取消
     * <p>
     * 按钮显示逻辑：
     * <p>
     * 1-修改操作
     * a.状态：0/2
     * b.申请人为当前登入用户(申请人接口给)
     * <p>
     * 2-取消按钮
     * a.状态：0/1/2
     * b.申请人为当前登入用户(申请人接口给)
     * <p>
     * 3-审核按钮
     * a.状态： 0/1
     * b.审核人为当前登入用户（审核人接口给）
     * <p>
     * 4-验收按钮
     * a.状态： 2
     * b.验收人为当前登入用户（验收人接口给）
     */
    protected List<String> genUserOptions(StockBill bill) {
        final String optionEdit = "OPTION_EDIT";
        final String optionCancel = "OPTION_CANCEL";
        final String optionAudit = "OPTION_AUDIT";
        final String optionAcceptance = "OPTION_ACCEPTANCE";
        final String optionReApply = "OPTION_RE_APPLY";
        final String optionMultiAcceptance = "OPTION_MULTI_ACCEPTANCE";

        Long applyId = bill.getApplyUserId();
        Long createId = bill.getCreateId();
        Integer status = bill.getStatus();
        String billType = bill.getBillType();
        Integer taskId = bill.getTask().getTaskId();
        boolean existOneAcceptance = bill.getDetails().stream().anyMatch(StockBillDetail::getAcceptance);

        List<String> options = new ArrayList<>(5);
        Long loginUserId = SecurityUtils.getUserId();

        //流程配置
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(billType);

        //修改按钮,存在审核和不存在审核两种情况
        if (checkExistAuditTask(flwConfig.getId(), bill.getTask().getVersion())) {
            if (status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0
                    || status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1) {
                if (createId.equals(loginUserId) || applyId.equals(loginUserId)) {
                    options.add(optionEdit);
                }
            }
        } else {
            if (status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE) {
                if (createId.equals(loginUserId) || applyId.equals(loginUserId)) {
                    options.add(optionEdit);
                }
            }
        }

        //取消按钮
        if (status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0
                || status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1
                || status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE) {
            if (!existOneAcceptance && applyId.equals(loginUserId)) {
                options.add(optionCancel);
            }
        }

        //审核按钮
        if (status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0
                || status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1) {
            //判断当前用户是否具有审核权限
            if (checkUserHaveOptions(loginUserId, flwConfig.getId(), bill.getTask().getVersion(), taskId)) {
                options.add(optionAudit);
            }
        }

        //验收按钮
        if (status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE) {
            //判断用户是否具有验收权限
            if (checkUserHaveOptions(loginUserId, flwConfig.getId(), bill.getTask().getVersion(), taskId)) {
                options.add(optionAcceptance);
                options.add(optionMultiAcceptance);
            }
        }

        //重新发起按钮
        if (status == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_REFUSE) {
            //判断用户是否具有重新发起权限
            if (applyId.equals(loginUserId)) {
                options.add(optionReApply);
            }
        }

        return options;
    }

    /**
     * 检验用户是否具有操作权限
     *
     * @author loki
     * @date 2020/09/03 14:48
     */
    private Boolean checkUserHaveOptions(Long empId, Long flwConfigId, Long version, Integer taskId) {
        //获取当前任务需要的权限
        StockFlwTask task = this.stockFlwTaskRepository.findByFlwConfigIdAndTaskIdAndVersion(flwConfigId, taskId, version);

        //获取当前登入用户拥有的权限
        FeignEmployeeDTO feignEmployeeDTO = feignEmployeeService.findById(empId);

        List<Long> optRoles = Arrays.
                stream(task.getHandlerId().split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<Long> userRoles = feignEmployeeDTO.getRoleIds();
        if (CollectionUtils.isEmpty(userRoles)) {
            return false;
        }
        userRoles.retainAll(optRoles);
        return !CollectionUtils.isEmpty(userRoles);
    }

    /**
     * 校验是否存在审核环节
     *
     * @author loki
     * @date 2021/6/10 13:59
     **/
    public Boolean checkExistAuditTask(Long flwConfigId, Long version) {
        List<StockFlwTask> taskList = this.stockFlwTaskRepository.findByFlwConfigIdAndVersionAndTaskDefine(
                flwConfigId,
                version,
                Constants.TASK_DEFINE.AUDIT);
        return !CollectionUtils.isEmpty(taskList);
    }
}
