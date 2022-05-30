package com.iotinall.canteen.protocol.excel;

import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.protocol.excel.annotation.ExcelComment;
import com.iotinall.canteen.protocol.excel.annotation.ExcelField;
import com.iotinall.canteen.protocol.excel.annotation.ExcelSheet;
import com.iotinall.canteen.protocol.excel.util.FieldReflectionUtil;
import com.iotinall.canteen.protocol.excel.vo.SheetExportVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Excel导出工具
 *
 * @author loki
 * @date 2019/10/15 14:37
 */
@Slf4j
public class ExcelExportUtil {
    /**
     * 导出Excel文件入口
     *
     * @author loki
     * @date 2019/10/15 18:51
     */
    public static void asyncExport(String filePath, List<SheetExportVo> exportList) {
        //生成excel
        Workbook workbook = ExcelExportUtil.makeWorkbook(exportList);

        //写入文件
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            throw new BizException("", "导出失败");
        }
    }

    /**
     * 导出Excel文件入口
     *
     * @author loki
     * @date 2019/10/15 18:51
     */
    public static void asyncExport(String filePath, SheetExportVo export) {
        /**
         * 生成excel
         */
        Workbook workbook = new HSSFWorkbook();
        ExcelExportUtil.makeSheet(workbook, export);

        /**
         * 写入文件
         */
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            throw new BizException("", "导出失败");
        }
    }

    /**
     * 导出Excel文件入口
     *
     * @author loki
     * @date 2019/10/15 18:51
     */
    public static void exportToFile(HttpServletResponse response, SheetExportVo export) {
        /**
         * 生成excel
         */
        Workbook workbook = new HSSFWorkbook();
        ExcelExportUtil.makeSheet(workbook, export);

        //写入文件
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BizException("", "导出失败");
        }
    }

    /**
     * 导出Excel文件入口
     *
     * @author loki
     * @date 2019/10/15 18:51
     */
    public static void exportToFile(HttpServletResponse response, List<SheetExportVo> exportList) {
        //生成excel
        Workbook workbook = ExcelExportUtil.makeWorkbook(exportList);

        //写入文件
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BizException("", "导出失败");
        }
    }

    /**
     * 导出
     *
     * @author loki
     * @date 2019/10/15 18:53
     */
    private static Workbook makeWorkbook(List<SheetExportVo> exportList) {
        if (CollectionUtils.isEmpty(exportList)) {
            throw new BizException("", "导出数据不能为空");
        }

        Workbook workbook = new HSSFWorkbook();
        for (SheetExportVo export : exportList) {
            ExcelExportUtil.makeSheet(workbook, export);
        }
        return workbook;
    }

    /**
     * 生成sheet
     *
     * @author loki
     * @date 2019/10/18 20:00
     */
    private static void makeSheet(Workbook workbook, SheetExportVo exportVo) {
        Class<?> sheetClass = exportVo.getSheetClass();
        int fieldsNums = sheetClass.getDeclaredFields().length;
        if (fieldsNums <= 0) {
            throw new BizException("", "没有可以导出的字段");
        }

        String sheetName = StringUtils.isBlank(exportVo.getSheetName()) ? exportVo.getSheetClass().getAnnotationsByType(ExcelSheet.class)[0].name() : exportVo.getSheetName();

        List<Field> fields = new ArrayList<>(fieldsNums);
        Field[] fieldsArray = exportVo.getSheetClass().getDeclaredFields();
        for (Field field : fieldsArray) {
            if (field.getAnnotationsByType(ExcelField.class).length > 0) {
                fields.add(field);
            }
        }

        if (CollectionUtils.isEmpty(fields)) {
            throw new BizException("", "没有可以导出的字段");
        }

        /**
         * 生成标题列
         */
        Sheet sheet = workbook.createSheet(sheetName);
        createTableHeader(sheet, workbook, fields);

        /**
         * 导出数据
         */
        if (CollectionUtils.isEmpty((List) exportVo.getData())) {
            return;
        }

        List dataList = (List) exportVo.getData();

        //单元格样式
        CellStyle style = genDataRowCellStyle(workbook);

        for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
            Row rowX = sheet.createRow(dataIndex + 1);
            Object rowData = dataList.get(dataIndex);

            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    Object obj;
                    if (rowData instanceof JSONObject) {
                        obj = JSONObject.parseObject(rowData.toString(), exportVo.getSheetClass());
                    } else {
                        obj = rowData;
                    }
                    fieldValue = field.get(obj);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Cell cellX = rowX.createCell(i, CellType.STRING);
                cellX.setCellStyle(style);
                String cellValue = FieldReflectionUtil.formatValue(field, fieldValue);
                cellX.setCellValue((StringUtils.isBlank(cellValue) || "null" == cellValue) ? "" : cellValue);
            }
        }
    }

    /**
     * 生成标题行
     *
     * @param sheet
     * @param workbook
     * @param fields
     */
    private static void createTableHeader(Sheet sheet, Workbook workbook, List<Field> fields) {
        //生成标题列样式
        CellStyle style = genHeaderCellStyle(workbook);

        //生成标题列
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < fields.size(); i++) {
            sheet.setColumnWidth(i, 20 * 256);
            Field field = fields.get(i);
            String fieldName = field.getAnnotationsByType(ExcelField.class)[0].name();
            Cell cellX = headRow.createCell(i, CellType.STRING);
            cellX.setCellValue(fieldName);
            cellX.setCellStyle(style);

            /**
             * 生成批注
             */
            if (field.getAnnotationsByType(ExcelComment.class).length > 0) {
                createComment(sheet, cellX, field.getAnnotationsByType(ExcelComment.class)[0].text());
            }
        }
    }

    /**
     * 生成标题行的样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle genHeaderCellStyle(Workbook workbook) {
        /**
         * excel 单元格样式
         */
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        /**
         * excel 字体
         */
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);

        style.setFont(font);
        return style;
    }

    /**
     * 生成数据单元格样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle genDataRowCellStyle(Workbook workbook) {
        /**
         * excel 单元格样式
         */
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.JUSTIFY);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        /**
         * 设置单元格为文本格式
         */
        HSSFDataFormat dataFormat = (HSSFDataFormat) workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("@"));

        return style;
    }

    /**
     * 生成批注
     *
     * @author loki
     * @date 2020/03/10 18:02
     */
    private static void createComment(Sheet sheet, Cell cell, String text) {
        Drawing p = sheet.createDrawingPatriarch();
        Comment comment = p.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
        comment.setString(new HSSFRichTextString(text));
        comment.setAuthor("sys");
        cell.setCellComment(comment);
    }
}
