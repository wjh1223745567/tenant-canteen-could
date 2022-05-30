package com.iotinall.canteen.excellistener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.orgemployee.OrgMemberExcelDto;
import com.iotinall.canteen.service.ExcelService;
import com.iotinall.canteen.utils.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrgMemberExcelListener extends AnalysisEventListener<OrgMemberExcelDto> {

    List<OrgMemberExcelDto> allData = new ArrayList<>();

    private static ExcelService excelService;

    @Override
    public void invoke(OrgMemberExcelDto object, AnalysisContext context) throws BizException {
        Integer row = context.readRowHolder().getRowIndex();
        String errorMsg = "第" + (row + 1) + "行数据异常：";
        if (StringUtils.isBlank(object.getOrgName())) {
            throw new BizException("", errorMsg + "部门不能为空");
        }

        if(StringUtils.isBlank(object.getSubOrgName())){
            throw new BizException("", errorMsg + "二级部门不能为空");
        }

        if(StringUtils.isBlank(object.getName())){
            throw new BizException("", errorMsg + "姓名不能为空");
        }

        if(StringUtils.isBlank(object.getIdNo())){
            throw new BizException("", errorMsg + "身份证号不能为空");
        }

        try {
            ValidateUtil.IDCardValidate(object.getIdNo());
        } catch (BizException e) {
            throw new BizException("", errorMsg + e.getMsg());
        }

        if(StringUtils.isBlank(object.getMobile())){
            throw new BizException("", errorMsg + "电话不能为空");
        }

        try {
            ValidateUtil.validPhone(object.getMobile());
        } catch (BizException e) {
            throw new BizException("", errorMsg + e.getMsg());
        }

        if(object.getEntryDate() == null){
            throw new BizException("", errorMsg + "入职时间不能为空");
        }

        allData.add(object);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        int row = 0, column = 0;
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException convertException = (ExcelDataConvertException) exception;

            row = convertException.getRowIndex();
            column = convertException.getColumnIndex();
            throw new BizException("", "解析出错：" + row + "行，" + column + "列，值：" + convertException.getCellData().getStringValue());
        }
        super.onException(exception, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        excelService.addMember(this.allData);
    }

    @Resource
    public void setExcelService(ExcelService excelService) {
        OrgMemberExcelListener.excelService = excelService;
    }
}
