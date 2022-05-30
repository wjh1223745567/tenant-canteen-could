package com.iotinall.canteen.excel.goods;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.service.StockGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StockGoodsExcelListener extends AnalysisEventListener<StockGoodsExcelDTO> {

    List<StockGoodsExcelDTO> goodsExcelDtos = new ArrayList<>();

    private static StockGoodsService stockGoodsService;

    @Override
    public void invoke(StockGoodsExcelDTO object, AnalysisContext context) throws BizException {
        Integer row = context.readRowHolder().getRowIndex();
        String errorMsg = "第" + (row + 1) + "行数据异常:";
        if (StringUtils.isBlank(object.getGoodsType())) {
            throw new BizException(errorMsg + "商品类别不能为空");
        }
        if (StringUtils.isBlank(object.getCode())) {
            throw new BizException(errorMsg + "商品编码不能为空");
        }
        if (StringUtils.isBlank(object.getName())) {
            throw new BizException(errorMsg + "商品名称不能为空");
        }
        if (StringUtils.isBlank(object.getUnit())) {
            throw new BizException(errorMsg + "商品单位不能为空");
        }
        if (StringUtils.isBlank(object.getPrice())) {
            throw new BizException(errorMsg + "中心定价不能为空");
        }

        goodsExcelDtos.add(object);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        int row, column;
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException convertException = (ExcelDataConvertException) exception;
            row = convertException.getRowIndex();
            column = convertException.getColumnIndex();
            throw new BizException("", "解析出错:" + row + "行," + column + "列,值:" + convertException.getCellData().getStringValue());
        }
        super.onException(exception, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        stockGoodsService.importGoods(this.goodsExcelDtos);
    }

    @Resource
    public void setStockGoodsService(StockGoodsService stockGoodsService) {
        StockGoodsExcelListener.stockGoodsService = stockGoodsService;
    }

}
