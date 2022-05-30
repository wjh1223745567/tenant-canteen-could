package com.iotinall.canteen.constant;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.iotinall.canteen.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter implements Converter<LocalDate> {

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
        String value = cellData.getStringValue();
        if(StringUtils.isNotBlank(value)){
            try {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } catch (Exception e) {
                throw new BizException("", "日期" + value + "异常：请使用2019/02/01格式");
            }
        }else{
            return null;
        }
    }

    @Override
    public CellData convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
        return null;
    }
}
