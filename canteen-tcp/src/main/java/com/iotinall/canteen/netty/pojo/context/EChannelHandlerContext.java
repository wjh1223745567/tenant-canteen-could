package com.iotinall.canteen.netty.pojo.context;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author loki
 * @date 2020/06/02 16:28
 */
@Data
public class EChannelHandlerContext {

    private ChannelHandlerContext ctx;

    private String serialNo;

    private String ip;

    private Boolean lock = false;

    /**
     * 0 - 未绑定， 1 - 绑定
     */
    private int isBound = 0;

    public EChannelHandlerContext(ChannelHandlerContext ctx, String serialNo, String ip, int isBound) {
        this.ctx = ctx;
        this.serialNo = serialNo;
        this.ip = ip;
        this.isBound = isBound;
    }
}
