package com.iotinall.canteen.dto.notice;

import lombok.Data;

/**
 * 大屏消息通知
 *
 * @author loki
 * @date 2021/7/15 20:44
 **/
@Data
public class FeignDashboardNoticeDTO {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片
     */
    private String img;
}
