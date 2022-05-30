package com.iotinall.canteen.netty.manager.codec;

import io.netty.handler.codec.http.*;

/**
 * 解码器
 *
 * @author loki
 * @date 2020/06/02 14:02
 */
public class HttpDecoder extends HttpObjectDecoder {

    /**
     * 相应未知状态
     */
    private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unkown");

    /**
     * 是否是request解码
     */
    private boolean isDecodingRequest = true;

    @Override
    protected boolean isDecodingRequest() {
        return isDecodingRequest;
    }

    @Override
    protected HttpMessage createMessage(String[] initialLine) throws Exception {
        if (initialLine[0].contains("HTTP")) {
            isDecodingRequest = false;
        } else if (initialLine[2].contains("HTTP")) {
            isDecodingRequest = true;
        }
        if (isDecodingRequest) {
            return new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]),
                    HttpMethod.valueOf(initialLine[0]), initialLine[1], validateHeaders);
        } else {
            return new DefaultHttpResponse(HttpVersion.valueOf(initialLine[0]),
                    new HttpResponseStatus(Integer.parseInt(initialLine[1]), initialLine[2]), validateHeaders);
        }
    }

    @Override
    protected HttpMessage createInvalidMessage() {
        if (isDecodingRequest) {
            return new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/bad-request", validateHeaders);
        } else {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, UNKNOWN_STATUS, validateHeaders);
        }
    }
}
