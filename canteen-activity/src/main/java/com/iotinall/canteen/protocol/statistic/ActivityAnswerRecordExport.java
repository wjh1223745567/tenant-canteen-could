package com.iotinall.canteen.protocol.statistic;

import com.iotinall.canteen.protocol.excel.annotation.ExcelField;
import com.iotinall.canteen.protocol.excel.annotation.ExcelSheet;
import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 调查数据统计答案结果导出对象
 *
 * @author joelau
 * @date 2021-06-04 15：12
 **/
@Data
@ExcelSheet(name = "统计详情记录", headColor = HSSFColor.HSSFColorPredefined.GREY_25_PERCENT)
public class ActivityAnswerRecordExport {

    @ExcelField(name = "选项名")
    private String optionName;

    @ExcelField(name = "题目名")
    private String subjectName;

    @ExcelField(name = "选中人数")
    private Integer count;
}
