/*
 * Copyright (c) 2019, Zhejiang uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     :
 * Module Name :
 * Date Created: 2020-03-25 10:08
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

import lombok.Data;

import java.util.concurrent.BlockingQueue;

/**
 * 请求阻塞队列
 *
 * @author loki
 * @date 2020/06/02 16:27
 */
@Data
public class RequestBlockingQueue {

    private BlockingQueue<HttpKeepAliveRequest> requestBlockingQueue;
    private int iResponseKey;
    private BlockingQueue<String> responseBlockingQueue;

    private BlockingQueue<HttpKeepAliveRequest> firstRequestBlockingQueue;
}
