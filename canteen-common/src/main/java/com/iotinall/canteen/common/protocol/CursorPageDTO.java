package com.iotinall.canteen.common.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xin-bing
 * @date 10/30/2019 15:32
 */
@Data
public class CursorPageDTO<T> {
    private Serializable cursor;
    private T content;
    private Long total;

    public CursorPageDTO(){}

    public CursorPageDTO(Serializable cursor, T content, Long total) {
        this.cursor = cursor;
        this.content = content;
        this.total = total;
    }

    public static <T> CursorPageDTO<T> of(Serializable cursor, T content, Long total) {
        return new CursorPageDTO<>(cursor, content, total);
    }
}
