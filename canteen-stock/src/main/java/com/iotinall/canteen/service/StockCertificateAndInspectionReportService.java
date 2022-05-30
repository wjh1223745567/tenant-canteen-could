package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.bill.StockBillCertificateReq;
import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockCertificateAndInspectionReport;
import com.iotinall.canteen.repository.StockBillRepository;
import com.iotinall.canteen.repository.StockCertificateAndInspectionReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单据验收报告逻辑处理类
 *
 * @author loki
 * @date 2021/6/10 14:32
 **/
@Service
@Slf4j
public class StockCertificateAndInspectionReportService {
    @Resource
    private StockBillRepository stockBillRepository;
    @Resource
    private StockCertificateAndInspectionReportRepository stockCertificateAndInspectionReportRepository;

    public Object imgList(String billNo) {
        StockBill bill = stockBillRepository.findByBillNo(billNo);
        List<StockCertificateAndInspectionReport> stockBillImgs = stockCertificateAndInspectionReportRepository.findByStockBill(bill);
        if (CollectionUtils.isEmpty(stockBillImgs)) {
            return Collections.EMPTY_LIST;
        }

        List<String> imgList = new ArrayList<>();
        for (StockCertificateAndInspectionReport i : stockBillImgs) {
            if (StringUtils.isBlank(i.getImgUrl())) {
                continue;
            }
            imgList.add(ImgPair.getFileServer() + i.getImgUrl());
        }
        return imgList;
    }

    public void update(StockBillCertificateReq req) {
        StockBill bill = stockBillRepository.findByBillNo(req.getBillNo());
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        List<StockCertificateAndInspectionReport> stockBillCertificateList = stockCertificateAndInspectionReportRepository.findByStockBill(bill);
        if (!CollectionUtils.isEmpty(stockBillCertificateList)) {
            this.stockCertificateAndInspectionReportRepository.deleteAll(stockBillCertificateList);
        }

        StockCertificateAndInspectionReport stockBillImg;
        String[] imgs = req.getImgs().split(",");
        for (String img : imgs) {
            stockBillImg = new StockCertificateAndInspectionReport();
            stockBillImg.setImgUrl(img.substring(ImgPair.getFileServer().length()));
            stockBillImg.setStockBill(bill);
            stockBillImg.setCreateTime(LocalDateTime.now());
            this.stockCertificateAndInspectionReportRepository.save(stockBillImg);
        }
    }
}
