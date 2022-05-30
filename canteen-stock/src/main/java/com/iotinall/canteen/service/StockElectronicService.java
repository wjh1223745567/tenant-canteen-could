package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.electronic.ElectronicAcceptReq;
import com.iotinall.canteen.dto.electronic.ElectronicAcceptResultDTO;
import com.iotinall.canteen.dto.electronic.ElectronicBillDTO;
import com.iotinall.canteen.dto.electronic.ElectronicGoodsDTO;
import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockBillDetail;
import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.repository.StockBillDetailRepository;
import com.iotinall.canteen.repository.StockBillRepository;
import com.iotinall.canteen.repository.StockGoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 电子秤逻辑处理类
 *
 * @author loki
 * @date 2021/05/27 17:05
 */
@Slf4j
@Service
public class StockElectronicService extends StockBillCommonService {
    @Resource
    private StockBillRepository stockBillRepository;
    @Resource
    private StockGoodsRepository stockGoodsRepository;
    @Resource
    private StockBillDetailRepository stockBillDetailRepository;
    @Resource
    private StockBillInService stockBillInService;

    /**
     * 电子秤获取单据列表
     * 注意：只返回未完成的单据
     *
     * @author loki
     * @date 2021/05/27 17:06
     */
    public Object billList(String billType) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.eq("billType", billType))
                .where(Criterion.eq("status", Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE));

        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<StockBill> result = this.stockBillRepository.findAll(builder.build(), sort);
        if (CollectionUtils.isEmpty(result)) {
            return Collections.EMPTY_LIST;
        }

        List<ElectronicBillDTO> electronicBillList = new ArrayList<>(result.size());
        ElectronicBillDTO electronicBill;
        for (StockBill bill : result) {
            electronicBill = new ElectronicBillDTO();
            electronicBillList.add(electronicBill);
            electronicBill.setBillId(bill.getId());
            electronicBill.setBillNo(bill.getBillNo());
            electronicBill.setBillDate(bill.getBillDate());
            electronicBill.setVersion(bill.getVersion());
            electronicBill.setSupplierName(null != bill.getSupplier() ? bill.getSupplier().getName() : "");


            if (!CollectionUtils.isEmpty(bill.getDetails())) {
                List<ElectronicGoodsDTO> electronicGoodsList = new ArrayList<>();
                ElectronicGoodsDTO electronicGoods;
                for (StockBillDetail detail : bill.getDetails()) {
                    electronicGoods = new ElectronicGoodsDTO();
                    electronicGoodsList.add(electronicGoods);

                    electronicGoods.setAmount(detail.getAmount());
                    electronicGoods.setRealAmount(detail.getRealAmount());
                    electronicGoods.setDetailId(detail.getId());
                    electronicGoods.setAcceptImgUrl(StringUtils.isBlank(detail.getImgUrl()) ? "" : ImgPair.getFileServer() + detail.getImgUrl());
                    electronicGoods.setOriginalImgPath(detail.getOriginalImgPath());
                    electronicGoods.setPrice(detail.getPrice());
                    electronicGoods.setProductionDate(detail.getProductionDate());
                    electronicGoods.setShelfLife(detail.getShelfLife());

                    electronicGoods.setShelfLifeUnit(detail.getShelfLifeUnit());
                    electronicGoods.setInspectionReport(detail.getInspectionReport());
                    electronicGoods.setUploaded(!StringUtils.isBlank(detail.getImgUrl()));
                    electronicGoods.setAccepted(detail.getAcceptance());
                    electronicGoods.setWeighed(detail.getWeighed());
                    electronicGoods.setVersion(bill.getVersion());

                    if (null != detail.getGoods()) {
                        electronicGoods.setGoodsId(detail.getGoods().getId());
                        electronicGoods.setGoodsName(detail.getGoods().getName());
                        electronicGoods.setGoodsNamePinYin(detail.getGoods().getNamePinYin() + "|" + detail.getGoods().getNameFirstLetter());
                        electronicGoods.setUnit(detail.getGoods().getUnit());
                        electronicGoods.setGoodsImgUrl(StringUtils.isBlank(detail.getGoods().getImgUrl()) ? "" : ImgPair.getFileServer() + detail.getGoods().getImgUrl());
                    }
                }

                electronicBill.setGoodsList(electronicGoodsList);
            }
        }
        log.info("单据列表参数：{}", JSON.toJSONString(electronicBillList));
        return electronicBillList;
    }

    /**
     * 电子秤验收
     *
     * @author loki
     * @date 2021/05/27 17:10
     */
    @Transactional(rollbackFor = Exception.class)
    public Object accept(ElectronicAcceptReq req) {
        log.info("验收请求参数：{}", JSON.toJSONString(req));
        StockBill bill = this.stockBillRepository.findByBillNo(req.getBillNo());
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        /**
         * 是否已被编辑
         */
        if (!bill.getVersion().equals(req.getVersion())) {
            throw new BizException("单据已被编辑,请重新操作");
        }

        StockGoods goods = this.stockGoodsRepository.findById(req.getGoodsId())
                .orElseThrow(() -> new BizException("商品不存在")
                );

        StockBillDetail billDetail = stockBillDetailRepository.findByBillAndGoods(bill, goods);
        if (null == billDetail) {
            throw new BizException("单据明细不存在");
        }

        if (!billDetail.getAcceptance()) {
            billDetail.setAcceptance(true);
            billDetail.setRealAmount(null == req.getRealAmount() ? BigDecimal.ZERO : req.getRealAmount());
            billDetail.setUpdateTime(LocalDateTime.now());
            billDetail.setWeighed(req.getWeighed());
            billDetail.setOriginalImgPath(req.getOriginalImgPath());

            switch (bill.getBillType()) {
                case Constants.BILL_TYPE.STOCK_IN: {
                    billDetail.setShelfLife(req.getShelfLife());
                    billDetail.setShelfLifeUnit(req.getShelfLifeUnit());
                    billDetail.setProductionDate(req.getProductionDate());
                    billDetail.setInspectionReport(req.getInspectionReport());

                    billDetail.setShelfLifeDate(this.calculateShelfLife(billDetail));
                    stockBillInService.acceptance(billDetail, goods);
                    break;
                }
                case Constants.BILL_TYPE.STOCK_OUT: {
                    //stockInService.electronicAccept();
                    break;
                }
                case Constants.BILL_TYPE.STOCK_IN_BACK: {
                    //stockInService.electronicAccept();
                    break;
                }
                case Constants.BILL_TYPE.STOCK_OUT_BACK: {
                    //stockInService.electronicAccept();
                    break;
                }
                default: {
                    throw new BizException("商品验收失败");
                }
            }

            this.stockBillDetailRepository.save(billDetail);
        } else {
            log.info("单据已经验收..");
        }

        return new ElectronicAcceptResultDTO()
                .setBillId(bill.getId())
                .setBillNo(bill.getBillNo())
                .setGoodsId(goods.getId())
                .setAcceptImgUrl(StringUtils.isBlank(billDetail.getImgUrl()) ? "" : ImgPair.getFileServer() + billDetail.getImgUrl()
                );
    }

    /**
     * 电子秤上传图片,只更新图片信息，支持重入，但是上一次已经保存了图片不会再更新图片信息
     *
     * @author loki
     * @date 2021/05/27 17:10
     */
    public void upload(MultipartFile file, String billNo, Long goodsId) {
        StockBill bill = this.stockBillRepository.findByBillNo(billNo);
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        StockGoods goods = this.stockGoodsRepository.findById(goodsId).orElseThrow(() -> new BizException("商品不存在"));
        StockBillDetail billDetail = stockBillDetailRepository.findByBillAndGoods(bill, goods);
        if (null == billDetail) {
            throw new BizException("单据明细不存在");
        }

        if (StringUtils.isBlank(billDetail.getImgUrl())) {
            /**
             * 新需求，如果商品没有图片，则保存验收的图片为商品图片
             */
            if (StringUtils.isBlank(goods.getImgUrl())) {
                try {

                    String goodsImgUrl = fileHandler.saveFile("group1", file);
                    goods.setImgUrl(goodsImgUrl);
                    this.stockGoodsRepository.save(goods);
                } catch (Exception ex) {
                    throw new BizException("验收失败");
                }
            }

            billDetail.setImgUrl(this.saveAcceptanceImage(file, bill, billDetail));

            this.stockBillDetailRepository.save(billDetail);
        }
    }
}
