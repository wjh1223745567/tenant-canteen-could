package com.iotinall.canteen.netty.manager.handler;

import com.iotinall.canteen.constant.NettyConstants;
import com.iotinall.canteen.netty.manager.codec.HttpDecoder;
import com.iotinall.canteen.netty.manager.codec.HttpEncoder;
import com.iotinall.canteen.netty.manager.codec.TcpDecoder;
import com.iotinall.canteen.netty.manager.codec.TcpEncoder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

/**
 * pipeline
 *
 * @author loki
 * @date 2020/06/04 14:05
 */
@Component
public class PipelineChangeHandler {
    /**
     * http
     *
     * @author loki
     * @date 2020/06/04 14:06
     */
    public void httpPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpDecoder());
        pipeline.addLast(new HttpEncoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        pipeline.addLast(new ReadTimeoutHandler(NettyConstants.READ_TIME_OUT));
        pipeline.addLast(new HttpHandler());
        pipeline.addLast(new HttpResponseHandler());
    }

    /**
     * websocket
     *
     * @author loki
     * @date 2021/02/24 15:15
     */
    public void websocketPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65535));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("http-handler", new WebSocketHandler());
        pipeline.addLast("protocol-handler", new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
    }

    /**
     * tcp
     *
     * @author loki
     * @date 2020/06/04 14:06
     */
    public void tcpPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,
                Unpooled.wrappedBuffer("<END>".getBytes())));
        pipeline.addLast(new TcpDecoder());
        pipeline.addLast(new TcpEncoder());
        pipeline.addLast(new ReadTimeoutHandler(NettyConstants.READ_TIME_OUT));
        pipeline.addLast(new TcpHandler());
    }
}
