package com.iotinall.canteen.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 厨师档案
 */
@Data
@Accessors(chain = true)
public class FeignMessProductCookFilesView {

    private String name;

    /**
     * 获取日期
     */
    private LocalDate haveDate;

    /**
     * 上传图片路径
     */
    private String url;

}
