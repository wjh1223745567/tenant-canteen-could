package com.iotinall.canteen.utils;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 开源Thumbnailator照片处理工具类
 *
 * @author loki
 * @date 2020/09/21 9:28
 */
@Slf4j
public class ThumbnailatorUtils {
    /**
     * 水印类型
     */
    private static final String STOCK_IN = "stock_in";
    private static final String STOCK_IN_BACK = "stock_in_back";
    private static final String STOCK_OUT = "stock_out";
    private static final String STOCK_OUT_BACK = "stock_out_back";

    public static BufferedImage createWaterMark(MultipartFile file, String type, String goodsName, String billDate, BigDecimal amount, BigDecimal realAmount, String tempFilePath) {
        return createWaterMark(file, type, goodsName, billDate, amount, realAmount, tempFilePath, false);
    }

    /**
     * 生成水印
     *
     * @author loki
     * @date 2020/09/21 9:29
     */
    public static BufferedImage createWaterMark(MultipartFile file, String type, String goodsName, String billDate, BigDecimal amount, BigDecimal realAmount, String tempFilePath, Boolean rotate) {
        //获取地图尺寸
        int width;
        int height;
        BufferedImage bi;
        try {
            bi = ImageIO.read(file.getInputStream());
            width = bi.getWidth();
            height = bi.getHeight();
        } catch (IOException e) {
            throw new BizException("", "生成水印图片失败");
        }

        BufferedImage waterMark;
        switch (type) {
            case STOCK_IN: {
                waterMark = createStockInWaterMark(width, height, goodsName, billDate, amount, realAmount);
                break;
            }
            case STOCK_IN_BACK: {
                waterMark = createStockInBackWaterMark(width, height, goodsName, billDate, amount, realAmount);
                break;
            }
            case STOCK_OUT: {
                waterMark = createStockOutWaterMark(width, height, goodsName, billDate, amount, realAmount);
                break;
            }
            case STOCK_OUT_BACK: {
                waterMark = createStockOutBackWaterMark(width, height, goodsName, billDate, amount, realAmount);
                break;
            }
            default: {
                throw new BizException("", "未知水印类型");
            }

        }

        try {
            String fileName = tempFilePath + System.currentTimeMillis();
            Thumbnails.of(bi).scale(1f)
                    .rotate(rotate ? 90 : 0)
                    .outputFormat("jpg")
                    .watermark(Positions.TOP_LEFT, waterMark, 0.8f)
                    .toFile(fileName);

            File tempFile = new File(fileName + ".jpg");
            BufferedImage bufferedImage = ImageIO.read(tempFile);
            tempFile.delete();
            return bufferedImage;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("", "生成水印失败");
        }
    }

    /**
     * 采购入库水印
     *
     * @author loki
     * @date 2020/09/21 9:36
     */
    private static BufferedImage createStockInWaterMark(int width, int height, String goodsName, String billDate, BigDecimal amount, BigDecimal realAmount) {
        String goodsNameText = "商品：" + goodsName;
        String billDateText = "入库日期：" + billDate;
        String amountText = "采购数量：" + amount;
        String realAmountText = "实际数量：" + realAmount;

        return createWaterMarkDetail(width, height, goodsNameText, billDateText, amountText, realAmountText);
    }

    /**
     * 退货水印
     *
     * @author loki
     * @date 2020/09/21 9:36
     */
    private static BufferedImage createStockInBackWaterMark(int width, int height, String goodsName, String billDate, BigDecimal amount, BigDecimal realAmount) {
        String goodsNameText = "商品：" + goodsName;
        String billDateText = "退货日期：" + billDate;
        String amountText = "退货数量：" + amount;
        String realAmountText = "实际数量：" + realAmount;

        return createWaterMarkDetail(width, height, goodsNameText, billDateText, amountText, realAmountText);
    }

    /**
     * 领用出库水印
     *
     * @author loki
     * @date 2020/09/21 9:36
     */
    private static BufferedImage createStockOutWaterMark(int width, int height, String goodsName, String billDate, BigDecimal amount, BigDecimal realAmount) {
        String goodsNameText = "商品：" + goodsName;
        String billDateText = "出库日期：" + billDate;
        String amountText = "出库数量：" + amount;
        String realAmountText = "实际数量：" + realAmount;

        return createWaterMarkDetail(width, height, goodsNameText, billDateText, amountText, realAmountText);
    }

    /**
     * 领用退库水印
     *
     * @author loki
     * @date 2020/09/21 9:36
     */
    private static BufferedImage createStockOutBackWaterMark(int width, int height, String goodsName, String billDate, BigDecimal amount, BigDecimal realAmount) {
        String goodsNameText = "商品：" + goodsName;
        String billDateText = "退库日期：" + billDate;
        String amountText = "退库数量：" + amount;
        String realAmountText = "实际数量：" + realAmount;

        return createWaterMarkDetail(width, height, goodsNameText, billDateText, amountText, realAmountText);
    }

    /**
     * 生成水印
     *
     * @author loki
     * @date 2020/09/21 9:50
     */
    private static BufferedImage createWaterMarkDetail(int width, int height, String goodsNameText, String billDateText, String amountText, String realAmountText) {
        if (height < 500 || width < 500) {
            throw new BizException("", "图片太小，请上传实物拍摄图片");
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);


        g = image.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.RED);
        g.setFont(new Font("宋体", Font.PLAIN, getFontSize(width, height)));

        int gHeight = getFontHeight(height);
        g.drawString(goodsNameText, 15, gHeight * 1);
        g.drawString(LocalDateTimeUtil.localDatetime2Str(LocalDateTime.now()), width / 5 * 3, gHeight);
        g.drawString(billDateText, 15, gHeight * 2 - 5);
        g.drawString(amountText, 15, gHeight * 3 - 5);
        g.drawString(realAmountText, 15, gHeight * 4 - 5);
        g.drawString("经办人：" + SecurityUtils.getUserName(), 15, gHeight * 5 - 5);

        g.dispose();
        return image;
    }

    /**
     * 获取字体的大小
     *
     * @author loki
     * @date 2020/09/21 15:57
     */
    private static int getFontSize(int width, int height) {
        int fontSize = height > width ? 100 : 60;
        int gFontSize = fontSize * width / 3000;
        return gFontSize > 100 ? 90 : gFontSize;
    }

    /**
     * 获取字体的高度
     *
     * @author loki
     * @date 2020/09/21 15:57
     */
    private static int getFontHeight(int height) {
        int gHeight = 120 * height / 3000;
        return gHeight > 120 ? 120 : gHeight;
    }
}
