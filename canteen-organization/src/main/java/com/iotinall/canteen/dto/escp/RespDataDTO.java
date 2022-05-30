package com.iotinall.canteen.dto.escp;

import lombok.Data;
import org.json.JSONArray;

/**
 * 获取人员列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class RespDataDTO {
    /**
     * 返回值 true表示调用成功
     */
    private Boolean result;

    /**
     * 返回的提示消息
     */
    private String message;

    /**
     * 人员信息数据集合
     */
    private JSONArray data;
}
