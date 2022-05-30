package com.iotinall.canteen.protocol.statistic;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;


@Data
@HeadRowHeight(value = 50) // 表头行高
@ContentRowHeight(value = 40) // 内容行高
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER_SELECTION)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
public class ActivitySubjectOptionStatisticExportDTO {
    @ExcelProperty(value = {"选择题选项统计","题目"},index = 0)
    private String subjectName;

    @ExcelProperty(value = {"选择题选项统计","题目类型"},index = 1)
    private String subjectType;

    @ExcelProperty(value = {"选择题选项统计","选项"},index = 2)
    private String subjectOptionName;

    @ExcelProperty(value = {"选择题选项统计","选中人数"},index = 3)
    private Integer selectedNum;

    @ExcelProperty(value = {"选择题选项统计","占比"},index = 4)
    private String percent;

}
