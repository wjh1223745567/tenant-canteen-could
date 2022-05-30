package com.iotinall.canteen.netty.manager;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class FullHttpRequestManager {

    private static final Logger KEEP_ALIVE_LOGGER = LoggerFactory.getLogger(FullHttpRequestManager.class);

    /**
     * 拼装 Http 请求
     *
     * @param api @return
     * @throws URISyntaxException
     */
    public static FullHttpRequest createGetKeepAliceRequest(String api, String json) throws URISyntaxException {
        FullHttpRequest request;
        URI url = new URI(api);
        if (StringUtils.isNotBlank(json)) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url.toASCIIString(), Unpooled.wrappedBuffer(json.getBytes(CharsetUtil.UTF_8)));
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        } else {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url.toASCIIString());
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return request;
    }


    /**
     * 拼装 Http 请求
     *
     * @param api @return
     * @throws URISyntaxException
     */
    public static FullHttpRequest createPostKeepAliceRequest(String api, String json) throws URISyntaxException {
        FullHttpRequest request;
        URI url = new URI(api);
        if (StringUtils.isNotBlank(json)) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, url.toASCIIString(), Unpooled.wrappedBuffer(json.getBytes(CharsetUtil.UTF_8)));
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        } else {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, url.toASCIIString());
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return request;
    }


    /**
     * 拼装 Http 请求
     *
     * @param api @return
     * @throws URISyntaxException
     */
    public static FullHttpRequest createPutKeepAliceRequest(String api, String json) throws URISyntaxException {
        FullHttpRequest request = null;
        URI url = new URI(api);
        if (StringUtils.isNotBlank(json)) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.PUT, url.toASCIIString(), Unpooled.wrappedBuffer(json.getBytes(CharsetUtil.UTF_8)));
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        } else {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.PUT, url.toASCIIString());
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return request;
    }


    /**
     * 拼装 Http 请求
     *
     * @param api @return
     * @throws URISyntaxException
     */
    public static FullHttpRequest createDeleteKeepAliceRequest(String api, String json) throws URISyntaxException {
        FullHttpRequest request = null;
        URI url = new URI(api);
        if (StringUtils.isNotBlank(json)) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.DELETE, url.toASCIIString(), Unpooled.wrappedBuffer(json.getBytes(CharsetUtil.UTF_8)));
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        } else {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.DELETE, url.toASCIIString());
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return request;
    }
}
