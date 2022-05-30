/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/9
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.iotinall.canteen.netty.pojo.equip.heart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 心跳
 *
 * @author loki
 * @date 2020/06/02 16:25
 */
@Data
public class HeartBack {

    @JSONField(name = "ResponseURL", ordinal = 1)
    private String responseURL = "/LAPI/V1.0/PACS/Controller/HeartReportInfo";

    @JSONField(name = "Code", ordinal = 2)
    private String code = "0";

    @JSONField(name = "Data", ordinal = 3)
    private HeartReportData data;


    @Override
    public String toString() {
        return "{\r\n" +
                "\"ResponseURL\":\"" + responseURL + "\"," + "\r\n" +
                "\"Code\": \"" + code + '\"' + ",\r\n" +
                "\"Data\": " + data.toString() +
                '}';
    }
}
