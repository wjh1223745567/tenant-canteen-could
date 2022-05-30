package com.iotinall.canteen.protocol.statistic;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

@Data
@HeadRowHeight(value = 40) // 表头行高
@ContentRowHeight(value = 30) // 内容行高
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER_SELECTION)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
public class ActivityTextAnswerStatisticExportDTO {
    @ExcelProperty(value = {"问答题答案统计","题目"},index = 0)
    private String subjectName;

    @ExcelProperty(value = {"问答题答案统计","题目类型"},index = 1)
    private String subjectType;

    @ExcelProperty(value = {"问答题答案统计","答案"},index = 2)
    private String textAnswer;

    @ExcelProperty(value = {"问答题答案统计","答题人"},index = 3)
    private String empName;
}
