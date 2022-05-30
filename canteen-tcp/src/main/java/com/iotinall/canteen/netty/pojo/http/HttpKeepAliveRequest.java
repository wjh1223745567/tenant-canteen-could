/*
 * Copyright (c) 2019, Zhejiang uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     :
 * Module Name :
 * Date Created: 2020-03-24 20:16
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

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

/**
 * @author loki
 * @date 2020/06/02 16:27
 */
@Data
public class HttpKeepAliveRequest {

    private FullHttpRequest httpRequest;
    private String method;
    private String api;

    /**
     * 发送请求的线程是否启动
     */
    private int iKeepAliveThread = 0;

}
