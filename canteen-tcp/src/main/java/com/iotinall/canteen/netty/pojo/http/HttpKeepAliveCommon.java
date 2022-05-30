/*
 * Copyright (c) 2019, Zhejiang uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     :
 * Module Name :
 * Date Created: 2020-03-24 18:44
 * Creator     : s04180
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *
 *------------------------------------------------------------------------------
 */
package com.iotinall.canteen.netty.pojo.http;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author loki
 * @date 2020/06/02 16:26
 */
@Data
public class HttpKeepAliveCommon {
    private ChannelHandlerContext ctx;
    private String serialNo;
    private String deviceCode;
}
