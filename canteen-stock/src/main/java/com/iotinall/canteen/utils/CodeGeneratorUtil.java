package com.iotinall.canteen.utils;

import com.iotinall.canteen.common.exception.BizException;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * code 生成器
 *
 * @author loki
 * @date 2020/09/11 9:28
 */
public class CodeGeneratorUtil {
    private static final AtomicInteger SEQ = new AtomicInteger(10);

    /**
     * 生成编号
     * STOCK_IN(0, "采购入库"),
     * STOCK_OUT(1, "领用出库"),
     * STOCK_IN_BACK(2, "采购退货"),
     * STOCK_OUT_BACK(3, "领用退库"),
     * STOCK_INVENTORY(4, "库存盘点"),
     * GOODS(5, "商品编号");
     * SUPPLIER(6, "供应商编号");
     *
     * @author loki
     * @date 2020/08/31 16:59
     */
    public static String buildCode(Integer type) {
        return buildCode(0, type);
    }

    public static String buildCode(Integer platform, Integer type) {
        String code;
        switch (type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4: {
                code = buildBillNo(type, platform);
                break;
            }
            case 5: {
                code = buildGoodsCode();
                break;
            }
            case 6: {
                code = "ST";
                break;
            }
            default: {
                throw new BizException("", "未知编号类型");
            }
        }

        if (SEQ.intValue() > 98) {
            SEQ.getAndSet(10);
        }
        return code + DigitalUtil.genCheckCode(2) + SEQ.getAndIncrement();
    }

    /**
     * 生成单据编号
     *
     * @author loki
     * @date 2020/08/31 17:14
     */
    private static String buildBillNo(Integer type, Integer platform) {
        String billNo = "";
        switch (type) {
            case 0: {
                billNo = "CK";
                break;
            }
            case 1: {
                billNo = "LK";
                break;
            }
            case 2: {
                billNo = "CT";
                break;
            }
            case 3: {
                billNo = "LT";
                break;
            }
            case 4: {
                billNo = "PD";
                break;
            }

            default: {
                throw new BizException("", "未知编号类型");
            }
        }

        return billNo + platform + LocalDateTimeUtil.localDatetime2Str(LocalDateTime.now(), DateTimeFormatters.YYYYMMDD);
    }

    /**
     * 生成货品编号
     *
     * @author loki
     * @date 2020/08/31 17:30
     */
    public static String buildGoodsTypeCode() {
        return "T" + DigitalUtil.genCheckCode(4);
    }

    /**
     * 生成货品编号
     *
     * @author loki
     * @date 2020/08/31 17:30
     */
    public static String buildGoodsCode() {
        return "GT" + DigitalUtil.genCheckCode(2);
    }
}
