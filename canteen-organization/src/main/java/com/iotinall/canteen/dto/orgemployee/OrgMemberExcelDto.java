package com.iotinall.canteen.dto.orgemployee;

import com.alibaba.excel.annotation.ExcelProperty;
import com.iotinall.canteen.constant.DateConverter;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrgMemberExcelDto {

    /**
     * 一级部门
     */
    @ExcelProperty(index = 0)
    private String orgName;

    /**
     * 二级部门
     */
    @ExcelProperty(index = 1)
    private String subOrgName;

    @ExcelProperty(index = 2)
    private String name;// 姓名

    @ExcelProperty(index = 3)
    private String idNo;// 身份证号

    @ExcelProperty(index = 4)
    private String role; //职位

    @ExcelProperty(index = 5)
    private String mobile;// 手机号码

    /**
     * 入职日期
     */
    @ExcelProperty(index = 6, converter = DateConverter.class)
    private LocalDate entryDate;

}
